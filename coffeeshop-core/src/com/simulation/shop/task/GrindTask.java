package com.simulation.shop.task;

import java.time.Instant;
import java.util.concurrent.CyclicBarrier;

import com.simulation.shop.config.Step;
import com.simulation.shop.machine.EspressoMachine;
import com.simulation.shop.machine.GrinderMachine;
import com.simulation.shop.machine.SteamerMachine;
import com.simulation.shop.model.Grounds;
import com.simulation.shop.task.EspressoTask;
import com.simulation.shop.util.CoffeeUtility;

public class GrindTask implements Runnable {

	private GrinderMachine grinderMachine;
	private EspressoMachine espressoMachine;
	private SteamerMachine steamerMachine;
	private CyclicBarrier cyclicBarrier;

	public GrindTask(GrinderMachine grinderMachine, EspressoMachine espressoMachine, SteamerMachine steamerMachine,
			CyclicBarrier cyclicBarrier) {
		this.grinderMachine = grinderMachine;
		this.espressoMachine = espressoMachine;
		this.steamerMachine = steamerMachine;

		this.cyclicBarrier = cyclicBarrier;
	}

	@Override
	public void run() {
		Grounds grounds = grindCoffee(grinderMachine);

		if (grounds != null) {
			Runnable task = new EspressoTask(grounds, espressoMachine, steamerMachine, cyclicBarrier);
			Thread espressoThread = new Thread(task, "stepB-espresso");
			espressoThread.start();
		}
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
