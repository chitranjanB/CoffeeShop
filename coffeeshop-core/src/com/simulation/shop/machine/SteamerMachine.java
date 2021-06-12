package com.simulation.shop.machine;

import com.simulation.shop.config.CoffeeShopConstant;
import com.simulation.shop.model.Milk;

public class SteamerMachine {

	public Milk steam() {
		Milk milk = null;
		try {
			Thread.sleep(CoffeeShopConstant.STEP_PROCESSING_TIME);
			milk = new Milk();
		} catch (InterruptedException e) {
			System.err.println("Something went wrong " + e.getLocalizedMessage());
		}
		return milk;
	}

}
