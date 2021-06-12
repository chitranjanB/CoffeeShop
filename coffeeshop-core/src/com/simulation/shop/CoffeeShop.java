package com.simulation.shop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.simulation.shop.config.CoffeeShopConstant;
import com.simulation.shop.machine.EspressoMachine;
import com.simulation.shop.machine.GrinderMachine;
import com.simulation.shop.machine.SteamerMachine;
import com.simulation.shop.util.CoffeeUtility;

public class CoffeeShop {

	private GrinderMachine grinderMachine = new GrinderMachine();
	private EspressoMachine espressoMachine = new EspressoMachine();
	private SteamerMachine steamerMachine = new SteamerMachine();

	public static void main(String[] args) throws InterruptedException {
		CoffeeShop shop = new CoffeeShop();
		int customers = args.length > 0 ? Integer.parseInt(args[0]) : CoffeeShopConstant.CUSTOMERS;
		shop.start(customers);
	}

	public void start(int customers) throws InterruptedException {

		ExecutorService fixedPool = Executors.newFixedThreadPool(customers);

		for (int i = 0; i < customers; i++) {
			Runnable task = new BrewTask(grinderMachine, espressoMachine, steamerMachine);
			fixedPool.submit(task);
		}
		fixedPool.shutdown();

		while (!fixedPool.isTerminated()) {
			// wait on main thread, until all threads are done
		}

		CoffeeUtility.stats();
		System.out.println("---------------COFFEE SHOP CLOSED-----------------------");
	}

}
