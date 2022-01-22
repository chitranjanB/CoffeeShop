package com.simulation.shop.machine;

import com.simulation.shop.model.Grounds;
import org.springframework.stereotype.Component;

@Component
public class GrinderMachine {

	public Grounds grind() {
		Grounds grounds = null;
		try {
			Thread.sleep(250);
			grounds = new Grounds();
		} catch (InterruptedException e) {
			System.err.println("Something went wrong " + e.getLocalizedMessage());
		}

		return grounds;
	}

}
