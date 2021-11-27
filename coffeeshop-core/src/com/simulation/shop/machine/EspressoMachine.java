package com.simulation.shop.machine;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.simulation.shop.model.Coffee;
import com.simulation.shop.product.Product;
import com.simulation.shop.util.CoffeeUtility;

public class EspressoMachine implements IMachine {

	private Lock espressoLock = new ReentrantLock();

	public Coffee concentrate() {
		espressoLock.lock();
		Coffee coffee = null;
		try {
			Thread.sleep(CoffeeUtility.buildStepTimeWithJitter());
			coffee = new Coffee();
		} catch (InterruptedException e) {
			System.err.println("Something went wrong " + e.getLocalizedMessage());
		} finally {
			espressoLock.unlock();
		}
		return coffee;
	}

	public Lock getEspressoLock() {
		return espressoLock;
	}

	@Override
	public Product start() {

		espressoLock.lock();
		Coffee coffee = null;
		try {
			Thread.sleep(CoffeeUtility.buildStepTimeWithJitter());
			coffee = new Coffee();
		} catch (InterruptedException e) {
			System.err.println("Something went wrong " + e.getLocalizedMessage());
		} finally {
			espressoLock.unlock();
		}
		return coffee;

	}

	@Override
	public Lock getMachineLock() {
		return espressoLock;
	}
}
