package com.simulation.shop.machine;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.simulation.shop.model.Coffee;
import com.simulation.shop.util.CoffeeUtility;

public class EspressoMachine {

	private Lock espressoLock = new ReentrantLock();

	public Coffee concentrate(String metadata) {
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
	
	public Lock getEspressoLock() {
		return espressoLock;
	}

}
