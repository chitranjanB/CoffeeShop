package com.simulation.shop.machine;

import com.simulation.shop.model.Coffee;
import com.simulation.shop.util.CoffeeUtility;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EspressoMachine {

	@Autowired
	private CoffeeUtility utility;

	private String machineName;
	private Lock espressoLock = new ReentrantLock();

	public EspressoMachine(String machineName) {
		this.machineName = machineName;
	}

	public Coffee concentrate(StringBuffer metadata) {
		espressoLock.lock();
		Coffee coffee = null;
		try {
			Thread.sleep(utility.buildStepTimeWithJitter());
			coffee = new Coffee();
		} catch (InterruptedException e) {
			System.err.println("Something went wrong - " + metadata + e.getLocalizedMessage());
		} finally {
			espressoLock.unlock();
		}
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