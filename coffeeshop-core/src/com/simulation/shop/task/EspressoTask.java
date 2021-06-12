package com.simulation.shop.task;

import java.time.Instant;
import java.util.concurrent.CyclicBarrier;

import com.simulation.shop.config.Step;
import com.simulation.shop.machine.EspressoMachine;
import com.simulation.shop.machine.SteamerMachine;
import com.simulation.shop.model.Coffee;
import com.simulation.shop.model.Grounds;
import com.simulation.shop.util.CoffeeUtility;

public class EspressoTask implements Runnable {

	private Grounds grounds;
	private EspressoMachine espressoMachine;
	private SteamerMachine steamerMachine;
	private CyclicBarrier cyclicBarrier;

	public EspressoTask(Grounds grounds, EspressoMachine espressoMachine, SteamerMachine steamerMachine,
			CyclicBarrier cyclicBarrier) {
		this.espressoMachine = espressoMachine;
		this.steamerMachine = steamerMachine;
		this.cyclicBarrier = cyclicBarrier;
	}

	@Override
	public void run() {
		Coffee coffee = makeEspresso(espressoMachine, grounds);
		if (coffee != null) {
			Runnable task = new SteamTask(coffee, steamerMachine, cyclicBarrier);
			Thread steamThread = new Thread(task, "stepC-steam");
			steamThread.start();
		}
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
