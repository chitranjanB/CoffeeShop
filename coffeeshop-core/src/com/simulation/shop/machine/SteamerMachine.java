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

	private Lock steamerLock = new ReentrantLock();

	public SteamedMilk steam() {
		steamerLock.lock();
		SteamedMilk steamedMilk = null;
		try {
			Thread.sleep(CoffeeUtility.buildStepTimeWithJitter());
			String raw_milk = fetchMilkFromInventory();
			
			if (raw_milk == null) {
				throw new OutOfIngredientsException("Milk is not in stock");
			}
			
			steamedMilk = new SteamedMilk(raw_milk);
		} catch (InterruptedException e) {
			System.err.println("Something went wrong " + e.getLocalizedMessage());
		} finally {
			steamerLock.unlock();
		}
		return steamedMilk;
	}
	
	private String fetchMilkFromInventory() {
		File milkInventory = new File(Config.MILK_INVENTORY);
		File tempFile = new File("resources/milk-temp.txt");
		
		String beans = getMilkFromInventory(milkInventory, tempFile);
		updateMilkInventory(milkInventory, tempFile);
		return beans;
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


	
}
