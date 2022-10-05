package com.simulation.shop.controller;

import com.coffee.shared.model.TransactionStatus;
import com.coffee.shared.request.OrderRequest;
import com.coffee.shared.request.TransactionRequest;
import com.simulation.shop.service.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("process")
@CrossOrigin(origins = "http://localhost:3000")
public class ProcessController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessController.class);

    @Autowired
    private ProcessService service;

    @PostMapping
    public List<TransactionStatus> process(@RequestHeader(value = HttpHeaders.AUTHORIZATION, defaultValue = "") String auth, @RequestBody OrderRequest request) {
        String orderId = service.queueOrder(request, auth);
        List<TransactionStatus> transactionStatusList = service.processOrder(orderId);
        service.archiveOrder(orderId);
        return transactionStatusList;
    }

    @PostMapping(value = "/packageCoffee")
    public void packageCoffee(@RequestHeader(value = HttpHeaders.AUTHORIZATION, defaultValue = "") String auth, @RequestBody TransactionRequest request) {
        service.packageCoffee(request.getTransactionId());
    }

    @PostMapping(value = "/collect",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<ByteArrayResource> collectCoffee(@RequestHeader(value = HttpHeaders.AUTHORIZATION, defaultValue = "") String auth, @RequestParam String transactionId) {
        ByteArrayResource resource = service.fetchCoffee(transactionId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=coffee.zip")
                .contentType(MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .contentLength(resource.contentLength())
                .body(resource);
    }

}
