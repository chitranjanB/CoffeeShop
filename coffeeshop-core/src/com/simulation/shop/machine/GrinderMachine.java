package com.simulation.shop.machine;

import com.simulation.shop.config.CoffeeShopConstant;
import com.simulation.shop.model.Grounds;

public class GrinderMachine {

	public Grounds grind() {
		Grounds grounds = null;
		try {
			Thread.sleep(CoffeeShopConstant.STEP_PROCESSING_TIME);
			grounds = new Grounds();
		} catch (InterruptedException e) {
			System.err.println("Something went wrong " + e.getLocalizedMessage());
		}
		return grounds;
	}

}
