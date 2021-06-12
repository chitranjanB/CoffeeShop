package com.simulation.shop.task;

import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import com.simulation.shop.config.Step;
import com.simulation.shop.machine.EspressoMachine;
import com.simulation.shop.machine.Kitchen;
import com.simulation.shop.model.Coffee;
import com.simulation.shop.model.Grounds;
import com.simulation.shop.util.CoffeeUtility;

public class EspressoTask implements Runnable {

	private Grounds grounds;
	private ExecutorService steamExecutor;
	private CountDownLatch latch;

	public EspressoTask(Grounds grounds, ExecutorService steamExecutor, CountDownLatch latch) {
		this.steamExecutor = steamExecutor;
		this.latch = latch;
	}

	@Override
	public void run() {
		Coffee coffee = makeEspresso(Kitchen.INSTANCE.getEspressoMachine(), grounds);
		if (coffee != null) {
			Runnable task = new SteamTask(coffee, latch);
			steamExecutor.submit(task);
		}
		latch.countDown();
	}

	private Coffee makeEspresso(EspressoMachine espressoMachine, Grounds grounds) {
		Instant start = Instant.now();
		Coffee coffee = espressoMachine.concentrate();
		Instant finish = Instant.now();
		String timeElapsed = CoffeeUtility.timeElapsed(start, finish);
		CoffeeUtility.collectMetric(Thread.currentThread().getName(), Step.MAKE_ESPRESSO, timeElapsed);
		return coffee;
	}

}
