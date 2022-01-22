package com.simulation.shop.stats;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


@SuppressWarnings("unchecked")
public class ApexTimelineChart implements IStats {

	private static final String FORMAT = "node=%s, start=%s, end=%s";
	private static final String APEX_DATE_RENDER = "new Date(%s).getTime()";

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
				String step = metricElement[0];
				String start = metricElement[1];
				String end = metricElement[2];

				String row = String.format(FORMAT, customer_worker_key, start, end);

				if ("grindCoffee".equals(step)) {
					grindBeans.add(row);
				} else if ("makeEspreso".equals(step)) {
					makeEspresso.add(row);
				} else if ("steaminMilk".equals(step)) {
					steamMilk.add(row);
				}

			}
		}

		String report = buildJsonReport(grindBeans, makeEspresso, steamMilk);
		String sanitizedReport = sanitizeReport(report);

		System.out.println(sanitizedReport);
	}

	private String buildJsonReport(List<String> grindBeans, List<String> makeEspresso, List<String> steamMilk) {
		JSONArray seriesArr = new JSONArray();

		JSONObject grindJson = processJson(grindBeans, "Grind Beans");
		JSONObject espressJson = processJson(makeEspresso, "Brew Espresso");
		JSONObject steamJson = processJson(steamMilk, "Steam Milk");

		seriesArr.add(grindJson);
		seriesArr.add(espressJson);
		seriesArr.add(steamJson);

		return seriesArr.toJSONString();
	}

	private String sanitizeReport(String report) {
		return report.replace("\"new Date(", "new Date(").replace(".getTime()\"", ".getTime()");
	}

	private JSONObject processJson(List<String> sampleList, String name) {

		JSONArray dataArr = new JSONArray();

		JSONObject seriesElement = new JSONObject();
		seriesElement.put("name", name);

		for (String row : sampleList) {

			String[] rowSplit = row.split(", ");
			String node = extractValue(rowSplit[0]);
			String start = extractValue(rowSplit[1]);
			String end = extractValue(rowSplit[2]);

			JSONObject dataElement = new JSONObject();
			dataElement.put("x", node);

			JSONArray yArr = new JSONArray();
			yArr.add(String.format(APEX_DATE_RENDER, start));
			yArr.add(String.format(APEX_DATE_RENDER, end));

			dataElement.put("y", yArr);

			dataArr.add(dataElement);
		}
		seriesElement.put("data", dataArr);
		return seriesElement;

	}

	// Given node=cust1, it returns cust1
	private String extractValue(String key_value) {
		return key_value.split("=")[1];
	}

}
