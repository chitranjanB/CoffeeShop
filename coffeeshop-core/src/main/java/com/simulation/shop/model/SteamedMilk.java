package com.simulation.shop.model;

public class SteamedMilk {
	private String rawMilk;
	private int tempInC;
	private int milkVolumeInML;

	public SteamedMilk(String rawMilk) {
		this.rawMilk = rawMilk;
		this.tempInC = 130;
		this.milkVolumeInML = 150;
	}

	@Override
	public String toString() {
		return "SteamedMilk [rawMilk=" + rawMilk + ", tempInC=" + tempInC + ", milkVolumeInML=" + milkVolumeInML + "]";
	}

}