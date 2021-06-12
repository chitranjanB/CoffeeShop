package com.simulation.shop.task;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

import com.simulation.shop.config.Step;
import com.simulation.shop.machine.SteamerMachine;
import com.simulation.shop.model.Coffee;
import com.simulation.shop.model.Milk;
import com.simulation.shop.util.CoffeeUtility;

public class SteamTask implements Runnable {
	private List<Coffee> listOfCoffee;
	private SteamerMachine steamerMachine;
	private CyclicBarrier cyclicBarrier;

	public SteamTask(List<Coffee> listOfCoffee, SteamerMachine steamerMachine, CyclicBarrier cyclicBarrier) {
		this.listOfCoffee = listOfCoffee;
		this.steamerMachine = steamerMachine;
		this.cyclicBarrier = cyclicBarrier;
	}

	@Override
	public void run() {
		steamMilk(steamerMachine);
	}

	private void steamMilk(SteamerMachine steamerMachine) {
		Instant start = Instant.now();
		try {
			synchronized (listOfCoffee) {
				while (listOfCoffee.isEmpty()) {
					listOfCoffee.wait();
				}
				Coffee coffee = listOfCoffee.remove(0);
				listOfCoffee.notifyAll();
				Milk milk = steamerMachine.steam();
				if (milk != null) {
					CoffeeUtility.mix(coffee, milk);
				}
			}
			Instant finish = Instant.now();
			String timeElapsed = CoffeeUtility.timeElapsed(start, finish);
			CoffeeUtility.collectMetric(Thread.currentThread().getName(), Step.STEAM_MILK, timeElapsed);

			cyclicBarrier.await();
		} catch (Exception e) {
			System.err.println("Something went wrong " + e.getLocalizedMessage());
		}
	}

}