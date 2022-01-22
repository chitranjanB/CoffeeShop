package com.simulation.shop.stats;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConsoleStats implements IStats {

	@Override
	public void report(Map<String, List<String>> data) {
		Set<String> keys = data.keySet();

		for (String key : keys) {
			List<String> info = data.get(key);
			System.out.println(key + "::" + info);
		}
	}
}
