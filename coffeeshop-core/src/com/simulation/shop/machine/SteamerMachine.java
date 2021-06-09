package com.simulation.shop.machine;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.simulation.shop.model.Milk;

public class SteamerMachine {

	private Lock steamerLock = new ReentrantLock();

	public Lock getSteamerLock() {
		return steamerLock;
	}

	public Milk steam() {
		Milk milk = null;
		try {
			Thread.sleep(250);
			milk = new Milk();
		} catch (InterruptedException e) {
			System.err.println("Something went wrong " + e.getLocalizedMessage());
		}
		return milk;
	}

}
