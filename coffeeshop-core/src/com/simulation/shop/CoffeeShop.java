package com.simulation.shop;

import java.util.concurrent.CyclicBarrier;

import com.simulation.shop.config.CoffeeShopConstant;
import com.simulation.shop.machine.EspressoMachine;
import com.simulation.shop.machine.GrinderMachine;
import com.simulation.shop.machine.SteamerMachine;
import com.simulation.shop.task.GrindTask;
import com.simulation.shop.util.CoffeeUtility;

public class CoffeeShop {
	private GrinderMachine grinderMachine = new GrinderMachine();
	private EspressoMachine espressoMachine = new EspressoMachine();
	private SteamerMachine steamerMachine = new SteamerMachine();

	private CyclicBarrier cyclicBarrier;

	public static void main(String[] args) throws Exception {
		CoffeeShop shop = new CoffeeShop();
		int customers = args.length > 0 ? Integer.parseInt(args[0]) : CoffeeShopConstant.CUSTOMERS;
		shop.start(customers);
	}

	public void start(int customers) throws Exception {
		cyclicBarrier = new CyclicBarrier(customers + 1);
		for (int i = 0; i < customers; i++) {
			GrindTask task = new GrindTask(grinderMachine, espressoMachine, steamerMachine, cyclicBarrier);
			Thread grindThread = new Thread(task, "stepA-grind");
			grindThread.start();
		}

		cyclicBarrier.await();
		CoffeeUtility.stats();
		System.out.println("---------------COFFEE SHOP CLOSED-----------------------");

	}

}
