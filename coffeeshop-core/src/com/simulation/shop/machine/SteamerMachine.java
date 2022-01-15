package com.simulation.shop.machine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.simulation.shop.OutOfIngredientsException;
import com.simulation.shop.config.Config;
import com.simulation.shop.model.SteamedMilk;
import com.simulation.shop.util.CoffeeUtility;

public class SteamerMachine {

	private String machineName;
	private Lock steamerLock = new ReentrantLock();

	public SteamerMachine(String machineName) {
		this.machineName = machineName;
	}

	public int getMachineId(){
		return CoffeeUtility.fetchMachineId(this.machineName);
	}

	public boolean isMilkInventoryEmpty() {
		File file = new File(String.format(Config.MILK_INVENTORY, getMachineId()));
		return file.length() == 0;
	}

	public SteamedMilk steam(String metadata) {
		steamerLock.lock();
		SteamedMilk steamedMilk = null;
		try {
			Thread.sleep(CoffeeUtility.buildStepTimeWithJitter());
			String raw_milk = fetchMilkFromInventory();
			
			if (raw_milk == null) {
				throw new OutOfIngredientsException("Milk is not in stock - " + metadata);
			}
			
			steamedMilk = new SteamedMilk(raw_milk);
		} catch (InterruptedException e) {
			System.err.println("Something went wrong - " + metadata + e.getLocalizedMessage());
		} finally {
			steamerLock.unlock();
		}
		return steamedMilk;
	}
	
	private String fetchMilkFromInventory() {
		int machineId = CoffeeUtility.fetchMachineId(this.machineName);
		File milkInventory = new File(String.format(Config.MILK_INVENTORY, machineId));
		File tempFile = new File(String.format(Config.MILK_INVENTORY, "-temp" + machineId));

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
		}
		catch (Exception e) {
			System.err.println("Something went wrong " + e.getLocalizedMessage());
		}
		return milk;
	}

	
	private boolean updateMilkInventory(File milkInventory, File tempFile)  {
		boolean isUpdated = true;
		
		try (BufferedWriter bw1 = new BufferedWriter(new FileWriter(milkInventory));
				BufferedReader br1 = new BufferedReader(new FileReader(tempFile));) {
			int original;

			while ((original = br1.read()) != -1) {
				bw1.write((char) original);
			}
			tempFile.deleteOnExit();
		}
		catch (Exception e) {
			isUpdated = false;
			System.err.println("Something went wrong " + e.getLocalizedMessage());
		}
		return isUpdated;
	}

	public Lock getSteamerLock() {
		return steamerLock;
	}

}
