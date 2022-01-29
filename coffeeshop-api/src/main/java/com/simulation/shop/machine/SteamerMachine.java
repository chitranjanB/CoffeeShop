package com.simulation.shop.machine;

import com.simulation.shop.OutOfIngredientsException;
import com.simulation.shop.config.Constants;
import com.simulation.shop.model.SteamedMilk;
import com.simulation.shop.util.CoffeeUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SteamerMachine {

    private static final Logger LOGGER = LoggerFactory.getLogger(SteamerMachine.class);

    private String machineName;
    private Lock steamerLock = new ReentrantLock();

    @Autowired
    private CoffeeUtility utility;

    public SteamerMachine(String machineName) {
        this.machineName = machineName;
    }

    public SteamedMilk steam(StringBuffer metadata) {
        steamerLock.lock();
        SteamedMilk steamedMilk = null;
        try {
            Thread.sleep(utility.buildStepTimeWithJitter());
            String raw_milk = fetchMilkFromInventory();

            if (raw_milk == null) {
                throw new OutOfIngredientsException("Milk is not in stock - " + metadata);
            }

            steamedMilk = new SteamedMilk(raw_milk);
        } catch (InterruptedException e) {
            LOGGER.error("Error while steaming milk for " + metadata + e.getLocalizedMessage(), e);
        } finally {
            steamerLock.unlock();
        }
        return steamedMilk;
    }

    public Lock getSteamerLock() {
        return steamerLock;
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

    public boolean isMilkInventoryEmpty() {
        return false;
    }

    private String fetchMilkFromInventory() {
        int machineId = utility.fetchMachineId(this.machineName);
        return "~~~~~~~~~~~~~~~~";
    }
}