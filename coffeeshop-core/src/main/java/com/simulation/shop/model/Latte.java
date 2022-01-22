package com.simulation.shop.model;

public class Latte {

	private Coffee coffee;
	private SteamedMilk milk;

	public Latte(Coffee coffee, SteamedMilk milk) {
		this.coffee = coffee;
		this.milk = milk;
	}

	@Override
	public String toString() {
		return "Latte [coffee=" + coffee + ", milk=" + milk + "]";
	}

}