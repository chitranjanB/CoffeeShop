package com.simulation.shop.service;

import com.simulation.shop.OutOfIngredientsException;
import com.simulation.shop.machine.GrinderMachine;
import com.simulation.shop.model.Grounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class GrindingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GrindingService.class);

    @Autowired
    private List<GrinderMachine> grinderMachines;

    public Grounds grind(String transactionId) {
        Instant start = Instant.now();
        GrinderMachine machine = getAvailableGrinderMachine(grinderMachines);
        //TODO grind using multithreading later
        //TODO fetch orders from transactionId
        Grounds grounds = machine.grind(transactionId);
        //send machine name and customerid mapping to db
        Instant end = Instant.now();
        // TODO collect metrics of time taken
//        utility.collectApexMetric(utility.buildThreadMeta(metadata, Thread.currentThread().getName()),
//                Step.GRIND_COFFEE, start, end);

        LOGGER.debug("Grinding Coffee - Completed");
        return grounds;
    }

    private GrinderMachine getAvailableGrinderMachine(List<GrinderMachine> machines) {
        Optional<GrinderMachine> optional = machines.stream().filter(m -> {
            boolean isAvailable = false;
            try {
                isAvailable = !m.isBeanInventoryEmpty() && m.getGrinderLock().tryLock(100, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                LOGGER.error("Error while getting available grinding machine " + e.getLocalizedMessage(), e);
            }
            return isAvailable;
        }).findAny();
        return optional.orElseThrow(() -> new OutOfIngredientsException("Beans Inventory is empty"));
    }
}
