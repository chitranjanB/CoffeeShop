package com.simulation.shop.task;

import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import com.simulation.shop.config.Step;
import com.simulation.shop.machine.GrinderMachine;
import com.simulation.shop.machine.Kitchen;
import com.simulation.shop.model.Grounds;
import com.simulation.shop.util.CoffeeUtility;

public class GrindTask implements Runnable {
	private ExecutorService espressoExecutor;
	private ExecutorService steamExecutor;
	private CountDownLatch latch;

	public GrindTask(ExecutorService espressoExecutor, ExecutorService steamExecutor, CountDownLatch latch) {
		this.espressoExecutor = espressoExecutor;
		this.steamExecutor = steamExecutor;
		this.latch = latch;
	}

	@Override
	public void run() {
		Grounds grounds = grindCoffee(Kitchen.INSTANCE.getGrinderMachine());

		if (grounds != null) {
			Runnable task = new EspressoTask(grounds, steamExecutor, latch);
			espressoExecutor.submit(task);
		}
		latch.countDown();
	}

	private Grounds grindCoffee(GrinderMachine grinderMachine) {
		Instant start = Instant.now();
		Grounds grounds = grinderMachine.grind();
		Instant finish = Instant.now();
		String timeElapsed = CoffeeUtility.timeElapsed(start, finish);
		CoffeeUtility.collectMetric(Thread.currentThread().getName(), Step.GRIND_COFFEE, timeElapsed);
		return grounds;
	}

}
