package com.simulation.shop;

import java.time.Instant;

import com.simulation.shop.machine.EspressoMachine;
import com.simulation.shop.machine.GrinderMachine;
import com.simulation.shop.machine.SteamerMachine;
import com.simulation.shop.model.Coffee;
import com.simulation.shop.model.Grounds;
import com.simulation.shop.model.Latte;
import com.simulation.shop.model.Milk;
import com.simulation.shop.util.CoffeeUtility;

public class CoffeeShop {

	private GrinderMachine grinderMachine = new GrinderMachine();
	private EspressoMachine espressoMachine = new EspressoMachine();
	private SteamerMachine steamerMachine = new SteamerMachine();

	public Latte brewLatte() {
		Grounds grounds = grindCoffee(grinderMachine);
		Coffee coffee = makeEspresso(espressoMachine, grounds);
		Milk milk = steamMilk(steamerMachine);
		return makeLatte(coffee, milk);
	}

	private Grounds grindCoffee(GrinderMachine grinderMachine) {
		Grounds grounds = grinderMachine.grind();
		return grounds;
	}

	private Coffee makeEspresso(EspressoMachine espressoMachine, Grounds grounds) {
		Coffee coffee = espressoMachine.concentrate();
		return coffee;
	}

	private Milk steamMilk(SteamerMachine steamerMachine) {
		Milk milk = steamerMachine.steam();
		return milk;
	}

	private Latte makeLatte(Coffee coffee, Milk milk) {
		return CoffeeUtility.mix(coffee, milk);
	}

	public static void main(String[] args) {
		Instant start = Instant.now();
		CoffeeShop shop = new CoffeeShop();
		int customers = args.length > 0 ? Integer.parseInt(args[0]) : 1;

		shop.start(customers);

		Instant finish = Instant.now();
		String timeElapsed = CoffeeUtility.timeElapsed(start, finish);
		System.out.println("---------------COFFEE SHOP CLOSED-----------------------");
		System.out.println("time elapsed " + timeElapsed);
	}

	public void start(int customers) {
		for (int i = 0; i < customers; i++) {
			brewLatte();
		}
	}

}
