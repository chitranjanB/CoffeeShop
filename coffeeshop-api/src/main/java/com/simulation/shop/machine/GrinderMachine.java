package com.simulation.shop.machine;

import com.simulation.shop.OutOfIngredientsException;
import com.simulation.shop.model.Grounds;
import com.simulation.shop.util.CoffeeUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GrinderMachine {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrinderMachine.class);

    @Autowired
    private CoffeeUtility utility;

    private String machineName;
    private Lock grinderLock = new ReentrantLock();

    public GrinderMachine(String machineName) {
        this.machineName = machineName;
    }

    public Grounds grind(String customerId) {
        grinderLock.lock();
        Grounds grounds = null;
        try {
            Thread.sleep(utility.buildStepTimeWithJitter());
            String beans = fetchBeanFromInventory();
            if (beans == null) {
                throw new OutOfIngredientsException("Beans are not in stock - " + customerId);
            }
            grounds = new Grounds(beans);
        } catch (InterruptedException e) {
            LOGGER.error("Error while grinding beans " + e.getLocalizedMessage(), e);
        } finally {
            grinderLock.unlock();
        }
        return grounds;
    }

    public Lock getGrinderLock() {
        return grinderLock;
    }

    public int getMachineId() {
        return utility.fetchMachineId(this.machineName);
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

    public boolean isBeanInventoryEmpty() {
        return false;
    }

    private String fetchBeanFromInventory() {
        int machineId = utility.fetchMachineId(this.machineName);
        return "--------------------";
    }
}