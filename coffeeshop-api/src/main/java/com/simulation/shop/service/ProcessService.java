package com.simulation.shop.service;

import com.simulation.shop.CoffeeShopException;
import com.simulation.shop.config.Constants;
import com.simulation.shop.config.Step;
import com.simulation.shop.controller.ProcessController;
import com.simulation.shop.entity.AuditLog;
import com.simulation.shop.entity.OrdersTable;
import com.simulation.shop.entity.StepTransactionId;
import com.simulation.shop.model.*;
import com.simulation.shop.repository.AuditLogRepository;
import com.simulation.shop.repository.OrdersRepository;
import com.simulation.shop.request.InputRequest;
import com.simulation.shop.request.InputRequests;
import com.simulation.shop.request.OrderRequest;
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
import java.util.UUID;
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
                ResponseEntity<Grounds> groundsResponse = restTemplate.postForEntity("http://localhost:8080/machine/grind", inputRequest, Grounds.class);
                ResponseEntity<Coffee> espressoResponse = restTemplate.postForEntity("http://localhost:8080/machine/espresso", inputRequest, Coffee.class);
                ResponseEntity<SteamedMilk> steamedResponse = restTemplate.postForEntity("http://localhost:8080/machine/steam", inputRequest, SteamedMilk.class);
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
     * @param inputRequests
     * @return
     */
    public ByteArrayResource buildBulkOrder(InputRequests inputRequests) {
        ByteArrayResource resource = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zout = new ZipOutputStream(baos);) {

            for (InputRequest request : inputRequests.getInputRequestList()) {
                String transactionId = request.getTransactionId();
                byte[] innerZipBytes = packageCoffee(zout, transactionId);

                ZipEntry entry = new ZipEntry("coffee-" + transactionId + ".zip");
                zout.putNextEntry(entry);
                zout.write(innerZipBytes);
                zout.closeEntry();
            }
            zout.flush();
            zout.finish();
            resource = new ByteArrayResource(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resource;
    }

    private byte[] packageCoffee(ZipOutputStream zout, String transactionId) throws IOException {
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
