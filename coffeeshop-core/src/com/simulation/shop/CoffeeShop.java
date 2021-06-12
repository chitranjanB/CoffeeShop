package com.simulation.shop;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.simulation.shop.config.CoffeeShopConstant;
import com.simulation.shop.task.GrindTask;
import com.simulation.shop.util.CoffeeUtility;

public class CoffeeShop {

	public static void main(String[] args) throws Exception {
		CoffeeShop shop = new CoffeeShop();
		int customers = args.length > 0 ? Integer.parseInt(args[0]) : CoffeeShopConstant.CUSTOMERS;
		shop.start(customers);
	}

	public void start(int customers) throws Exception {
		// CountDownLatch to wait till all threads have completed executions
		// Just for printing stats
		CountDownLatch latch = new CountDownLatch(4 * customers);
		ExecutorService grindExecutor = Executors.newSingleThreadExecutor();
		ExecutorService espressoExecutor = Executors.newSingleThreadExecutor();
		ExecutorService steamExecutor = Executors.newSingleThreadExecutor();

		for (int i = 0; i < customers; i++) {
			GrindTask task = new GrindTask(espressoExecutor, steamExecutor, latch);
			grindExecutor.submit(task);
			latch.countDown();
		}

		latch.await();

		grindExecutor.shutdown();
		espressoExecutor.shutdown();
		steamExecutor.shutdown();

		CoffeeUtility.stats();
		System.out.println("---------------COFFEE SHOP CLOSED-----------------------");
	}

}
