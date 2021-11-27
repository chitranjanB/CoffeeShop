package com.simulation.shop.machine;

import java.util.concurrent.locks.Lock;

import com.simulation.shop.product.Product;

public interface IMachine {

	public Product start();
	
	public Lock getMachineLock();
}
