package com.simulation.shop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

import com.simulation.shop.config.CoffeeShopConstant;
import com.simulation.shop.machine.EspressoMachine;
import com.simulation.shop.machine.GrinderMachine;
import com.simulation.shop.machine.SteamerMachine;
import com.simulation.shop.model.Coffee;
import com.simulation.shop.model.Grounds;
import com.simulation.shop.task.EspressoTask;
import com.simulation.shop.task.GrindTask;
import com.simulation.shop.task.SteamTask;
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
		// cyclic barrier to wait till all threads have completed executions
		// Just for printing stats
		List<Grounds> listOfgrounds = new ArrayList<Grounds>();
		List<Coffee> listOfCoffee = new ArrayList<Coffee>();
		cyclicBarrier = new CyclicBarrier(3 * customers + 1);
		for (int i = 0; i < customers; i++) {
			GrindTask grindTask = new GrindTask(listOfgrounds, grinderMachine, cyclicBarrier);
			Thread grindThread = new Thread(grindTask, "stepA-grind");
			grindThread.start();

			EspressoTask espressoTask = new EspressoTask(listOfgrounds, listOfCoffee, espressoMachine, cyclicBarrier);
			Thread espressoThread = new Thread(espressoTask, "stepB-espresso");
			espressoThread.start();

			SteamTask steamTask = new SteamTask(listOfCoffee, steamerMachine, cyclicBarrier);
			Thread steamThread = new Thread(steamTask, "stepC-steam");
			steamThread.start();
		}

		cyclicBarrier.await();
		CoffeeUtility.stats();
		System.out.println("---------------COFFEE SHOP CLOSED-----------------------");

	}

}
