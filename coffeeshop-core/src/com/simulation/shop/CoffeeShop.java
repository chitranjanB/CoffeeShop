package com.simulation.shop;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.simulation.shop.machine.EspressoMachine;
import com.simulation.shop.machine.GrinderMachine;
import com.simulation.shop.machine.SteamerMachine;
import com.simulation.shop.util.CoffeeUtility;

public class CoffeeShop {

	private List<GrinderMachine> grinderMachines = Arrays.asList(new GrinderMachine(), new GrinderMachine());
	private List<EspressoMachine> espressoMachines = Arrays.asList(new EspressoMachine(), new EspressoMachine());
	private List<SteamerMachine> steamerMachines = Arrays.asList(new SteamerMachine(), new SteamerMachine());

	public static void main(String[] args) throws InterruptedException {
		Instant start = Instant.now();
		CoffeeShop shop = new CoffeeShop();

		int customers = args.length > 0 ? Integer.parseInt(args[0]) : 1;
		addShutdownHook();
		shop.start(customers);

		Instant finish = Instant.now();
		String timeElapsed = CoffeeUtility.timeElapsed(start, finish);
		System.out.println("Total time elapsed " + timeElapsed);
	}

	public void start(int customers) throws InterruptedException {

		int cores = Runtime.getRuntime().availableProcessors();
		cores = customers > cores ? cores : customers;
		ExecutorService fixedPool = Executors.newFixedThreadPool(cores);

		for (int i = 0; i < customers; i++) {
			Runnable task = new BrewTask(grinderMachines, espressoMachines, steamerMachines);
			fixedPool.submit(task);
		}
		fixedPool.shutdown();

		while (!fixedPool.isTerminated()) {
			// wait on main thread, until all threads are done
		}
	}

	private static void addShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("---------------COFFEE SHOP CLOSED-----------------------");
			}
		});
	}

}
