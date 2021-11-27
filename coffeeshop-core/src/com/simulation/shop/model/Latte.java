package com.simulation.shop.model;

import com.simulation.shop.product.Product;

public class Latte extends Product {

	private Coffee coffee;
	private Milk milk;

	public Latte(Coffee coffee, Milk milk) {
		this.coffee = coffee;
		this.milk = milk;
	}

	@Override
	public String toString() {
		return "Latte [coffee=" + coffee + ", milk=" + milk + "]";
	}

}
