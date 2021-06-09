package com.simulation.shop.machine;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.simulation.shop.model.Coffee;

public class EspressoMachine {

	private Lock espressoLock = new ReentrantLock();

	public Coffee concentrate() {
		espressoLock.lock();
		Coffee coffee = null;
		try {
			Thread.sleep(250);
			coffee = new Coffee();
		} catch (InterruptedException e) {
			System.err.println("Something went wrong " + e.getLocalizedMessage());
		} finally {
			espressoLock.unlock();
		}
		return coffee;
	}

}
