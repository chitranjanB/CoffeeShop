package com.simulation.shop;

import java.util.concurrent.ExecutionException;

import com.simulation.shop.config.Config;
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
		int customers = 10;
		CoffeeUtility.loadupBeans(CoffeeUtility.fetchRequiredMachines(customers), Config.BEANS_INVENTORY_LIMIT);
		CoffeeUtility.loadupMilk(CoffeeUtility.fetchRequiredMachines(customers), Config.MILK_INVENTORY_LIMIT);
		CoffeeShop shop = new CoffeeShop();
		shop.start(customers);
	}
}
