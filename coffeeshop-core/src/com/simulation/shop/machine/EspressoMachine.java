package com.simulation.shop.machine;

import com.simulation.shop.config.CoffeeShopConstant;
import com.simulation.shop.model.Coffee;
import com.simulation.shop.model.Grounds;

public class EspressoMachine {

	public Coffee concentrate(Grounds grounds) {
		Coffee coffee = null;
		try {
			Thread.sleep(CoffeeShopConstant.STEP_PROCESSING_TIME);
			coffee = new Coffee();
		} catch (InterruptedException e) {
			System.err.println("Something went wrong " + e.getLocalizedMessage());
		}
		return coffee;
	}

}
