package com.simulation.shop.task;

import java.time.Instant;
import java.util.concurrent.CyclicBarrier;

import com.simulation.shop.config.Step;
import com.simulation.shop.machine.SteamerMachine;
import com.simulation.shop.model.Coffee;
import com.simulation.shop.model.Milk;
import com.simulation.shop.util.CoffeeUtility;

public class SteamTask implements Runnable {

	private Coffee coffee;
	private SteamerMachine steamerMachine;
	private CyclicBarrier cyclicBarrier;

	public SteamTask(Coffee coffee, SteamerMachine steamerMachine, CyclicBarrier cyclicBarrier) {
		this.coffee = coffee;
		this.steamerMachine = steamerMachine;
		this.cyclicBarrier = cyclicBarrier;
	}

	@Override
	public void run() {
		Milk milk = steamMilk(steamerMachine);

		if (milk != null) {
			CoffeeUtility.mix(coffee, milk);
		}

		try {
			cyclicBarrier.await();
		} catch (Exception e) {
			System.err.println("Something went wrong " + e.getLocalizedMessage());
		}
	}

	private Milk steamMilk(SteamerMachine steamerMachine) {
		Instant start = Instant.now();
		Milk milk = steamerMachine.steam();
		Instant finish = Instant.now();
		String timeElapsed = CoffeeUtility.timeElapsed(start, finish);
		CoffeeUtility.collectMetric(Thread.currentThread().getName(), Step.STEAM_MILK, timeElapsed);
		return milk;
	}

}
