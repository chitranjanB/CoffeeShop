package com.simulation.shop.controller;

import com.coffee.shared.model.SteamedMilk;
import com.coffee.shared.request.TransactionRequest;
import com.simulation.shop.service.SteamerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/machine")
@CrossOrigin(origins = "*")
public class SteamerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SteamerController.class);

    @Autowired
    private SteamerService service;

    @PostMapping(value = "/steam")
    public SteamedMilk grind(@RequestHeader(value = HttpHeaders.AUTHORIZATION, defaultValue = "") String auth, @RequestBody TransactionRequest request) {
        return service.steam(request.getTransactionId());
    }

}
