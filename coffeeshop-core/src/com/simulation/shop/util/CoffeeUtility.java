package com.simulation.shop.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.simulation.shop.config.Config;
import com.simulation.shop.config.Step;
import com.simulation.shop.model.Coffee;
import com.simulation.shop.model.Latte;
import com.simulation.shop.model.SteamedMilk;
import com.simulation.shop.stats.ApexTimelineChart;
import com.simulation.shop.stats.IStats;

public class CoffeeUtility {

	private static final String COLLECT_APEX_METRIC_FORMAT = "%s::%s::%s::%s";

	private static BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1024);

	private static IStats timelineReporter = new ApexTimelineChart();

	public static int fetchRequiredMachines(int customers) {
		return customers >= Config.MACHINES_LIMIT ? Config.MACHINES_LIMIT : customers;
	}

	public static int fetchCustomerId(String metadata) {
		return Integer.parseInt(metadata.split("-")[1]);
	}

	public static int fetchMachineId() {
		String machineName = Thread.currentThread().getName();
		System.out.println(machineName);
		String[] split = machineName.split("-");
		int machineId = Integer.parseInt(split[split.length - 1]);
		return machineId - 1;
	}

	public static void loadupBeans(int machines, int limit) throws IOException {
		for (int machine = 0; machine < machines; machine++) {
			try (PrintWriter pw = new PrintWriter(String.format(Config.BEANS_INVENTORY, machine))) {
				for (int i = 1; i <= limit; i++) {
					pw.println("**************" + i);
				}
			}
		}
	}

	public static void loadupMilk(int machines, int limit) throws IOException {
		for (int machine = 0; machine < machines; machine++) {
			try (PrintWriter pw = new PrintWriter(String.format(Config.MILK_INVENTORY, machine))) {
				for (int i = 1; i <= limit; i++) {
					pw.println("==============" + i);
				}
			}
		}
	}

	public static void collectApexMetric(String threadName, Step step, Instant startInstant, Instant endInstant) {
		if (isDebuggingEnabled()) {

			String start = ApexUtil.formatApexDate(startInstant);
			String end = ApexUtil.formatApexDate(endInstant);

			queue.add(String.format(COLLECT_APEX_METRIC_FORMAT, threadName, step, start, end));
		}
	}

	public static void benchmarks() {
		if (isDebuggingEnabled()) {
			Map<String, List<String>> map = processStats();
			System.out.println("\n*******************BENCHMARK STATS (Apex Timeline chart)*******************");
			displayApexStats(map);
			queue.clear();
		}

	}

	private static Map<String, List<String>> processStats() {
		Map<String, List<String>> map = new HashMap<>();
		for (String element : queue) {
			String[] split = element.split("::");
			String key = split[0];
			String info = split[1] + "|" + split[2] + "|" + split[3];
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

	private static void displayApexStats(Map<String, List<String>> map) {
		timelineReporter.report(map);
	}

	public static Latte mix(Coffee coffee, SteamedMilk milk) {
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
		System.out.println("-----------------------------------------------------------------------");
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

	public static String buildMetadata(int i) {
		return "customer-" + i;
	}

	public static String buildThreadMeta(String oldMeta, String threadInfo) {
		return oldMeta + "--" + threadInfo;
	}
}
