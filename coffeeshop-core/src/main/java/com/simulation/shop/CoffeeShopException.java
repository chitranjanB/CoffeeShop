package com.simulation.shop;

public class CoffeeShopException extends RuntimeException {
	private static final long serialVersionUID = 7387215987139524023L;

	public CoffeeShopException() {
		super();
	}

	public CoffeeShopException(String message) {
		super(message);
	}
}
