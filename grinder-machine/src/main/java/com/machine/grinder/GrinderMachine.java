package com.machine.grinder;

import com.coffee.shared.entity.StepTransactionId;
import com.coffee.shared.exception.OutOfIngredientsException;
import com.coffee.shared.model.Grounds;
import com.coffee.shared.model.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GrinderMachine {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrinderMachine.class);

    private String machineName;
    private Lock grinderLock = new ReentrantLock();

    public GrinderMachine(String machineName) {
        this.machineName = machineName;
    }

    public Grounds grind(String transactionId, String customerId, String beans) {
        Instant start = Instant.now();
        Grounds grounds = null;
        try {
            Thread.sleep(1000);
            if (beans == null) {
                throw new OutOfIngredientsException("Beans are not in stock - " + customerId);
            }
            grounds = new Grounds(beans);
        } catch (InterruptedException e) {
            LOGGER.error("Error while grinding beans " + e.getLocalizedMessage(), e);
        }

        StepTransactionId stepTransactionId = new StepTransactionId(Step.GRIND_COFFEE, transactionId);
        grounds.setCustomerId(customerId);
        grounds.setMachineName(machineName);
        grounds.setStart(start);
        grounds.setEnd(Instant.now());

        return grounds;
    }

    public Lock getGrinderLock() {
        return grinderLock;
    }

    public String getMachineName() {
        return this.machineName;
    }

}