package com.simulation.shop;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import com.simulation.shop.machine.EspressoMachine;
import com.simulation.shop.machine.GrinderMachine;
import com.simulation.shop.machine.SteamerMachine;
import com.simulation.shop.model.Coffee;
import com.simulation.shop.model.Grounds;
import com.simulation.shop.model.Latte;
import com.simulation.shop.model.Milk;
import com.simulation.shop.util.CoffeeUtility;

public class BrewTask implements Runnable {

	private List<GrinderMachine> grinderMachines;
	private List<EspressoMachine> espressoMachines;
	private List<SteamerMachine> steamerMachines;

	public BrewTask(List<GrinderMachine> grinderMachines, List<EspressoMachine> espressoMachines,
			List<SteamerMachine> steamerMachines) {
		this.grinderMachines = grinderMachines;
		this.espressoMachines = espressoMachines;
		this.steamerMachines = steamerMachines;
	}

	@Override
	public void run() {
		Instant start = Instant.now();
		brewLatte();
		Instant finish = Instant.now();
		String timeElapsed = CoffeeUtility.timeElapsed(start, finish);
		System.out.println(Thread.currentThread().getName() + " brew took -------->" + timeElapsed);
	}

	public Latte brewLatte() {
		Grounds grounds = grindCoffee(grinderMachines);
		Coffee coffee = makeEspresso(espressoMachines, grounds);
		Milk milk = steamMilk(steamerMachines);
		return makeLatte(coffee, milk);
	}

	private Grounds grindCoffee(List<GrinderMachine> grinderMachines) {
		Instant start = Instant.now();

		GrinderMachine availableGrinder = null;
		Grounds grounds = null;
		try {
			// check if any element in pool is available for lock
			int i = 0;
			while (true) {
				GrinderMachine grinderMachine = grinderMachines.get(i);
				Lock grinderLock = grinderMachine.getGrinderLock();
				if (grinderLock.tryLock(0, TimeUnit.MILLISECONDS)) {
					availableGrinder = grinderMachine;
					break;
				}
				i++;

				// reset counter
				if (i > grinderMachines.size() - 1) {
					i = 0;
				}
			}
			grounds = availableGrinder.grind();
		} catch (Exception e) {
			System.err.println("something went wrong while grinding " + e.getLocalizedMessage());
		} finally {
			availableGrinder.getGrinderLock().unlock();
		}

		Instant finish = Instant.now();
		String timeElapsed = CoffeeUtility.timeElapsed(start, finish);
		System.out.println(Thread.currentThread().getName() + " grindCoffee " + timeElapsed);
		return grounds;
	}

	private Coffee makeEspresso(List<EspressoMachine> espressoMachines, Grounds grounds) {
		Instant start = Instant.now();

		EspressoMachine availableEspresso = null;
		Coffee coffee = null;
		try {
			// check if any element in pool is available for lock
			int i = 0;
			while (true) {
				EspressoMachine espressoMachine = espressoMachines.get(i);
				Lock grinderLock = espressoMachine.getEspressoLock();
				if (grinderLock.tryLock(0, TimeUnit.MILLISECONDS)) {
					availableEspresso = espressoMachine;
					break;
				}
				// reset counter
				if (i > grinderMachines.size() - 1) {
					i = 0;
				}
			}

			coffee = availableEspresso.concentrate();
		} catch (Exception e) {
			System.err.println("something went wrong while espresso" + e.getLocalizedMessage());
		} finally {
			availableEspresso.getEspressoLock().unlock();
		}

		Instant finish = Instant.now();
		String timeElapsed = CoffeeUtility.timeElapsed(start, finish);
		System.out.println(Thread.currentThread().getName() + " makeEspresso " + timeElapsed);
		return coffee;
	}

	private Milk steamMilk(List<SteamerMachine> steamerMachines) {
		Instant start = Instant.now();

		SteamerMachine availableSteamer = null;
		Milk milk = null;
		try {
			// check if any element in pool is available for lock
			int i = 0;
			while (true) {
				SteamerMachine steamerMachine = steamerMachines.get(i);
				Lock steamerLock = steamerMachine.getSteamerLock();
				if (steamerLock.tryLock(0, TimeUnit.MILLISECONDS)) {
					availableSteamer = steamerMachine;
					break;
				}

				// reset counter
				if (i > grinderMachines.size() - 1) {
					i = 0;
				}
			}

			milk = availableSteamer.steam();
		} catch (Exception e) {
			System.err.println("something went wrong while steaming " + e.getLocalizedMessage());
		} finally {
			availableSteamer.getSteamerLock().unlock();
		}

		Instant finish = Instant.now();
		String timeElapsed = CoffeeUtility.timeElapsed(start, finish);
		System.out.println(Thread.currentThread().getName() + " steamMilk " + timeElapsed);
		return milk;
	}

	private Latte makeLatte(Coffee coffee, Milk milk) {
		return CoffeeUtility.mix(coffee, milk);
	}

}
