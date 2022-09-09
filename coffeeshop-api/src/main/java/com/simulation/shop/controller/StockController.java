package com.simulation.shop.controller;

import com.coffee.shared.entity.OrdersTable;
import com.simulation.shop.service.OrdersService;
import com.simulation.shop.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("stock")
public class StockController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockController.class);

    @Autowired
    private StockService service;

    @GetMapping(value = "beans")
    public long fetchBeansStock() {
        return service.fetchBeansStock();
    }

    @GetMapping(value = "milk")
    public long fetchMilkStock() {
        return service.fetchMilkStock();
    }

}