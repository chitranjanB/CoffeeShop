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
import com.simulation.shop.model.Grounds;
import com.simulation.shop.util.CoffeeUtility;

public class GrinderMachine {

	private Lock grinderLock = new ReentrantLock();

	public Grounds grind(int customerId) {
		grinderLock.lock();
		Grounds grounds = null;
		try {
			Thread.sleep(CoffeeUtility.buildStepTimeWithJitter());
			String beans = fetchBeanFromInventory(customerId);
			if (beans == null) {
				throw new OutOfIngredientsException("Beans are not in stock");
			}
			
			grounds = new Grounds(beans);
		} catch (InterruptedException e) {
			System.err.println("Something went wrong - " + e.getLocalizedMessage());
		} finally {
			grinderLock.unlock();
		}
		return grounds;
	}
	
	public Lock getGrinderLock() {
		return grinderLock;
	}
	
	private String fetchBeanFromInventory(int customerId) {
		int machineId = CoffeeUtility.fetchMachineId();
		File beanInventory = new File(String.format(Config.BEANS_INVENTORY, machineId));
		File tempFile = new File(String.format(Config.BEANS_INVENTORY, "-temp" + machineId));
		
		String beans = getBeansFromInventory(beanInventory, tempFile);
		updateBeanInventory(beanInventory, tempFile);
		return beans;
	}

	private String getBeansFromInventory(File beanInventory, File tempFile) {
		String beans = null;

		try (BufferedReader br = new BufferedReader(new FileReader(beanInventory));
				BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));) {
			
			beans = br.readLine();

			int temp;
			while (beans != null && (temp = br.read()) != -1) {
				bw.write((char) temp);
			}
		}
		catch (Exception e) {
			System.err.println("Something went wrong " + e.getLocalizedMessage());
		}
		return beans;
	}

	
	private boolean updateBeanInventory(File beanInventory, File tempFile)  {
		boolean isUpdated = true;
		
		try (BufferedWriter bw1 = new BufferedWriter(new FileWriter(beanInventory));
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
