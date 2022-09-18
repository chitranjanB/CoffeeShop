package com.simulation.shop.controller;

import com.coffee.shared.model.OrderStatus;
import com.coffee.shared.request.OrderRequest;
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
    public List<OrderStatus> process(@RequestBody OrderRequest request) {
        List<String> orderList = service.queueOrder(request);
        List<OrderStatus> orderStatusList = service.processOrder(orderList);
        return orderStatusList;
    }

    @PostMapping(value = "/packageCoffee")
    public void packageCoffee(@RequestBody  String transactionId) {
        service.packageCoffee(transactionId);
    }

    @PostMapping(value = "/collect",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<ByteArrayResource> collectCoffee(@RequestParam String transactionId) {
        ByteArrayResource resource = service.fetchCoffee(transactionId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=coffee.zip")
                .contentType(MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .contentLength(resource.contentLength())
                .body(resource);
    }

}
