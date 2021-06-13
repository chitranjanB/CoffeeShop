package com.simulation.shop;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.simulation.shop.config.Config;
import com.simulation.shop.machine.EspressoMachine;
import com.simulation.shop.machine.GrinderMachine;
import com.simulation.shop.machine.SteamerMachine;
import com.simulation.shop.model.Coffee;
import com.simulation.shop.model.Grounds;
import com.simulation.shop.model.Milk;
import com.simulation.shop.task.EspressoTask;
import com.simulation.shop.task.GrindTask;
import com.simulation.shop.task.SteamTask;
import com.simulation.shop.util.CoffeeUtility;

public class CoffeeShop {
	private GrinderMachine grinderMachine = new GrinderMachine();
	private EspressoMachine espressoMachine = new EspressoMachine();
	private SteamerMachine steamerMachine = new SteamerMachine();

	public static void main(String[] args) throws Exception {
		CoffeeShop shop = new CoffeeShop();
		int customers = args.length > 0 ? Integer.parseInt(args[0]) : Config.CUSTOMERS;
		shop.start(customers);
	}

	public void start(int customers) throws Exception {
		System.out.println("-----------------------COFFEE SHOP STARTED-----------------------------");
		ExecutorService grindExecutor = Executors.newSingleThreadExecutor();
		ExecutorService espressoExecutor = Executors.newSingleThreadExecutor();
		ExecutorService steamExecutor = Executors.newSingleThreadExecutor();

		List<Long> brewSamples = new ArrayList<Long>();

		for (int i = 0; i < customers; i++) {
			Instant start = Instant.now();
			Future<Grounds> groundsFuture = grindExecutor.submit(new GrindTask(grinderMachine));
			Grounds grounds = groundsFuture.get();

			Future<Coffee> espressoFuture = espressoExecutor.submit(new EspressoTask(grounds, espressoMachine));
			Coffee coffee = espressoFuture.get();

			Future<Milk> steamFuture = steamExecutor.submit(new SteamTask(steamerMachine));
			Milk milk = steamFuture.get();

			if (coffee != null && milk != null) {
				CoffeeUtility.mix(coffee, milk);
			}
			Instant finish = Instant.now();
			brewSamples.add(Duration.between(start, finish).toMillis());
		}
		
		CoffeeUtility.benchmarks();
		CoffeeUtility.stats(brewSamples);
	}

}
