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
	public void test_10_threads() throws Exception {
		int limit = 10;
		CoffeeUtility.loadupBeans(limit,limit);
		CoffeeUtility.loadupMilk(limit,limit);
		CoffeeShop shop = new CoffeeShop();
		int customers = 10;
		shop.start(customers);
	}
}
