package com.simulation.shop.util;

import java.time.Duration;
import java.time.Instant;

import com.simulation.shop.model.Coffee;
import com.simulation.shop.model.Latte;
import com.simulation.shop.model.Milk;

public class CoffeeUtility {

	public static Latte mix(Coffee coffee, Milk milk) {
		return new Latte(coffee, milk);
	}

	public static String timeElapsed(Instant start, Instant finish) {
		String unit = "ms";
		long timeElapsed = Duration.between(start, finish).toMillis();
		return timeElapsed + unit;
	}

}
