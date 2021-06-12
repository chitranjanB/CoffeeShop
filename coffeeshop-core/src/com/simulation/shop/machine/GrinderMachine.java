package com.simulation.shop.machine;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.simulation.shop.config.CoffeeShopConstant;
import com.simulation.shop.model.Grounds;

public class GrinderMachine {

	private Lock grinderLock = new ReentrantLock();

	public Grounds grind() {
		grinderLock.lock();
		Grounds grounds = null;
		try {
			Thread.sleep(CoffeeShopConstant.STEP_PROCESSING_TIME);
			grounds = new Grounds();
		} catch (InterruptedException e) {
			System.err.println("Something went wrong " + e.getLocalizedMessage());
		} finally {
			grinderLock.unlock();
		}
		return grounds;
	}

}
