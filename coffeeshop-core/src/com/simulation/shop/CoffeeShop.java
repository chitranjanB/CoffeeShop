package com.simulation.shop;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.simulation.shop.config.CoffeeShopConstant;
import com.simulation.shop.machine.EspressoMachine;
import com.simulation.shop.machine.GrinderMachine;
import com.simulation.shop.machine.SteamerMachine;
import com.simulation.shop.util.CoffeeUtility;

public class CoffeeShop {

	private BlockingQueue<GrinderMachine> grinderMachines;
	private BlockingQueue<EspressoMachine> espressoMachines;
	private BlockingQueue<SteamerMachine> steamerMachines;

	public CoffeeShop() {
		grinderMachines = new ArrayBlockingQueue<GrinderMachine>(CoffeeShopConstant.MULTI_SHARED_INSTANCE);
		espressoMachines = new ArrayBlockingQueue<EspressoMachine>(CoffeeShopConstant.MULTI_SHARED_INSTANCE);
		steamerMachines = new ArrayBlockingQueue<SteamerMachine>(CoffeeShopConstant.MULTI_SHARED_INSTANCE);

		for (int i = 0; i < CoffeeShopConstant.MULTI_SHARED_INSTANCE; i++) {
			grinderMachines.add(new GrinderMachine());
			espressoMachines.add(new EspressoMachine());
			steamerMachines.add(new SteamerMachine());
		}

	}

	public static void main(String[] args) throws InterruptedException {
		CoffeeShop shop = new CoffeeShop();
		int customers = args.length > 0 ? Integer.parseInt(args[0]) : CoffeeShopConstant.CUSTOMERS;
		shop.start(customers);
	}

	public void start(int customers) throws InterruptedException {

		ExecutorService fixedPool = Executors.newFixedThreadPool(customers);

		for (int i = 0; i < customers; i++) {
			Runnable task = new BrewTask(grinderMachines, espressoMachines, steamerMachines);
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
