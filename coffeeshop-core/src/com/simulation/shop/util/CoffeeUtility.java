package com.simulation.shop.util;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.simulation.shop.config.CoffeeShopConstant;
import com.simulation.shop.config.Step;
import com.simulation.shop.model.Coffee;
import com.simulation.shop.model.Latte;
import com.simulation.shop.model.Milk;

public class CoffeeUtility {

	private static final String COLLECT_METRIC_FORMAT = "%s::%s::%s";
	private static BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1024);

	public static void collectMetric(String threadName, Step step, String timeElapsed) {
		queue.add(String.format(COLLECT_METRIC_FORMAT, threadName, step, timeElapsed));
	}

	public static void stats() {
		try {
			Map<String, List<String>> map = processStats();
			displayStats(map);
		} finally {
			queue.clear();
		}
	}

	private static Map<String, List<String>> processStats() {
		Map<String, List<String>> map = new HashMap<>();
		for (String element : queue) {
			String[] split = element.split("::");
			String key = split[0];
			String info = split[1] + ":" + split[2];
			List<String> value = null;
			if (map.containsKey(key)) {
				value = map.get(key);
				value.add(info);
			} else {
				value = new ArrayList<String>();
				value.add(info);
				map.put(key, value);
			}

		}
		return map;
	}

	private static void displayStats(Map<String, List<String>> map) {
		Set<String> keys = map.keySet();

		for (String key : keys) {
			List<String> info = map.get(key);
			System.out.println(key + " :: " + info);
		}
	}

	public static Latte mix(Coffee coffee, Milk milk) {
		return new Latte(coffee, milk);
	}

	public static String timeElapsed(Instant start, Instant finish) {
		String unit = "ms";
		long timeElapsed = Duration.between(start, finish).toMillis();
		return timeElapsed + unit;
	}

	public static int buildStepTimeWithJitter() {
		Random random = new Random();
		int result = 0;
		int jitter = random.nextInt(CoffeeShopConstant.JITTER);

		if (random.nextBoolean()) {
			result = CoffeeShopConstant.STEP_PROCESSING_TIME + jitter;
		} else {
			result = CoffeeShopConstant.STEP_PROCESSING_TIME - jitter;
		}
		return result;
	}

}
