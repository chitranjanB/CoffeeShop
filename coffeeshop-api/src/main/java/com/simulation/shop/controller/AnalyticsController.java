package com.simulation.shop.controller;

import com.coffee.shared.model.Benchmark;
import com.simulation.shop.service.AnalyticsService;
import com.simulation.shop.service.MachineBenchmark;
import com.simulation.shop.service.OrderTimeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analytics")
@CrossOrigin(origins = "http://localhost:3000")
public class AnalyticsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyticsController.class);

    @Autowired
    private AnalyticsService service;

    @PostMapping(value="/data")
    public List<Benchmark> grind() {
        return service.getData();
    }

    @GetMapping(value="/machine-efficiency")
    public List<MachineBenchmark> fetchMachineEfficiency(){return service.fetchMachineEfficiency();}

    @GetMapping(value="/timeline")
    public OrderTimeline fetch(@RequestParam String transactionId){return service.findAuditLog(transactionId);}

}
