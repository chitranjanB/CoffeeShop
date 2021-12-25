package com.simulation.shop;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.simulation.shop.config.CoffeeShopRuntime;
import com.simulation.shop.config.Config;
import com.simulation.shop.config.Step;
import com.simulation.shop.machine.EspressoMachine;
import com.simulation.shop.machine.GrinderMachine;
import com.simulation.shop.machine.SteamerMachine;
import com.simulation.shop.model.Coffee;
import com.simulation.shop.model.Grounds;
import com.simulation.shop.model.SteamedMilk;
import com.simulation.shop.util.CoffeeUtility;

public class CoffeeShop {
	
	private GrinderMachine grinderMachine = new GrinderMachine();
	private EspressoMachine espressoMachine = new EspressoMachine();
	private SteamerMachine steamerMachine = new SteamerMachine();

	
	public static void main(String[] args) throws Exception {
		CoffeeShop shop = new CoffeeShop();
		int customers = args.length > 0 ? Integer.parseInt(args[0]) : Config.CUSTOMERS;
		CoffeeUtility.loadupBeans(Config.BEANS_INVENTORY_LIMIT);
		CoffeeUtility.loadupMilk(Config.MILK_INVENTORY_LIMIT);
		try {
			shop.start(customers);
		} catch (CoffeeShopException e) {
			System.err.println("We are currently experiencing some issues " + e.getLocalizedMessage());
		}
	}

	public void start(int customers) throws Exception {
		CoffeeShopRuntime.getInstance().setShopOpenTimestamp(LocalTime.now());
		
		System.out.println("-----------------------COFFEE SHOP STARTED-----------------------------");
		
		List<CompletableFuture<Object>> futures = new ArrayList<>();
		
		for (int i = 0; i < customers; i++) {
			String metadata = CoffeeUtility.buildMetadata(i);

			CompletableFuture<Object> future = CompletableFuture
					.supplyAsync(() -> grindCoffee(grinderMachine, metadata))
					.thenApply(grounds -> makeEspresso(espressoMachine, grounds, metadata))
					.thenApply(espresso -> steamMilk(steamerMachine, metadata)).thenApply(steamedMilk -> mix());

			futures.add(future);
		}

		// Wait for Async threads to complete
		for(CompletableFuture<Object> future:futures) {
			future.get();
		}
		
		long millis = Duration.between(CoffeeShopRuntime.getInstance().getShopOpenTimestamp(), LocalTime.now()).toMillis();
		System.out.println("---------------------COFFEE SHOP CLOSED ("+millis+"ms) --------------------------");
		CoffeeUtility.benchmarks();
	}

	private Grounds grindCoffee(GrinderMachine grinderMachine, String metadata) {
		Instant start = Instant.now();
		Grounds grounds = grinderMachine.grind();
		Instant end = Instant.now();
		CoffeeUtility.collectApexMetric(CoffeeUtility.buildThreadMeta(metadata, Thread.currentThread().getName()),
				Step.GRIND_COFFEE, start, end);
		return grounds;
	}

	private Coffee makeEspresso(EspressoMachine espressoMachine, Grounds grounds, String metadata) {
		Instant start = Instant.now();
		Coffee coffee = espressoMachine.concentrate();
		Instant end = Instant.now();
		CoffeeUtility.collectApexMetric(CoffeeUtility.buildThreadMeta(metadata, Thread.currentThread().getName()),
				Step.MAKE_ESPRESSO, start, end);
		return coffee;
	}

	private SteamedMilk steamMilk(SteamerMachine steamerMachine, String metadata) {
		Instant start = Instant.now();
		SteamedMilk milk = steamerMachine.steam();
		Instant end = Instant.now();
		CoffeeUtility.collectApexMetric(CoffeeUtility.buildThreadMeta(metadata, Thread.currentThread().getName()),
				Step.STEAM_MILK, start, end);
		return milk;
	}
	
	private Coffee mix() {
		Coffee coffee = new Coffee();
		return coffee;
	}
	
}
