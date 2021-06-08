package com.simulation.shop;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.simulation.shop.machine.EspressoMachine;
import com.simulation.shop.machine.GrinderMachine;
import com.simulation.shop.machine.SteamerMachine;
import com.simulation.shop.util.CoffeeUtility;

public class CoffeeShop {

	private GrinderMachine grinderMachine = new GrinderMachine();
	private EspressoMachine espressoMachine = new EspressoMachine();
	private SteamerMachine steamerMachine = new SteamerMachine();

	public static void main(String[] args) throws InterruptedException {
		Instant start = Instant.now();
		CoffeeShop shop = new CoffeeShop();
		int customers = args.length > 0 ? Integer.parseInt(args[0]) : 1;

		shop.start(customers);

		Instant finish = Instant.now();
		String timeElapsed = CoffeeUtility.timeElapsed(start, finish);
		System.out.println("---------------COFFEE SHOP CLOSED-----------------------");
		System.out.println("time elapsed " + timeElapsed);
	}

	public void start(int customers) throws InterruptedException {
		List<Thread> threads = new ArrayList<>();

		for (int i = 0; i < customers; i++) {
			Runnable task = new BrewTask(grinderMachine, espressoMachine, steamerMachine);
			Thread thread = new Thread(task, "Thread" + i);
			threads.add(thread);
			thread.start();
		}
		// wait on main thread, until all threads are done
		for (Thread t : threads) {
			t.join();
		}
	}

}
