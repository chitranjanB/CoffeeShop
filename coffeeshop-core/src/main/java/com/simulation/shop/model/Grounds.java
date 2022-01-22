package com.simulation.shop.model;

public class Grounds {

	private String coffeeBeans;
	private int coffeeBeansInGms;

	public Grounds(String beans) {
		this.coffeeBeans = beans;
		this.coffeeBeansInGms = beans.length();
	}

	@Override
	public String toString() {
		return "Grounds [coffeeBeans=" + coffeeBeans + ", coffeeBeansInGms=" + coffeeBeansInGms + "]";
	}
}