package com.simulation.shop.utility;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.simulation.shop.util.CoffeeUtility;

public class CoffeeUtilityTest {

	@Test
	public void test_fetchMachineId_customers_morethan_machineslimit() {
		assertEquals(2, CoffeeUtility.fetchMachineId());
	}

}
