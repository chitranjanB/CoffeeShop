package com.simulation.shop.machine;

import com.simulation.shop.OutOfIngredientsException;
import com.simulation.shop.config.Step;
import com.simulation.shop.entity.StepTransactionId;
import com.simulation.shop.model.SteamedMilk;
import com.simulation.shop.util.CoffeeUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SteamerMachine {

    private static final Logger LOGGER = LoggerFactory.getLogger(SteamerMachine.class);

    @Autowired
    private CoffeeUtility utility;

    private String machineName;
    private Lock steamerLock = new ReentrantLock();

    public SteamerMachine(String machineName) {
        this.machineName = machineName;
    }

    public SteamedMilk steam(String transactionId, String customerId, String milk) {
        Instant start = Instant.now();
        SteamedMilk steamedMilk = null;
        try {
            Thread.sleep(utility.buildStepTimeWithJitter());
            if (milk == null) {
                throw new OutOfIngredientsException("Milk is not in stock - " + customerId);
            }
            steamedMilk = new SteamedMilk(milk);
        } catch (InterruptedException e) {
            LOGGER.error("Error while steaming milk for " + e.getLocalizedMessage(), e);
        }

        StepTransactionId stepTransactionId = new StepTransactionId(Step.STEAM_MILK, transactionId);
        utility.auditLog(stepTransactionId, getMachineName(), customerId, start);

        return steamedMilk;
    }

    public Lock getSteamerLock() {
        return steamerLock;
    }

    public String getMachineName() {
        return this.machineName;
    }

    public CoffeeUtility getUtility() {
        return utility;
    }

    public void setUtility(CoffeeUtility utility) {
        this.utility = utility;
    }

}