package com.simulation.shop.task;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

import com.simulation.shop.config.CoffeeShopConstant;
import com.simulation.shop.config.Step;
import com.simulation.shop.machine.GrinderMachine;
import com.simulation.shop.model.Grounds;
import com.simulation.shop.util.CoffeeUtility;

public class GrindTask implements Runnable {

	private List<Grounds> listOfGrounds;
	private GrinderMachine grinderMachine;
	private CyclicBarrier cyclicBarrier;

	public GrindTask(List<Grounds> listOfgrounds, GrinderMachine grinderMachine, CyclicBarrier cyclicBarrier) {
		this.listOfGrounds = listOfgrounds;
		this.grinderMachine = grinderMachine;

		this.cyclicBarrier = cyclicBarrier;
	}

	@Override
	public void run() {
		grindCoffee(grinderMachine);
	}

	private void grindCoffee(GrinderMachine grinderMachine) {
		Instant start = Instant.now();
		try {
			synchronized (listOfGrounds) {
				while (listOfGrounds.size() == CoffeeShopConstant.MAX_SIZE) {
					listOfGrounds.wait();
				}
				Grounds grounds = grinderMachine.grind();
				listOfGrounds.add(grounds);
				listOfGrounds.notifyAll();
			}
			Instant finish = Instant.now();
			String timeElapsed = CoffeeUtility.timeElapsed(start, finish);
			CoffeeUtility.collectMetric(Thread.currentThread().getName(), Step.GRIND_COFFEE, timeElapsed);

			cyclicBarrier.await();
		} catch (Exception e) {
			System.err.println("Something went wrong " + e.getLocalizedMessage());
		}
	}
}