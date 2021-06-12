package com.simulation.shop;

import java.time.Instant;

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

	private GrinderMachine grinderMachine;
	private EspressoMachine espressoMachine;
	private SteamerMachine steamerMachine;

	public BrewTask(GrinderMachine grinderMachine, EspressoMachine espressoMachine, SteamerMachine steamerMachine) {
		this.grinderMachine = grinderMachine;
		this.espressoMachine = espressoMachine;
		this.steamerMachine = steamerMachine;
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
		Grounds grounds = grindCoffee(grinderMachine);
		Coffee coffee = makeEspresso(espressoMachine, grounds);
		Milk milk = steamMilk(steamerMachine);
		return makeLatte(coffee, milk);
	}

	private Grounds grindCoffee(GrinderMachine grinderMachine) {
		Instant start = Instant.now();
		Grounds grounds = grinderMachine.grind();
		Instant finish = Instant.now();
		String timeElapsed = CoffeeUtility.timeElapsed(start, finish);
		CoffeeUtility.collectMetric(Thread.currentThread().getName(), Step.GRIND_COFFEE, timeElapsed);
		return grounds;
	}

	private Coffee makeEspresso(EspressoMachine espressoMachine, Grounds grounds) {
		Instant start = Instant.now();
		Coffee coffee = espressoMachine.concentrate();
		Instant finish = Instant.now();
		String timeElapsed = CoffeeUtility.timeElapsed(start, finish);
		CoffeeUtility.collectMetric(Thread.currentThread().getName(), Step.MAKE_ESPRESSO, timeElapsed);
		return coffee;
	}

	private Milk steamMilk(SteamerMachine steamerMachine) {
		Instant start = Instant.now();
		Milk milk = steamerMachine.steam();
		Instant finish = Instant.now();
		String timeElapsed = CoffeeUtility.timeElapsed(start, finish);
		CoffeeUtility.collectMetric(Thread.currentThread().getName(), Step.STEAM_MILK, timeElapsed);
		return milk;
	}

	private Latte makeLatte(Coffee coffee, Milk milk) {
		return CoffeeUtility.mix(coffee, milk);
	}

}
