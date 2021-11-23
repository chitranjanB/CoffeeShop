package com.simulation.shop;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import com.simulation.shop.config.Config;
import com.simulation.shop.config.Step;
import com.simulation.shop.machine.EspressoMachine;
import com.simulation.shop.machine.GrinderMachine;
import com.simulation.shop.machine.SteamerMachine;
import com.simulation.shop.model.Coffee;
import com.simulation.shop.model.Grounds;
import com.simulation.shop.model.Milk;
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

		for (int i = 0; i < customers; i++) {
			String metadata = CoffeeUtility.buildMetadata(i);

			CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> grindCoffee(grinderMachine, metadata))
					.thenApply(grounds -> makeEspresso(espressoMachine, grounds, metadata))
					.thenApply(coffee -> steamMilk(steamerMachine, metadata)).thenApply(milk -> print(milk, metadata))
					.thenRun(() -> System.out.println("done"));

			future.get();
		}

		// Wait for Async threads to complete
		// Thread.sleep(3000);

		CoffeeUtility.benchmarks();
	}

	private Milk print(Milk milk, String metadata) {
		System.out.println(milk);
		return milk;
	}

	private Grounds grindCoffee(GrinderMachine grinderMachine, String metadata) {
		Instant start = Instant.now();
		Grounds grounds = grinderMachine.grind();
		Instant finish = Instant.now();
		String timeElapsed = CoffeeUtility.timeElapsed(start, finish);
		CoffeeUtility.collectMetric(CoffeeUtility.buildThreadMeta(metadata, Thread.currentThread().getName()),
				Step.GRIND_COFFEE, timeElapsed);
		return grounds;
	}

	private Coffee makeEspresso(EspressoMachine espressoMachine, Grounds grounds, String metadata) {
		Instant start = Instant.now();
		Coffee coffee = espressoMachine.concentrate();
		Instant finish = Instant.now();
		String timeElapsed = CoffeeUtility.timeElapsed(start, finish);
		CoffeeUtility.collectMetric(CoffeeUtility.buildThreadMeta(metadata, Thread.currentThread().getName()),
				Step.MAKE_ESPRESSO, timeElapsed);
		return coffee;
	}

	private Milk steamMilk(SteamerMachine steamerMachine, String metadata) {
		Instant start = Instant.now();
		Milk milk = steamerMachine.steam();
		Instant finish = Instant.now();
		String timeElapsed = CoffeeUtility.timeElapsed(start, finish);
		CoffeeUtility.collectMetric(CoffeeUtility.buildThreadMeta(metadata, Thread.currentThread().getName()),
				Step.STEAM_MILK, timeElapsed);
		return milk;
	}
}
