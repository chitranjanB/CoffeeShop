package com.machine.espresso;

import com.coffee.shared.entity.StepTransactionId;
import com.coffee.shared.model.Coffee;
import com.coffee.shared.model.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EspressoMachine {
    private static final Logger LOGGER = LoggerFactory.getLogger(EspressoMachine.class);

    private String machineName;
    private Lock espressoLock = new ReentrantLock();

    public EspressoMachine(String machineName) {
        this.machineName = machineName;
    }

    public Coffee concentrate(String transactionId, String customerId) {
        Instant start = Instant.now();
        Coffee coffee = null;
        try {
            //TODO fix this
            Thread.sleep(1000);
            coffee = new Coffee();
        } catch (InterruptedException e) {
            LOGGER.error("Error while making espresso " + e.getLocalizedMessage(), e);
        } finally {
            espressoLock.unlock();
        }

        coffee.setMachineName(machineName);
        coffee.setCustomerId(customerId);
        coffee.setStart(start);
        coffee.setEnd(Instant.now());

        return coffee;
    }

    public String getMachineName() {
        return this.machineName;
    }

    public Lock getEspressoLock() {
        return espressoLock;
    }

}