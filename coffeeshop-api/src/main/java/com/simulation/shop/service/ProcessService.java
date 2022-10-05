package com.simulation.shop.service;

import com.coffee.shared.entity.CoffeeOrder;
import com.coffee.shared.entity.*;
import com.coffee.shared.model.*;
import com.coffee.shared.request.OrderRequest;
import com.coffee.shared.request.TransactionRequest;
import com.simulation.shop.CoffeeShopException;
import com.simulation.shop.config.Constants;
import com.simulation.shop.controller.ProcessController;
import com.simulation.shop.repository.AuditLogRepository;
import com.simulation.shop.repository.CoffeeOrderRepository;
import com.simulation.shop.repository.CoffeeRepository;
import com.simulation.shop.repository.TransactionSequenceRepository;
import com.simulation.shop.util.CoffeeUtility;
import com.simulation.shop.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ProcessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CoffeeOrderRepository coffeeOrderRepository;

    @Autowired
    private TransactionSequenceRepository transactionSequenceRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private CoffeeRepository coffeeRepository;

    @Autowired
    private CoffeeUtility util;

    @Autowired
    private JwtUtils jwtUtils;

    public String queueOrder(OrderRequest request, String auth) {
        int count = request.getOrders();
        List<TransactionSequence> transactionSequences = new ArrayList<>();
        CoffeeOrder order = new CoffeeOrder();

        Optional<Claims> claims = Optional.ofNullable(jwtUtils.verify(auth));
        String customerId = claims.map(claim -> (String) claim.get("emailId"))
                .orElseThrow(()->new IllegalArgumentException("Customer detail not associated with order"));
        order.setCustomerId(customerId);

        order.setStatus(Status.PENDING);
        order.setOrderCreatedDate(new Date());
        for (int i = 0; i < count; i++) {
            TransactionSequence transSeq = new TransactionSequence();
            transSeq.setCustomerId(request.getCustomerId());
            transSeq.setStatus(Status.PENDING);
            transSeq.setTimeStarted(new Date());
            transSeq.setOrder(order);
            transactionSequences.add(transSeq);
        }
        order.setTransactionSequences(transactionSequences);
        coffeeOrderRepository.save(order);

        return order.getOrderId();
    }

    public List<TransactionStatus> processOrder(String orderId) {
        Optional<CoffeeOrder> optional = coffeeOrderRepository.findById(orderId);
        CoffeeOrder order = optional.orElseThrow(() -> new CoffeeShopException("No Order details available for order " + orderId));
        List<TransactionSequence> transactionSequences = transactionSequenceRepository.findByOrder(order);

        List<TransactionStatus> statusList = new ArrayList<>();
        for (TransactionSequence transactionSequence : transactionSequences) {
            TransactionRequest inputRequest = new TransactionRequest();
            inputRequest.setTransactionId(transactionSequence.getTransactionId());


            TransactionStatus transactionStatus = new TransactionStatus(transactionSequence.getTransactionId());
            try {
                String host = "http://localhost:8080/";
                String appToken = util.generateAppToken();

                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", appToken);
                HttpEntity<TransactionRequest> requestEntity = new HttpEntity<TransactionRequest>(inputRequest, headers);

                //TODO Kafka - send to Grinder, Espresso, Steam queues, remove rest calls
                //TODO create separate microservices for below resources, which acts as kafka consumers
                ResponseEntity<Grounds> groundsResponse = restTemplate.postForEntity(host + "machine/grind", requestEntity, Grounds.class);
                ResponseEntity<Coffee> espressoResponse = restTemplate.postForEntity(host + "machine/espresso", requestEntity, Coffee.class);
                ResponseEntity<SteamedMilk> steamedResponse = restTemplate.postForEntity(host + "machine/steam", requestEntity, SteamedMilk.class);
                restTemplate.postForLocation(host + "process/packageCoffee", requestEntity);
                transactionStatus.setStatus(Status.COMPLETE);
            } catch (Exception e) {
                LOGGER.error("Error while processing order " + transactionSequence.getTransactionId(), e);
                transactionStatus.setStatus(Status.ERROR);
            }
            archiveTransaction(transactionStatus);
            statusList.add(transactionStatus);
        }
        return statusList;
    }

    private void archiveTransaction(TransactionStatus transactionStatus) {
        Optional<TransactionSequence> optional = transactionSequenceRepository.findById(transactionStatus.getTransactionId());
        TransactionSequence transaction = optional.orElseThrow(() -> new CoffeeShopException("No transaction details available"));
        transaction.setStatus(transactionStatus.getStatus());
        transaction.setTimeEnded(new Date());
        transactionSequenceRepository.save(transaction);
    }

    public void archiveOrder(String orderId) {
        Optional<CoffeeOrder> optional = coffeeOrderRepository.findById(orderId);
        CoffeeOrder order = optional.orElseThrow(() -> new CoffeeShopException("No Order details available for order " + orderId));
        order.setStatus(Status.COMPLETE);
        order.setOrderEndedDate(new Date());
        coffeeOrderRepository.save(order);
    }

    public TransactionSequence findOrder(String transactionId) {
        Optional<TransactionSequence> optional = transactionSequenceRepository.findById(transactionId);
        return optional.orElseThrow(() -> new IllegalStateException("Unable to find order orderId: " + transactionId));
    }

    public AuditLog findAuditLog(Step step, String transactionId) {
        Optional<AuditLog> optional = auditLogRepository.findById(new StepTransactionId(step, transactionId));
        return optional.get();
    }

    /**
     * This method package coffee given a bulk order by a customer at one time
     *
     * @param transactionId
     * @return
     */
    public void packageCoffee(String transactionId) {
        try {
            byte[] innerZipBytes = buildCoffee(transactionId);
            CoffeeEntity coffee = new CoffeeEntity();
            coffee.setTransactionId(transactionId);
            coffee.setData(innerZipBytes);
            coffeeRepository.save(coffee);
        } catch (IOException e) {
            LOGGER.error("Error while saving coffee to DB " + transactionId, e);
        }

    }

    private byte[] buildCoffee(String transactionId) throws IOException {
        byte[] innerByteArray = null;

        try (ByteArrayOutputStream innerZipBufferOutput = new ByteArrayOutputStream();
             ZipOutputStream innerZipOutput = new ZipOutputStream(new BufferedOutputStream(innerZipBufferOutput));) {

            TransactionSequence transactionSequence = findOrder(transactionId);
            AuditLog grinderLog = findAuditLog(Step.GRIND_COFFEE, transactionId);
            AuditLog espressoLog = findAuditLog(Step.MAKE_ESPRESSO, transactionId);
            AuditLog steamerLog = findAuditLog(Step.STEAM_MILK, transactionId);

            ZipEntry entry = new ZipEntry(Constants.COFFEE_JAR_ENTRY);
            innerZipOutput.putNextEntry(entry);
            innerZipOutput.setMethod(ZipOutputStream.STORED);
            innerZipOutput.setLevel(5);

            String customerId = transactionSequence.getCustomerId();
            String info = buildCoffeeData(grinderLog, espressoLog, steamerLog, customerId);

            InputStream is = new ByteArrayInputStream(info.getBytes());

            innerZipOutput.setComment("Order : " + transactionId + " by customer " + customerId);

            int data = 0;
            while ((data = is.read()) != -1) {
                innerZipOutput.write(data);
            }

            innerZipOutput.closeEntry();
            innerZipOutput.finish();
            innerZipOutput.flush();
            is.close();
            innerByteArray = innerZipBufferOutput.toByteArray();
        }

        return innerByteArray;
    }

    public ByteArrayResource fetchCoffee(String transactionId){
        Optional<CoffeeEntity> optional = coffeeRepository.findById(transactionId);
        byte[] data = optional.orElse(new CoffeeEntity()).getData();
        return new ByteArrayResource(data);
    }

    private String buildCoffeeData(AuditLog grinderLog, AuditLog espressoLog, AuditLog steamerLog, String customerId) {
        String grindingMachineName = grinderLog.getMachineName();
        String grindingThread = grinderLog.getThreadName();
        long grindingTime = grinderLog.getTimeElapsed();

        String espressoMachineName = espressoLog.getMachineName();
        String espressoThread = espressoLog.getThreadName();
        long espressoTime = espressoLog.getTimeElapsed();

        String steamingMachineName = steamerLog.getMachineName();
        String steamingThread = steamerLog.getThreadName();
        long steamingTime = steamerLog.getTimeElapsed();

        return String.format(Constants.DATA_FORMAT, customerId,
                grindingMachineName, grindingThread, grindingTime,
                espressoMachineName, espressoThread, espressoTime,
                steamingMachineName, steamingThread, steamingTime);
    }

}
