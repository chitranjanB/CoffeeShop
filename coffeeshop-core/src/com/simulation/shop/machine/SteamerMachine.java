package com.simulation.shop.machine;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.simulation.shop.model.Milk;
import com.simulation.shop.model.Product;
import com.simulation.shop.util.CoffeeUtility;

public class SteamerMachine implements IMachine {

	private Lock steamerLock = new ReentrantLock();

	@Override
	public Product start() {
		steamerLock.lock();
		Milk milk = null;
		try {
			Thread.sleep(CoffeeUtility.buildStepTimeWithJitter());
			milk = new Milk();
		} catch (InterruptedException e) {
			System.err.println("Something went wrong " + e.getLocalizedMessage());
		} finally {
			steamerLock.unlock();
		}
		return milk;
	}

	@Override
	public Lock getMachineLock() {
		return steamerLock;
	}
}
