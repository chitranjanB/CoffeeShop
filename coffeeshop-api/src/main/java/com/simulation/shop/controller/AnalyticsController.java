package com.simulation.shop.controller;

import com.simulation.shop.model.Benchmark;
import com.simulation.shop.service.AnalyticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyticsController.class);

    @Autowired
    private AnalyticsService service;

    @PostMapping(value="/data")
    public List<Benchmark> grind() {
        return service.getData();
    }
}
