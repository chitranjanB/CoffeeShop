package com.simulation.shop.machine;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.simulation.shop.model.Grounds;
import com.simulation.shop.product.Product;
import com.simulation.shop.util.CoffeeUtility;

public class GrinderMachine implements IMachine {

	private Lock grinderLock = new ReentrantLock();

	public Grounds grind() {
		grinderLock.lock();
		Grounds grounds = null;
		try {
			Thread.sleep(CoffeeUtility.buildStepTimeWithJitter());
			grounds = new Grounds();
		} catch (InterruptedException e) {
			System.err.println("Something went wrong " + e.getLocalizedMessage());
		} finally {
			grinderLock.unlock();
		}
		return grounds;
	}

	public Lock getGrinderLock() {
		return grinderLock;
	}

	@Override
	public Product start() {
		grinderLock.lock();
		Grounds grounds = null;
		try {
			Thread.sleep(CoffeeUtility.buildStepTimeWithJitter());
			grounds = new Grounds();
		} catch (InterruptedException e) {
			System.err.println("Something went wrong " + e.getLocalizedMessage());
		} finally {
			grinderLock.unlock();
		}
		return grounds;
	}

	@Override
	public Lock getMachineLock() {
		return grinderLock;
	}

}
