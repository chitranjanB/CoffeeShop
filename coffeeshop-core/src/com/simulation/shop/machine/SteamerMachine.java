package com.simulation.shop.machine;

import com.simulation.shop.model.Milk;

public class SteamerMachine {

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
