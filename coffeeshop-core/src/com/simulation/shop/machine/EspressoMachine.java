package com.simulation.shop.machine;

import com.simulation.shop.model.Coffee;

public class EspressoMachine {

	public synchronized Coffee concentrate() {
		Coffee coffee = null;
		try {
			Thread.sleep(250);
			coffee = new Coffee();
		} catch (InterruptedException e) {
			System.err.println("Something went wrong " + e.getLocalizedMessage());
		}
		return coffee;
	}

}
