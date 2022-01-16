package com.simulation.shop.machine;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.simulation.shop.model.Coffee;
import com.simulation.shop.util.CoffeeUtility;

public class EspressoMachine {

	private String machineName;
	private Lock espressoLock = new ReentrantLock();

	public EspressoMachine(String machineName) {
		this.machineName = machineName;
	}

	public Coffee concentrate(StringBuffer metadata) {
		espressoLock.lock();
		Coffee coffee = null;
		try {
			Thread.sleep(CoffeeUtility.buildStepTimeWithJitter());
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

}
