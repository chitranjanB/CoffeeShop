package com.simulation.shop;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * Just a timed test, without exceptions
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CoffeeShopTest {

	@Test(timeout = 900)
	public void test_01_thread() throws Exception {
		CoffeeShop shop = new CoffeeShop();
		int customers = 1;
		shop.start(customers);
	}

	@Test(timeout = 2500)
	public void test_04_threads() throws Exception {
		CoffeeShop shop = new CoffeeShop();
		int customers = 4;
		shop.start(customers);
	}

	@Test(timeout = 5600)
	public void test_10_threads() throws Exception {
		CoffeeShop shop = new CoffeeShop();
		int customers = 10;
		shop.start(customers);
	}

	@Test(timeout = 10800)
	public void test_20_threads() throws Exception {
		CoffeeShop shop = new CoffeeShop();
		int customers = 20;
		shop.start(customers);
	}

	@Test(timeout = 15900)
	public void test_30_threads() throws Exception {
		CoffeeShop shop = new CoffeeShop();
		int customers = 30;
		shop.start(customers);
	}
}
