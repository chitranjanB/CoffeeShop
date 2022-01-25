package com.simulation.shop.machine;

import com.simulation.shop.OutOfIngredientsException;
import com.simulation.shop.config.Constants;
import com.simulation.shop.model.Grounds;
import com.simulation.shop.util.CoffeeUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
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

	public Grounds grind(StringBuffer metadata) {
		grinderLock.lock();
		Grounds grounds = null;
		try {
			Thread.sleep(utility.buildStepTimeWithJitter());
			String beans = fetchBeanFromInventory();
			if (beans == null) {
				throw new OutOfIngredientsException("Beans are not in stock - "+metadata);
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
		File file = new File(String.format(Constants.BEANS_INVENTORY, getMachineId()));
		return file.length() == 0;
	}

	private String fetchBeanFromInventory() {
		int machineId = utility.fetchMachineId(this.machineName);
		File beanInventory = new File(String.format(Constants.BEANS_INVENTORY, machineId));
		File tempFile = new File(String.format(Constants.BEANS_INVENTORY, "-temp" + machineId));

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
			LOGGER.error("Error while fetching beans from bean inventory " + e.getLocalizedMessage(), e);
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
			LOGGER.error("Error while updating bean inventory " + e.getLocalizedMessage(), e);
		}
		return isUpdated;
	}

}