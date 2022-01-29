package com.simulation.shop.controller;

import com.simulation.shop.model.Grounds;
import com.simulation.shop.request.InputRequest;
import com.simulation.shop.service.GrindingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/machine")
public class GrinderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrinderController.class);

    @Autowired
    private GrindingService service;

    @PostMapping(value="/grind")
    public Grounds grind(InputRequest request) {
        return service.grind(request.getTransactionId());
    }

}
