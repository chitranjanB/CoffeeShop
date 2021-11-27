package com.simulation.shop.model;

public class Milk extends Product {
	private int tempInC;
	private int milkVolumeInML;

	public Milk() {
		this.tempInC = 130;
		this.milkVolumeInML = 150;
	}

	@Override
	public String toString() {
		return "Milk [tempInC=" + tempInC + ", milkVolumeInML=" + milkVolumeInML + "]";
	}

}
