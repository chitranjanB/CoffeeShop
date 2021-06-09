package com.simulation.shop.machine;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.simulation.shop.model.Grounds;

public class GrinderMachine {

	private Lock grinderLock = new ReentrantLock();

	public Lock getGrinderLock() {
		return grinderLock;
	}

	public Grounds grind() {

		Grounds grounds = null;
		try {
			Thread.sleep(250);
			grounds = new Grounds();
		} catch (InterruptedException e) {
			System.err.println("Something went wrong " + e.getLocalizedMessage());
		}
		return grounds;
	}

}
