package com.simulation.shop.controller;

import com.coffee.shared.model.AnalyticsTimeline;
import com.coffee.shared.model.MachineBenchmark;
import com.coffee.shared.model.OrderTimeline;
import com.simulation.shop.service.AnalyticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyticsController.class);

    @Autowired
    private AnalyticsService service;

    @PostMapping(value="/system-benchmark")
    public List<AnalyticsTimeline> fetchSystemBenchmark(@RequestHeader(value = HttpHeaders.AUTHORIZATION, defaultValue = "") String auth) {
        return service.fetchAnalyticsTimeline();
    }

    @GetMapping(value="/machine-efficiency")
    public List<MachineBenchmark> fetchMachineEfficiency(@RequestHeader(value = HttpHeaders.AUTHORIZATION, defaultValue = "") String auth){return service.fetchMachineEfficiency();}

    @GetMapping(value="/timeline")
    public OrderTimeline fetch(@RequestHeader(value = HttpHeaders.AUTHORIZATION, defaultValue = "") String auth, @RequestParam String transactionId){return service.findAuditLog(transactionId);}

}
