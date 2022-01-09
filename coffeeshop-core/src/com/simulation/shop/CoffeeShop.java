package com.simulation.shop;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

	public static void main(String[] args)  {

		Runnable kioskOperator = () -> {
			try {
				PrintWriter pw = new PrintWriter(new FileWriter(Config.ORDER_POOL, true));
				Scanner sc = new Scanner(System.in);

				String orders = "";
				pw.println(orders);

				while (CoffeeUtility.isShopClosed(orders)) {
					System.out.println("Input no. of orders to be placed : ");
					orders = sc.nextLine();
					pw.println(orders);
					pw.flush();
				}
				sc.close();
				pw.close();
			} catch (IOException e) {
				System.err.println("Error while placing order " + e.getLocalizedMessage());
			}
		};

		Thread kioskOperatorThread = new Thread(kioskOperator);
		kioskOperatorThread.start();

		//The below code is given to main thread, so that results are ready once all orders are processed
		CoffeeShop shop = new CoffeeShop();
		int customers = args.length > 0 ? Integer.parseInt(args[0]) : Config.CUSTOMERS;
		int machines = CoffeeUtility.fetchRequiredMachines(customers);

		CoffeeUtility.loadupBeans(machines, Config.BEANS_INVENTORY_LIMIT);
		CoffeeUtility.loadupMilk(machines, Config.MILK_INVENTORY_LIMIT);
		try {
			shop.start(customers);
		} catch (Exception e) {
			System.err.println("We are currently experiencing some issues " + e.getLocalizedMessage());
		}


	}

	/**
	 * The CoffeeShop on-start keeps polling orders.txt for continuous orders
	 * The application stops, if -1 is encountered in orders.txt
	 */
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

		try (BufferedReader br = new BufferedReader(new FileReader(Config.ORDER_POOL));) {
			String orderData = null;

			while ((orderData = br.readLine()) != null) {
				if (!orderData.equals("")) {
					customers = Integer.parseInt(orderData);
					service(customers, executor);
				}
			}
		}

		long millis = Duration.between(CoffeeShopRuntime.getInstance().getShopOpenTimestamp(), LocalTime.now()).toMillis();
		System.out.println("---------------------COFFEE SHOP CLOSED ("+millis+"ms) --------------------------");
		CoffeeUtility.benchmarks();
		
		executor.shutdown();
	}

	private boolean service(int customers, ExecutorService executor) {
		if (customers == -1) {
			//Close the shop
			return true;
		} else {
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
		return machines.stream().filter(m->m.getGrinderLock().tryLock()).findAny().get();
	}
	
	private EspressoMachine getAvailableEspressoMachine(List<EspressoMachine> machines) {
		return machines.stream().filter(m->m.getEspressoLock().tryLock()).findAny().get();
	}
	
	private SteamerMachine getAvailableSteamerMachine(List<SteamerMachine> machines) {
		return machines.stream().filter(m->m.getSteamerLock().tryLock()).findAny().get();
	}
	
}
