package com.simulation.shop.controller;

import com.simulation.shop.model.OrderStatus;
import com.simulation.shop.request.InputRequests;
import com.simulation.shop.request.OrderRequest;
import com.simulation.shop.service.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("process")
public class ProcessController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessController.class);

    @Autowired
    private ProcessService service;

    @PostMapping
    public List<OrderStatus> process(@RequestBody OrderRequest request) {
        List<String> orderList = service.queueOrder(request);
        List<OrderStatus> orderStatusList = service.processOrder(orderList);
        //TODO return response as downloadable zip
        return orderStatusList;
    }

    @PostMapping(value = "/serveCoffee",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<ByteArrayResource> serveCoffee(@RequestBody InputRequests inputRequests) {
        ByteArrayResource resource = service.buildBulkOrder(inputRequests);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=coffee.zip")
                .contentType(MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .contentLength(resource.contentLength())
                .body(resource);
    }

}
