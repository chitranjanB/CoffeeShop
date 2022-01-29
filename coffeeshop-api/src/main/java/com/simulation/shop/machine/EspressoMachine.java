package com.simulation.shop.machine;

import com.simulation.shop.config.Step;
import com.simulation.shop.entity.StepTransactionId;
import com.simulation.shop.model.Coffee;
import com.simulation.shop.util.CoffeeUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EspressoMachine {

    private static final Logger LOGGER = LoggerFactory.getLogger(EspressoMachine.class);

    @Autowired
    private CoffeeUtility utility;

    private String machineName;
    private Lock espressoLock = new ReentrantLock();

    public EspressoMachine(String machineName) {
        this.machineName = machineName;
    }

    public Coffee concentrate(String transactionId, String customerId) {
        Instant start = Instant.now();
        Coffee coffee = null;
        try {
            Thread.sleep(utility.buildStepTimeWithJitter());
            coffee = new Coffee();
        } catch (InterruptedException e) {
            LOGGER.error("Error while making espresso " + e.getLocalizedMessage(), e);
        } finally {
            espressoLock.unlock();
        }
        StepTransactionId stepTransactionId = new StepTransactionId(Step.MAKE_ESPRESSO, transactionId);
        utility.auditLog(stepTransactionId, getMachineName(), customerId, start);
        return coffee;
    }

    public String getMachineName() {
        return this.machineName;
    }

    public Lock getEspressoLock() {
        return espressoLock;
    }

    public CoffeeUtility getUtility() {
        return utility;
    }

    public void setUtility(CoffeeUtility utility) {
        this.utility = utility;
    }

}