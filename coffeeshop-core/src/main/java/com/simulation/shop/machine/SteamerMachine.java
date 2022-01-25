package com.simulation.shop.machine;

import com.simulation.shop.OutOfIngredientsException;
import com.simulation.shop.config.Constants;
import com.simulation.shop.model.SteamedMilk;
import com.simulation.shop.util.CoffeeUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
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

    public boolean isMilkInventoryEmpty() {
        File file = new File(String.format(Constants.MILK_INVENTORY, getMachineId()));
        return file.length() == 0;
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

    private String fetchMilkFromInventory() {
        int machineId = utility.fetchMachineId(this.machineName);
        File milkInventory = new File(String.format(Constants.MILK_INVENTORY, machineId));
        File tempFile = new File(String.format(Constants.MILK_INVENTORY, "-temp" + machineId));

        String milk = getMilkFromInventory(milkInventory, tempFile);
        updateMilkInventory(milkInventory, tempFile);
        return milk;
    }

    private String getMilkFromInventory(File milkInventory, File tempFile) {
        String milk = null;

        try (BufferedReader br = new BufferedReader(new FileReader(milkInventory));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));) {
            milk = br.readLine();

            int temp;
            while (milk != null && (temp = br.read()) != -1) {
                bw.write((char) temp);
            }
        } catch (Exception e) {
            LOGGER.error("Error while getting milk from inventory " + e.getLocalizedMessage(), e);
        }
        return milk;
    }


    private boolean updateMilkInventory(File milkInventory, File tempFile) {
        boolean isUpdated = true;

        try (BufferedWriter bw1 = new BufferedWriter(new FileWriter(milkInventory));
             BufferedReader br1 = new BufferedReader(new FileReader(tempFile));) {
            int original;

            while ((original = br1.read()) != -1) {
                bw1.write((char) original);
            }
            tempFile.deleteOnExit();
        } catch (Exception e) {
            isUpdated = false;
            LOGGER.error("Error while updating milk inventory " + e.getLocalizedMessage(), e);
        }
        return isUpdated;
    }

    public Lock getSteamerLock() {
        return steamerLock;
    }

}