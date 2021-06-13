package com.simulation.shop.task;

import java.time.Instant;
import java.util.concurrent.Callable;

import com.simulation.shop.config.Step;
import com.simulation.shop.machine.SteamerMachine;
import com.simulation.shop.model.Milk;
import com.simulation.shop.util.CoffeeUtility;

public class SteamTask implements Callable<Milk> {

	private SteamerMachine steamerMachine;

	public SteamTask(SteamerMachine steamerMachine) {
		this.steamerMachine = steamerMachine;
	}

	@Override
	public Milk call() throws Exception {
		Milk milk = steamMilk(steamerMachine);
		return milk;
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
