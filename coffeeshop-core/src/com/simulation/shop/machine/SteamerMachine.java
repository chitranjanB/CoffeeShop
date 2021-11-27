package com.simulation.shop.machine;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.simulation.shop.config.Config;
import com.simulation.shop.model.Milk;
import com.simulation.shop.product.Product;
import com.simulation.shop.util.CoffeeUtility;

public class SteamerMachine implements IMachine {

	private Lock steamerLock = new ReentrantLock();

	public Milk steam() {
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

	public Lock getSteamerLock() {
		return steamerLock;
	}

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
