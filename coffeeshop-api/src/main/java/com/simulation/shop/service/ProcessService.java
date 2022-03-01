package com.simulation.shop.service;

import com.coffee.shared.entity.AuditLog;
import com.coffee.shared.entity.CoffeeEntity;
import com.coffee.shared.entity.OrdersTable;
import com.coffee.shared.entity.StepTransactionId;
import com.coffee.shared.model.*;
import com.coffee.shared.request.InputRequest;
import com.coffee.shared.request.OrderRequest;
import com.simulation.shop.CoffeeShopException;
import com.simulation.shop.config.Constants;
import com.simulation.shop.controller.ProcessController;
import com.simulation.shop.repository.AuditLogRepository;
import com.simulation.shop.repository.CoffeeRepository;
import com.simulation.shop.repository.OrdersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.ArrayList;
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
    private OrdersRepository ordersRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private CoffeeRepository coffeeRepository;

    public List<String> queueOrder(OrderRequest request) {
        int orders = request.getOrders();
        List<String> transactionList = new ArrayList<>();
        for (int i = 0; i < orders; i++) {
            OrdersTable ordersTable = new OrdersTable();
            ordersTable.setCustomerId(request.getCustomerId());
            ordersTable.setStatus(Status.PENDING);
            ordersRepository.save(ordersTable);

            transactionList.add(ordersTable.getTransactionId());
        }
        return transactionList;
    }

    public List<OrderStatus> processOrder(List<String> orderList) {
        List<OrderStatus> statusList = new ArrayList<>();

        for (String transactionId : orderList) {
            InputRequest inputRequest = new InputRequest();
            inputRequest.setTransactionId(transactionId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<InputRequest> requestEntity = new HttpEntity<InputRequest>(inputRequest, headers);

            OrderStatus orderStatus = new OrderStatus(transactionId);
            try {
                String host = "http://localhost:8080/";
                ResponseEntity<Grounds> groundsResponse = restTemplate.postForEntity(host + "machine/grind", inputRequest, Grounds.class);
                ResponseEntity<Coffee> espressoResponse = restTemplate.postForEntity(host + "machine/espresso", inputRequest, Coffee.class);
                ResponseEntity<SteamedMilk> steamedResponse = restTemplate.postForEntity(host + "machine/steam", inputRequest, SteamedMilk.class);
                restTemplate.postForLocation(host + "process/packageCoffee", transactionId);
                orderStatus.setStatus(Status.COMPLETE);
            } catch (Exception e) {
                LOGGER.error("Error while processing order " + transactionId, e);
                orderStatus.setStatus(Status.ERROR);
            }
            archiveOrder(orderStatus);
            statusList.add(orderStatus);
        }
        return statusList;
    }

    private void archiveOrder(OrderStatus orderStatus) {
        Optional<OrdersTable> optional = ordersRepository.findById(orderStatus.getTransactionId());
        OrdersTable order = optional.orElseThrow(() -> new CoffeeShopException("No order details available"));
        order.setStatus(orderStatus.getStatus());
        ordersRepository.save(order);
    }

    public OrdersTable findOrder(String transactionId) {
        Optional<OrdersTable> optional = ordersRepository.findById(transactionId);
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

            OrdersTable order = findOrder(transactionId);
            AuditLog grinderLog = findAuditLog(Step.GRIND_COFFEE, transactionId);
            AuditLog espressoLog = findAuditLog(Step.MAKE_ESPRESSO, transactionId);
            AuditLog steamerLog = findAuditLog(Step.STEAM_MILK, transactionId);

            ZipEntry entry = new ZipEntry(Constants.COFFEE_JAR_ENTRY);
            innerZipOutput.putNextEntry(entry);
            innerZipOutput.setMethod(ZipOutputStream.STORED);
            innerZipOutput.setLevel(5);

            String customerId = order.getCustomerId();
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
