package com.simulation.shop;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Lock;

import com.simulation.shop.config.CoffeeShopRuntime;
import com.simulation.shop.config.Config;
import com.simulation.shop.config.Step;
import com.simulation.shop.machine.EspressoMachine;
import com.simulation.shop.machine.GrinderMachine;
import com.simulation.shop.machine.IMachine;
import com.simulation.shop.machine.SteamerMachine;
import com.simulation.shop.model.Product;
import com.simulation.shop.util.CoffeeUtility;

public class CoffeeShop {
	private List<IMachine> grinderMachines;
	private List<IMachine> espressoMachines;
	private List<IMachine> steamerMachines;

	public static void main(String[] args) throws Exception {
		CoffeeShop shop = new CoffeeShop();
		int customers = args.length > 0 ? Integer.parseInt(args[0]) : Config.CUSTOMERS;
		shop.start(customers);
	}

	public void start(int customers) throws Exception {
		CoffeeShopRuntime.getInstance().setShopOpenTimestamp(LocalTime.now());

		System.out.println("-----------------------COFFEE SHOP STARTED-----------------------------");

		grinderMachines = new ArrayList<>();
		espressoMachines = new ArrayList<>();
		steamerMachines = new ArrayList<>();

		for (int i = 0; i < Config.MULTI_SHARED_INSTANCE; i++) {
			grinderMachines.add(new GrinderMachine());
			espressoMachines.add(new EspressoMachine());
			steamerMachines.add(new SteamerMachine());
		}

		List<CompletableFuture<Object>> futures = new ArrayList<>();

		for (int i = 0; i < customers; i++) {
			String metadata = CoffeeUtility.buildMetadata(i);

			CompletableFuture<Object> future = CompletableFuture
					.supplyAsync(() -> operate(grinderMachines, null, metadata, Step.GRIND_COFFEE))
					.thenApply(grounds -> operate(espressoMachines, grounds, metadata, Step.MAKE_ESPRESSO))
					.thenApply(coffee -> operate(steamerMachines, null, metadata, Step.STEAM_MILK));

			futures.add(future);
		}

		// Wait for Async threads to complete
		for (CompletableFuture<Object> future : futures) {
			future.get();
		}

		long millis = Duration.between(CoffeeShopRuntime.getInstance().getShopOpenTimestamp(), LocalTime.now())
				.toMillis();
		System.out.println("---------------------COFFEE SHOP CLOSED (" + millis + "ms) --------------------------");
		CoffeeUtility.benchmarks();
	}

	private Product operate(List<IMachine> machines, Product ingredients, String metadata, Step step) {
		Instant start = Instant.now();

		IMachine availableMachine = null;
		Product product = null;
		try {
			// check if any element in pool is available for lock
			availableMachine = getAvailableMachine(machines, availableMachine);
			product = availableMachine.start();
		} catch (Exception e) {
			System.err.println("something went wrong while processing step " + step.name() + e.getLocalizedMessage());
		} finally {
			availableMachine.getMachineLock().unlock();
		}

		Instant end = Instant.now();
		CoffeeUtility.collectApexMetric(CoffeeUtility.buildThreadMeta(metadata, Thread.currentThread().getName()), step,
				start, end);
		return product;
	}

	private IMachine getAvailableMachine(List<IMachine> machines, IMachine availableMachine) {
		while (availableMachine == null) {
			for (int i = 0; i < machines.size(); i++) {
				IMachine machine = machines.get(i);
				Lock machineLock = machine.getMachineLock();
				if (machineLock.tryLock()) {
					availableMachine = machine;
					break;
				}
			}
		}
		return availableMachine;
	}

}
