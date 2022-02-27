package com.simulation.shop.controller;

import com.coffee.shared.model.SteamedMilk;
import com.coffee.shared.request.InputRequest;
import com.simulation.shop.service.SteamerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/machine")
public class SteamerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SteamerController.class);

    @Autowired
    private SteamerService service;

    @PostMapping(value = "/steam")
    public SteamedMilk grind(@RequestBody InputRequest request) {
        return service.steam(request.getTransactionId());
    }

}
