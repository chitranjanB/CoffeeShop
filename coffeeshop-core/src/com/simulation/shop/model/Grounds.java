package com.simulation.shop.model;

import com.simulation.shop.product.*;

public class Grounds extends Product {

	private int coffeeBeansInGms;

	public Grounds() {
		this.coffeeBeansInGms = 20;
	}

	@Override
	public String toString() {
		return "Grounds [coffeeBeansInGms=" + coffeeBeansInGms + "]";
	}

}
