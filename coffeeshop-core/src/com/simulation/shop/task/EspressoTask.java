package com.simulation.shop.task;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

import com.simulation.shop.config.CoffeeShopConstant;
import com.simulation.shop.config.Step;
import com.simulation.shop.machine.EspressoMachine;
import com.simulation.shop.model.Coffee;
import com.simulation.shop.model.Grounds;
import com.simulation.shop.util.CoffeeUtility;

public class EspressoTask implements Runnable {

	private List<Grounds> listOfGrounds;
	private List<Coffee> listOfCoffee;
	private EspressoMachine espressoMachine;
	private CyclicBarrier cyclicBarrier;

	public EspressoTask(List<Grounds> listOfgrounds, List<Coffee> listOfCoffee, EspressoMachine espressoMachine,
			CyclicBarrier cyclicBarrier) {
		this.listOfGrounds = listOfgrounds;
		this.listOfCoffee = listOfCoffee;
		this.espressoMachine = espressoMachine;
		this.cyclicBarrier = cyclicBarrier;
	}

	@Override
	public void run() {
		makeEspresso(espressoMachine);
	}

	private void makeEspresso(EspressoMachine espressoMachine) {
		Instant start = Instant.now();
		try {
			synchronized (listOfGrounds) {
				while (listOfGrounds.isEmpty()) {
					listOfGrounds.wait();
				}
				Grounds grounds = listOfGrounds.remove(0);
				listOfGrounds.notifyAll();

				synchronized (listOfCoffee) {
					while (listOfCoffee.size() == CoffeeShopConstant.MAX_SIZE) {
						listOfCoffee.wait();
					}
					Coffee coffee = espressoMachine.concentrate(grounds);
					listOfCoffee.add(coffee);
					listOfCoffee.notifyAll();
				}
			}

			Instant finish = Instant.now();
			String timeElapsed = CoffeeUtility.timeElapsed(start, finish);
			CoffeeUtility.collectMetric(Thread.currentThread().getName(), Step.MAKE_ESPRESSO, timeElapsed);
			cyclicBarrier.await();
		} catch (Exception e) {
			System.err.println("Something went wrong " + e.getLocalizedMessage());
		}
	}

}