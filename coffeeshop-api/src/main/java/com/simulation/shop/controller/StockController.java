package com.simulation.shop.controller;

import com.simulation.shop.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("stock")
@CrossOrigin(origins = "*")
public class StockController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockController.class);

    @Autowired
    private StockService service;

    @GetMapping(value = "beans")
    public long fetchBeansStock(@RequestHeader(value = HttpHeaders.AUTHORIZATION, defaultValue = "") String auth) {
        return service.fetchBeansStock();
    }

    @GetMapping(value = "milk")
    public long fetchMilkStock(@RequestHeader(value = HttpHeaders.AUTHORIZATION, defaultValue = "") String auth) {
        return service.fetchMilkStock();
    }

}
