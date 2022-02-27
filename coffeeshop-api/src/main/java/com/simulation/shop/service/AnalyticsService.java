package com.simulation.shop.service;

import com.coffee.shared.entity.AuditLog;
import com.coffee.shared.model.Benchmark;
import com.coffee.shared.model.Data;
import com.coffee.shared.model.Step;
import com.simulation.shop.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AnalyticsService {

    @Autowired
    private AuditLogRepository repository;

    public List<Benchmark> getData() {
        //TODO take 10 recent orders only
        Iterable<AuditLog> all = repository.findAll();

        List<Benchmark> benchmarks = new ArrayList<>();

        List<Data> grindList = new ArrayList<>();
        List<Data> espressoList = new ArrayList<>();
        List<Data> steamerList = new ArrayList<>();
        for (AuditLog auditLog : all) {

            String customerId = auditLog.getCustomerId();
            Date startDate = auditLog.getTimeStarted();
            Date endDate = auditLog.getTimeEnded();
            Step step = auditLog.getStepTransactionId().getStep();

            switch(step) {
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

}
