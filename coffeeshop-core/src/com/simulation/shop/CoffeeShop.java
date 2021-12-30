package com.simulation.shop;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	
	private List<GrinderMachine> grinderMachines;
	private List<EspressoMachine> espressoMachines;
	private List<SteamerMachine> steamerMachines;

	
	public static void main(String[] args) throws Exception {
		CoffeeShop shop = new CoffeeShop();
		int customers = args.length > 0 ? Integer.parseInt(args[0]) : Config.CUSTOMERS;
		int machines = CoffeeUtility.fetchRequiredMachines(customers);
		
		CoffeeUtility.loadupBeans(machines, Config.BEANS_INVENTORY_LIMIT);
		CoffeeUtility.loadupMilk(machines, Config.MILK_INVENTORY_LIMIT);
		try {
			shop.start(customers);
		} catch (CoffeeShopException e) {
			System.err.println("We are currently experiencing some issues " + e.getLocalizedMessage());
		}
	}

	public void start(int customers) throws Exception {
		CoffeeShopRuntime.getInstance().setShopOpenTimestamp(LocalTime.now());
		
		System.out.println("-----------------------COFFEE SHOP STARTED-----------------------------");
		
		grinderMachines = Stream.iterate(1, i -> i + 1)
				.limit(customers)
				.map(i -> new GrinderMachine())
				.collect(Collectors.toList());

		espressoMachines = Stream.iterate(1, i -> i + 1)
				.limit(customers)
				.map(i -> new EspressoMachine())
				.collect(Collectors.toList());

		steamerMachines = Stream.iterate(1, i -> i + 1)
				.limit(customers)
				.map(i -> new SteamerMachine())
				.collect(Collectors.toList());
		 
		ExecutorService executor = Executors.newFixedThreadPool(CoffeeUtility.fetchRequiredMachines(customers));

		List<CompletableFuture<Coffee>> futures = Stream.iterate(1, i -> i + 1)
				.limit(customers)
				.map(i -> {
					String metadata = CoffeeUtility.buildMetadata(i);
					return CompletableFuture
							.supplyAsync(() -> grindCoffee(grinderMachines, metadata), executor)
							.thenApply(grounds -> makeEspresso(espressoMachines, grounds, metadata))
							.thenApply(espresso -> steamMilk(steamerMachines, metadata)).thenApply(steamedMilk -> mix());
				})
				.collect(Collectors.toList());


		// Wait for Async threads to complete
		futures.stream().
				forEach(CoffeeUtility.handlingConsumerWrapper(future -> future.get(), Exception.class));

		long millis = Duration.between(CoffeeShopRuntime.getInstance().getShopOpenTimestamp(), LocalTime.now()).toMillis();
		System.out.println("---------------------COFFEE SHOP CLOSED ("+millis+"ms) --------------------------");
		CoffeeUtility.benchmarks();
		
		executor.shutdown();
	}


	private Grounds grindCoffee(List<GrinderMachine> grinderMachines, String metadata) {
		Instant start = Instant.now();
		Grounds grounds = getAvailableGrinderMachine(grinderMachines).grind();
		Instant end = Instant.now();
		CoffeeUtility.collectApexMetric(CoffeeUtility.buildThreadMeta(metadata, Thread.currentThread().getName()),
				Step.GRIND_COFFEE, start, end);
		return grounds;
	}

	private Coffee makeEspresso(List<EspressoMachine> espressoMachines, Grounds grounds, String metadata) {
		Instant start = Instant.now();
		Coffee coffee = getAvailableEspressoMachine(espressoMachines).concentrate();
		Instant end = Instant.now();
		CoffeeUtility.collectApexMetric(CoffeeUtility.buildThreadMeta(metadata, Thread.currentThread().getName()),
				Step.MAKE_ESPRESSO, start, end);
		return coffee;
	}

	private SteamedMilk steamMilk(List<SteamerMachine> steamerMachines, String metadata) {
		Instant start = Instant.now();
		SteamedMilk milk = getAvailableSteamerMachine(steamerMachines).steam();
		Instant end = Instant.now();
		CoffeeUtility.collectApexMetric(CoffeeUtility.buildThreadMeta(metadata, Thread.currentThread().getName()),
				Step.STEAM_MILK, start, end);
		return milk;
	}
	
	private Coffee mix() {
		Coffee coffee = new Coffee();
		return coffee;
	}
	
	private GrinderMachine getAvailableGrinderMachine(List<GrinderMachine> machines) {
		return machines.stream().filter(m->m.getGrinderLock().tryLock()).findAny().get();
	}
	
	private EspressoMachine getAvailableEspressoMachine(List<EspressoMachine> machines) {
		return machines.stream().filter(m->m.getEspressoLock().tryLock()).findAny().get();
	}
	
	private SteamerMachine getAvailableSteamerMachine(List<SteamerMachine> machines) {
		return machines.stream().filter(m->m.getSteamerLock().tryLock()).findAny().get();
	}
	
}
