package com.simulation.shop.util;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.simulation.shop.config.Config;
import com.simulation.shop.config.Step;
import com.simulation.shop.model.Coffee;
import com.simulation.shop.model.Latte;
import com.simulation.shop.model.Milk;

public class CoffeeUtility {

	private static final String COLLECT_METRIC_FORMAT = "%s::%s::%s::%s";
	private static BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1024);

	public static void collectMetric(String threadName, Step step, String timeElapsed) {
		if (isDebuggingEnabled()) {
			queue.add(String.format(COLLECT_METRIC_FORMAT, threadName, step, timeElapsed, LocalTime.now()));
		}
	}

	public static void benchmarks() {
		if (isDebuggingEnabled()) {
			Map<String, List<String>> map = processStats();
			displayStats(map);
			queue.clear();
		}

	}

	private static Map<String, List<String>> processStats() {
		Map<String, List<String>> map = new HashMap<>();
		for (String element : queue) {
			String[] split = element.split("::");
			String key = split[0];
			String info = split[1] + ":" + split[2] + " (" + split[3] + ")";
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
		int jitter = Config.JITTER;
		jitter = jitter > 0 ? random.nextInt(jitter) : jitter;

		if (random.nextBoolean()) {
			result = Config.STEP_PROCESSING_TIME + jitter;
		} else {
			result = Config.STEP_PROCESSING_TIME - jitter;
		}
		return result;
	}

	public static boolean isDebuggingEnabled() {
		return Config.IS_DEBUG_ENABLED;
	}

	public static long stats(List<Long> samples) {
		long timePerLatte = 0;
		long maxBrewTime = 0;
		long totalBrewTime = totalBrewTime(samples);
		for (Long e : samples) {

			timePerLatte = Long.sum(timePerLatte, e);
			if (e > maxBrewTime) {
				maxBrewTime = e;
			}
		}
		timePerLatte = timePerLatte / samples.size();

		System.out.println(
				String.format(Config.STATS_FORMAT, samples.size(), maxBrewTime, timePerLatte, totalBrewTime, samples));
		System.out.println(
				"-----------------------------------------------------------------------");
		System.out.println();
		return timePerLatte;

	}

	private static long totalBrewTime(List<Long> samples) {
		long totalBrewTime = 0;
		for (Long e : samples) {
			totalBrewTime = Long.sum(totalBrewTime, e);
		}
		return totalBrewTime;
	}

}
