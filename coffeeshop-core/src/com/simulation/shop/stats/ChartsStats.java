package com.simulation.shop.stats;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ChartsStats implements IStats {

	@Override
	public void report(Map<String, List<String>> data) {

		List<String> grindBeans = new ArrayList<String>();
		List<String> makeEspresso = new ArrayList<String>();
		List<String> steamMilk = new ArrayList<String>();

		Set<String> keys = data.keySet();

		for (String customer_worker_key : keys) {
			List<String> stats = data.get(customer_worker_key);

			for (String metric : stats) {
				String[] metricElement = metric.split("\\|");
				if ("grindCoffee".equals(metricElement[0])) {
					grindBeans.add(customer_worker_key + "==" + metricElement[1]);
				} else if ("makeEspreso".equals(metricElement[0])) {
					makeEspresso.add(customer_worker_key + "==" + metricElement[1]);
				} else if ("steaminMilk".equals(metricElement[0])) {
					steamMilk.add(customer_worker_key + "==" + metricElement[1]);
				}

			}
		}

		JSONArray jsonarr = new JSONArray();

		JSONObject grindJson = processJson(grindBeans, "Grind Beans");
		JSONObject espressoJson = processJson(makeEspresso, "Make Espresso");
		JSONObject steamJson = processJson(steamMilk, "Steam Milk");

		jsonarr.add(grindJson);
		jsonarr.add(espressoJson);
		jsonarr.add(steamJson);

		System.out.println(jsonarr);

	}

	private JSONObject processJson(List<String> list, String title) {
		JSONObject json = new JSONObject();
		json.put("name", title);

		for (String value : list) {
			String[] cust = value.split("==");
			json.put(cust[0], extractValue(cust[1]));
		}
		return json;

	}

	// given 252ms, return 252
	private Integer extractValue(String timeElapsed) {
		return Integer.parseInt(timeElapsed.substring(0, timeElapsed.length() - 2));
	}

}
