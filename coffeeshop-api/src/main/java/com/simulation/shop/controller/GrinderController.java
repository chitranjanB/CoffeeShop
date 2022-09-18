package com.simulation.shop.controller;

import com.coffee.shared.model.Grounds;
import com.coffee.shared.request.InputRequest;
import com.simulation.shop.service.GrindingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/machine")
@CrossOrigin(origins = "http://localhost:3000")
public class GrinderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrinderController.class);

    @Autowired
    private GrindingService service;

    @PostMapping(value="/grind")
    public Grounds grind(@RequestBody InputRequest request) {
        return service.grind(request.getTransactionId());
    }

}
