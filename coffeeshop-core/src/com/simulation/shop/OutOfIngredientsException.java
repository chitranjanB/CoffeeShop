package com.simulation.shop;

public class OutOfIngredientsException extends CoffeeShopException {

	private static final long serialVersionUID = -3289006160941022432L;

	public OutOfIngredientsException() {
		super();
	}

	public OutOfIngredientsException(String message) {
		super(message);
	}
}
