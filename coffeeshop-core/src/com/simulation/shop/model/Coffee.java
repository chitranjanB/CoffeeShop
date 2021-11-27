package com.simulation.shop.model;

public class Coffee extends Product {
	private int tempInC;
	private int coffeeVolumeInML;

	public Coffee() {
		this.tempInC = 120;
		this.coffeeVolumeInML = 20;
	}

	@Override
	public String toString() {
		return "Coffee [tempInC=" + tempInC + ", coffeeVolumeInML=" + coffeeVolumeInML + "]";
	}

}
