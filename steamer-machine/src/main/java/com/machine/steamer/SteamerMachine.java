package com.machine.steamer;

import com.coffee.shared.exception.OutOfIngredientsException;
import com.coffee.shared.model.SteamedMilk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SteamerMachine {

    private static final Logger LOGGER = LoggerFactory.getLogger(SteamerMachine.class);

    private String machineName;
    private Lock steamerLock = new ReentrantLock();

    public SteamerMachine(String machineName) {
        this.machineName = machineName;
    }

    public SteamedMilk steam(String transactionId, String customerId, String milk) {
        Instant start = Instant.now();
        SteamedMilk steamedMilk = null;
        try {
            //TODO fix this in all machines, run using property file
            Thread.sleep(1000);
            if (milk == null) {
                throw new OutOfIngredientsException("Milk is not in stock - " + customerId);
            }
            steamedMilk = new SteamedMilk(milk);
        } catch (InterruptedException e) {
            LOGGER.error("Error while steaming milk for " + e.getLocalizedMessage(), e);
        }

//        StepTransactionId stepTransactionId = new StepTransactionId(Step.STEAM_MILK, transactionId);
//        utility.auditLog(stepTransactionId, getMachineName(), customerId, start);
        steamedMilk.setMachineName(machineName);
        steamedMilk.setCustomerId(customerId);
        steamedMilk.setStart(start);
        steamedMilk.setEnd(Instant.now());

        return steamedMilk;
    }

    public Lock getSteamerLock() {
        return steamerLock;
    }

    public String getMachineName() {
        return this.machineName;
    }

}