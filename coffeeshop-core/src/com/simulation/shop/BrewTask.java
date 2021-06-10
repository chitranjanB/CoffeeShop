package com.simulation.shop;

import java.time.Instant;
import java.util.concurrent.BlockingQueue;

import com.simulation.shop.config.Step;
import com.simulation.shop.machine.EspressoMachine;
import com.simulation.shop.machine.GrinderMachine;
import com.simulation.shop.machine.SteamerMachine;
import com.simulation.shop.model.Coffee;
import com.simulation.shop.model.Grounds;
import com.simulation.shop.model.Latte;
import com.simulation.shop.model.Milk;
import com.simulation.shop.util.CoffeeUtility;

public class BrewTask implements Runnable {

	private BlockingQueue<GrinderMachine> grinderMachines;
	private BlockingQueue<EspressoMachine> espressoMachines;
	private BlockingQueue<SteamerMachine> steamerMachines;

	public BrewTask(BlockingQueue<GrinderMachine> grinderMachines, BlockingQueue<EspressoMachine> espressoMachines,
			BlockingQueue<SteamerMachine> steamerMachines) {
		this.grinderMachines = grinderMachines;
		this.espressoMachines = espressoMachines;
		this.steamerMachines = steamerMachines;
	}

	@Override
	public void run() {
		Instant start = Instant.now();
		brewLatte();
		Instant finish = Instant.now();
		String timeElapsed = CoffeeUtility.timeElapsed(start, finish);
		CoffeeUtility.collectMetric(Thread.currentThread().getName(), Step.BREW, timeElapsed);
	}

	public Latte brewLatte() {
		Grounds grounds = grindCoffee(grinderMachines);
		Coffee coffee = makeEspresso(espressoMachines, grounds);
		Milk milk = steamMilk(steamerMachines);
		return makeLatte(coffee, milk);
	}

	private Grounds grindCoffee(BlockingQueue<GrinderMachine> grinderMachines) {
		Instant start = Instant.now();
		GrinderMachine grinderMachine = null;
		Grounds grounds = null;
		try {
			grinderMachine = grinderMachines.take();
			grounds = grinderMachine.grind();
			grinderMachines.put(grinderMachine);
		} catch (Exception e) {
			System.err.println("something went wrong while " + Step.GRIND_COFFEE + e.getLocalizedMessage());
		}

		Instant finish = Instant.now();
		String timeElapsed = CoffeeUtility.timeElapsed(start, finish);
		CoffeeUtility.collectMetric(Thread.currentThread().getName(), Step.GRIND_COFFEE, timeElapsed);
		return grounds;
	}

	private Coffee makeEspresso(BlockingQueue<EspressoMachine> espressoMachines, Grounds grounds) {
		Instant start = Instant.now();

		Coffee coffee = null;
		try {
			EspressoMachine espressoMachine = espressoMachines.take();
			coffee = espressoMachine.concentrate();
			espressoMachines.put(espressoMachine);
		} catch (Exception e) {
			System.err.println("something went wrong while " + Step.MAKE_ESPRESSO + e.getLocalizedMessage());
		}

		Instant finish = Instant.now();
		String timeElapsed = CoffeeUtility.timeElapsed(start, finish);
		CoffeeUtility.collectMetric(Thread.currentThread().getName(), Step.MAKE_ESPRESSO, timeElapsed);
		return coffee;
	}

	private Milk steamMilk(BlockingQueue<SteamerMachine> steamerMachines) {
		Instant start = Instant.now();

		Milk milk = null;
		try {
			SteamerMachine steamerMachine = steamerMachines.take();
			milk = steamerMachine.steam();
			steamerMachines.put(steamerMachine);
		} catch (Exception e) {
			System.err.println("something went wrong while " + Step.STEAM_MILK + e.getLocalizedMessage());
		}

		Instant finish = Instant.now();
		String timeElapsed = CoffeeUtility.timeElapsed(start, finish);
		CoffeeUtility.collectMetric(Thread.currentThread().getName(), Step.STEAM_MILK, timeElapsed);
		return milk;
	}

	private Latte makeLatte(Coffee coffee, Milk milk) {
		return CoffeeUtility.mix(coffee, milk);
	}

}
