package com.simulation.shop.stats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConsoleStats implements IStats {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleStats.class);

	@Override
	public void report(Map<String, List<String>> data) {
		Set<String> keys = data.keySet();

		for (String key : keys) {
			List<String> info = data.get(key);
			LOGGER.info(key + "::" + info);
		}
	}
}
