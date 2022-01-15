package com.simulation.shop;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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

	public static void main(String[] args) throws IOException {
		PipedOutputStream pop = new PipedOutputStream();
		PipedInputStream pip = new PipedInputStream();
		pop.connect(pip);

		Runnable kioskOperator = () -> {
			try {
				Scanner sc = new Scanner(System.in);
				int orders = -1;

				while (!CoffeeUtility.isShopClosed(orders)) {
					System.out.println("Input no. of orders to be placed : ");
					orders = sc.nextInt();
					CoffeeUtility.writeNoOfOrders(pop, orders);
				}
				sc.close();
			} catch (Exception e) {
				System.err.println("Error while placing order " + e.getLocalizedMessage());
			}
		};

		Thread kioskOperatorThread = new Thread(kioskOperator);
		kioskOperatorThread.start();

		//The below code is given to main thread, so that results are ready once all orders are processed
		CoffeeShop shop = new CoffeeShop();
		int machines = CoffeeUtility.fetchRequiredMachines(Config.CUSTOMERS);

		CoffeeUtility.loadupBeans(machines, Config.BEANS_INVENTORY_LIMIT);
		CoffeeUtility.loadupMilk(machines, Config.MILK_INVENTORY_LIMIT);
		try {
			shop.start(pip);
		} catch (Exception e) {
			System.err.println("We are currently experiencing some issues " + e.getLocalizedMessage());
		}
	}

	/**
	 * The CoffeeShop on-start keeps polling piped input stream for continuous orders
	 * The application stops, if 0 is encountered in input stream
	 * @param pip
	 */
	public void start(PipedInputStream pip) throws Exception {
		CoffeeShopRuntime.getInstance().setShopOpenTimestamp(LocalTime.now());

		System.out.println("-----------------------COFFEE SHOP STARTED-----------------------------");

		grinderMachines = Stream.iterate(1, i -> i + 1)
				.limit(Config.CUSTOMERS)
				.map(i -> new GrinderMachine(String.format(Config.MACHINEID_FORMAT, Config.GRINDER_PREFIX, i)))
				.collect(Collectors.toList());

		espressoMachines = Stream.iterate(1, i -> i + 1)
				.limit(Config.CUSTOMERS)
				.map(i -> new EspressoMachine(String.format(Config.MACHINEID_FORMAT, Config.ESPRESSO_PREFIX, i)))
				.collect(Collectors.toList());

		steamerMachines = Stream.iterate(1, i -> i + 1)
				.limit(Config.CUSTOMERS)
				.map(i -> new SteamerMachine(String.format(Config.MACHINEID_FORMAT, Config.STEAMER_PREFIX, i)))
				.collect(Collectors.toList());

		ExecutorService executor = Executors.newFixedThreadPool(CoffeeUtility.fetchRequiredMachines(Config.CUSTOMERS));

		int orders = 0;
		while ((orders = CoffeeUtility.readNoOfOrders(pip)) !=0) {
			service(orders, executor);
		}

		long millis = Duration.between(CoffeeShopRuntime.getInstance().getShopOpenTimestamp(), LocalTime.now()).toMillis();
		System.out.println("---------------------COFFEE SHOP CLOSED ("+millis+"ms) --------------------------");
		CoffeeUtility.benchmarks();
		
		executor.shutdown();
	}

	private boolean service(int orders, ExecutorService executor) {
		if (orders == -1) {
			//Close the shop
			return true;
		} else {
			List<CompletableFuture<Coffee>> futures = Stream.iterate(1, i -> i + 1)
					.limit(orders)
					.map(i -> {
						String metadata = CoffeeUtility.buildMetadata(i);
						return CompletableFuture
								.supplyAsync(() -> grindCoffee(grinderMachines, metadata), executor)
								.thenApply(grounds -> makeEspresso(espressoMachines, grounds, metadata))
								.thenApply(espresso -> steamMilk(steamerMachines, metadata)).thenApply(steamedMilk -> mix());
					})
					.collect(Collectors.toList());

			// Wait for Async threads to complete
			futures.stream()
					.forEach(CoffeeUtility.handlingConsumerWrapper(future -> future.get(),
							Exception.class));
		}
		return false;
	}

	private Grounds grindCoffee(List<GrinderMachine> grinderMachines, String metadata) {
		Instant start = Instant.now();
		Grounds grounds = getAvailableGrinderMachine(grinderMachines).grind(metadata);
		Instant end = Instant.now();
		CoffeeUtility.collectApexMetric(CoffeeUtility.buildThreadMeta(metadata, Thread.currentThread().getName()),
				Step.GRIND_COFFEE, start, end);
		return grounds;
	}

	private Coffee makeEspresso(List<EspressoMachine> espressoMachines, Grounds grounds, String metadata) {
		Instant start = Instant.now();
		Coffee coffee = getAvailableEspressoMachine(espressoMachines).concentrate(metadata);
		Instant end = Instant.now();
		CoffeeUtility.collectApexMetric(CoffeeUtility.buildThreadMeta(metadata, Thread.currentThread().getName()),
				Step.MAKE_ESPRESSO, start, end);
		return coffee;
	}

	private SteamedMilk steamMilk(List<SteamerMachine> steamerMachines, String metadata) {
		Instant start = Instant.now();
		SteamedMilk milk = getAvailableSteamerMachine(steamerMachines).steam(metadata);
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
		Optional<GrinderMachine> optional = machines.stream().filter(m -> {
			boolean isAvailable = false;
			try {
				isAvailable = !m.isBeanInventoryEmpty() && m.getGrinderLock().tryLock(100, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return isAvailable;
		}).findAny();
		return optional.orElseThrow(()->new OutOfIngredientsException("Beans Inventory is empty"));
	}

	private EspressoMachine getAvailableEspressoMachine(List<EspressoMachine> machines) {
		return machines.stream().filter(m -> {
			boolean isAvailable = false;
			try {
				isAvailable = m.getEspressoLock().tryLock(100, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return isAvailable;
		}).findAny().get();
	}

	private SteamerMachine getAvailableSteamerMachine(List<SteamerMachine> machines) {
		Optional<SteamerMachine> optional = machines.stream().filter(m -> {
			boolean isAvailable = false;
			try {
				isAvailable =  !m.isMilkInventoryEmpty() && m.getSteamerLock().tryLock(100, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return isAvailable;
		}).findAny();

		return optional.orElseThrow(()->new OutOfIngredientsException("Milk Inventory is empty"));
	}
	
}
