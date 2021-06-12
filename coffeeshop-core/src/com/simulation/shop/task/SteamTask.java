package com.simulation.shop.task;

import java.time.Instant;
import java.util.concurrent.CountDownLatch;

import com.simulation.shop.config.Step;
import com.simulation.shop.machine.Kitchen;
import com.simulation.shop.machine.SteamerMachine;
import com.simulation.shop.model.Coffee;
import com.simulation.shop.model.Milk;
import com.simulation.shop.util.CoffeeUtility;

public class SteamTask implements Runnable {

	private Coffee coffee;
	private CountDownLatch latch;

	public SteamTask(Coffee coffee, CountDownLatch latch) {
		this.latch = latch;
		this.coffee = coffee;
	}

	@Override
	public void run() {
		Milk milk = steamMilk(Kitchen.INSTANCE.getSteamerMachine());

		if (milk != null) {
			CoffeeUtility.mix(coffee, milk);
		}
		latch.countDown();
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
