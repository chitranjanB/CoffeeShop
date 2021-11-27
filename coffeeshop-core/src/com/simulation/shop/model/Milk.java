package com.simulation.shop.model;

import com.simulation.shop.product.Product;

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
