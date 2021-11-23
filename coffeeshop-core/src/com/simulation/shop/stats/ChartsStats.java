package com.simulation.shop.stats;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChartsStats implements IStats {

	@Override
	public void report(Map<String, List<String>> data) {
		Set<String> keys = data.keySet();

		for (String key : keys) {
			List<String> info = data.get(key);
			
			
		}

	}

}

/**
const data = [
              {
                name: "Grind Beans",
                worker1: 332,
                worker2: 261,
                worker3: 265,
              },
              {
                name: "Make Espresso",
                worker1: 255,
                worker2: 265,
                worker3: 264,
              },
              {
                name: "Steam Milk",
                worker1: 263,
                worker2: 262,
                worker3: 262,
              },
            ];

*/