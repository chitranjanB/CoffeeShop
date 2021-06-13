package com.simulation.shop.task;

import java.time.Instant;
import java.util.concurrent.Callable;

import com.simulation.shop.config.Step;
import com.simulation.shop.machine.GrinderMachine;
import com.simulation.shop.model.Grounds;
import com.simulation.shop.util.CoffeeUtility;

public class GrindTask implements Callable<Grounds> {

	private GrinderMachine grinderMachine;

	public GrindTask(GrinderMachine grinderMachine) {
		this.grinderMachine = grinderMachine;
	}

	@Override
	public Grounds call() throws Exception {
		Grounds grounds = grindCoffee(grinderMachine);
		return grounds;
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
