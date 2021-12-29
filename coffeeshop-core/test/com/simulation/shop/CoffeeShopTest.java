package com.simulation.shop;

import java.util.concurrent.ExecutionException;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.simulation.shop.util.CoffeeUtility;

/**
 *
 * Just a timed test, without exceptions
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CoffeeShopTest {

	@Test
	public void test_01_thread() throws Exception {
		int limit = 1;
		CoffeeUtility.loadupBeans(limit,limit);
		CoffeeUtility.loadupMilk(limit,limit);
		CoffeeShop shop = new CoffeeShop();
		int customers = 1;
		shop.start(customers);
	}

	@Test
	public void test_04_threads() throws Exception {
		int limit = 4;
		CoffeeUtility.loadupBeans(limit,limit);
		CoffeeUtility.loadupMilk(limit,limit);
		CoffeeShop shop = new CoffeeShop();
		int customers = 4;
		shop.start(customers);
	}

	@Test
	public void test_10_threads() throws Exception {
		int limit = 10;
		CoffeeUtility.loadupBeans(limit,limit);
		CoffeeUtility.loadupMilk(limit,limit);
		CoffeeShop shop = new CoffeeShop();
		int customers = 10;
		shop.start(customers);
	}

	@Test
	public void test_20_threads() throws Exception {
		int limit = 20;
		CoffeeUtility.loadupBeans(limit,limit);
		CoffeeUtility.loadupMilk(limit,limit);
		CoffeeShop shop = new CoffeeShop();
		int customers = 20;
		shop.start(customers);
	}

	@Test
	public void test_30_threads() throws Exception {
		int limit = 30;
		CoffeeUtility.loadupBeans(limit,limit);
		CoffeeUtility.loadupMilk(limit,limit);
		CoffeeShop shop = new CoffeeShop();
		int customers = 30;
		shop.start(customers);
	}
}
