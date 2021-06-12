package com.simulation.shop.machine;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.simulation.shop.config.CoffeeShopConstant;
import com.simulation.shop.model.Milk;

public class SteamerMachine {

	private Lock steamerLock = new ReentrantLock();

	public Milk steam() {
		steamerLock.lock();
		Milk milk = null;
		try {
			Thread.sleep(CoffeeShopConstant.STEP_PROCESSING_TIME);
			milk = new Milk();
		} catch (InterruptedException e) {
			System.err.println("Something went wrong " + e.getLocalizedMessage());
		} finally {
			steamerLock.unlock();
		}
		return milk;
	}

}
