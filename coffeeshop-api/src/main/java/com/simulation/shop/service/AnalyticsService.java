package com.simulation.shop.service;

import com.coffee.shared.entity.AuditLog;
import com.coffee.shared.entity.StepTransactionId;
import com.coffee.shared.model.Benchmark;
import com.coffee.shared.model.Data;
import com.coffee.shared.model.MachineBenchmark;
import com.coffee.shared.model.Step;
import com.simulation.shop.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnalyticsService {

    private static final int MAX_PAGES = 70;

    @Autowired
    private AuditLogRepository repository;

    public List<Benchmark> getData() {
        //Takes only recent 50 records
        long count = repository.count();
        PageRequest lastPageAsc = PageRequest.of((int) count / MAX_PAGES, MAX_PAGES);

        List<AuditLog> all = repository.findAll(lastPageAsc).getContent();

        List<Benchmark> benchmarks = new ArrayList<>();

        List<Data> grindList = new ArrayList<>();
        List<Data> espressoList = new ArrayList<>();
        List<Data> steamerList = new ArrayList<>();
        for (AuditLog auditLog : all) {

            String customerId = auditLog.getCustomerId();
            Date startDate = auditLog.getTimeStarted();
            Date endDate = auditLog.getTimeEnded();
            Step step = auditLog.getStepTransactionId().getStep();

            switch (step) {
                case GRIND_COFFEE:
                    grindList.add(new Data(customerId, startDate, endDate));
                    break;
                case MAKE_ESPRESSO:
                    espressoList.add(new Data(customerId, startDate, endDate));
                    break;
                case STEAM_MILK:
                    steamerList.add(new Data(customerId, startDate, endDate));
                    break;
            }
        }

        Benchmark grindBenchmark = new Benchmark();
        grindBenchmark.setData(grindList);
        grindBenchmark.setName(Step.GRIND_COFFEE);

        Benchmark espressoBenchmark = new Benchmark();
        espressoBenchmark.setData(espressoList);
        espressoBenchmark.setName(Step.MAKE_ESPRESSO);

        Benchmark steamerBenchmark = new Benchmark();
        steamerBenchmark.setData(steamerList);
        steamerBenchmark.setName(Step.STEAM_MILK);

        benchmarks.add(grindBenchmark);
        benchmarks.add(espressoBenchmark);
        benchmarks.add(steamerBenchmark);

        return benchmarks;

    }

    public List<MachineBenchmark> fetchMachineEfficiency() {
        Map<String, MachineBenchmark> map = new HashMap<>();
        List<AuditLog> all = repository.findAll();

        //TODO filter out completed orders, show only last 10 orders
        for (AuditLog auditLog : all) {
            String transactionId = auditLog.getStepTransactionId().getTransactionId();
            Step step = auditLog.getStepTransactionId().getStep();
            long stepTimeElapsed = auditLog.getTimeElapsed();

            if (map.containsKey(transactionId)) {
                MachineBenchmark mb = map.get(transactionId);
                if (Step.GRIND_COFFEE.equals(step)) {
                    mb.setTimeTakenByGrinderMachine(stepTimeElapsed);
                } else if (Step.MAKE_ESPRESSO.equals(step)) {
                    mb.setTimeTakenByEspressoMachine(stepTimeElapsed);
                } else if (Step.STEAM_MILK.equals(step)) {
                    mb.setTimeTakenBySteamerMachine(stepTimeElapsed);
                }
            } else {
                MachineBenchmark mb = new MachineBenchmark();
                mb.setTransactionId(transactionId);
                if (Step.GRIND_COFFEE.equals(step)) {
                    mb.setTimeTakenByGrinderMachine(stepTimeElapsed);
                } else if (Step.MAKE_ESPRESSO.equals(step)) {
                    mb.setTimeTakenByEspressoMachine(stepTimeElapsed);
                } else if (Step.STEAM_MILK.equals(step)) {
                    mb.setTimeTakenBySteamerMachine(stepTimeElapsed);
                }
                map.put(transactionId, mb);
            }
        }

        List<MachineBenchmark> values = new ArrayList<>(map.values());

        return values;
    }

    public OrderTimeline findAuditLog(String transactionId) {
        AuditLog grindCoffeeAuditLog = repository.findById(new StepTransactionId(Step.GRIND_COFFEE, transactionId)).orElseThrow(() -> new IllegalStateException("Unable to find " + Step.GRIND_COFFEE + " details for " + transactionId));
        AuditLog makeEspressoAuditLog = repository.findById(new StepTransactionId(Step.MAKE_ESPRESSO, transactionId)).orElseThrow(() -> new IllegalStateException("Unable to find " + Step.MAKE_ESPRESSO + " details for " + transactionId));
        AuditLog steamMilkAuditLog = repository.findById(new StepTransactionId(Step.STEAM_MILK, transactionId)).orElseThrow(() -> new IllegalStateException("Unable to find " + Step.STEAM_MILK + " details for " + transactionId));

        OrderTimeline orderTimeline = new OrderTimeline();
        orderTimeline.setGrindCoffeeAuditLog(grindCoffeeAuditLog);
        orderTimeline.setMakeEspressoAuditLog(makeEspressoAuditLog);
        orderTimeline.setSteamMilkAuditLog(steamMilkAuditLog);

        return orderTimeline;
    }
}