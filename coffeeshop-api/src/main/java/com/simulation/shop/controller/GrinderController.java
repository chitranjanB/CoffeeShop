package com.simulation.shop.controller;

import com.coffee.shared.model.Grounds;
import com.coffee.shared.request.TransactionRequest;
import com.simulation.shop.service.GrindingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/machine")
@CrossOrigin(origins = "*")
public class GrinderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrinderController.class);

    @Autowired
    private GrindingService service;

    @PostMapping(value = "/grind")
    public Grounds grind(@RequestHeader(value = HttpHeaders.AUTHORIZATION, defaultValue = "") String auth, @RequestBody TransactionRequest request) {
        return service.grind(request.getTransactionId());
    }

}
