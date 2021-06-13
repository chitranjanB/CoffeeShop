package com.simulation.shop.task;

import java.time.Instant;
import java.util.concurrent.Callable;

import com.simulation.shop.config.Step;
import com.simulation.shop.machine.EspressoMachine;
import com.simulation.shop.model.Coffee;
import com.simulation.shop.model.Grounds;
import com.simulation.shop.util.CoffeeUtility;

public class EspressoTask implements Callable<Coffee> {

	private Grounds grounds;
	private EspressoMachine espressoMachine;

	public EspressoTask(Grounds grounds, EspressoMachine espressoMachine) {
		this.espressoMachine = espressoMachine;
	}

	@Override
	public Coffee call() throws Exception {
		Coffee coffee = makeEspresso(espressoMachine, grounds);
		return coffee;
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
