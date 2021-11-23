package com.simulation.shop.config;

import java.time.LocalTime;

public class CoffeeShopRuntime {

	private static CoffeeShopRuntime coffeeRuntime = new CoffeeShopRuntime();

	private LocalTime shopOpenTimestamp;

	public static CoffeeShopRuntime getInstance() {
		if (coffeeRuntime != null) {
			return coffeeRuntime;
		}
		return new CoffeeShopRuntime();
	}

	public LocalTime getShopOpenTimestamp() {
		return shopOpenTimestamp;
	}

	public void setShopOpenTimestamp(LocalTime shopOpenTimestamp) {
		this.shopOpenTimestamp = shopOpenTimestamp;
	}

}
