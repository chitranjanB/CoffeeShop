package com.simulation.shop.config;

public class Config {
	public static final String BEANS_INVENTORY = "resources/beans%s.txt";
	public static final String MILK_INVENTORY = "resources/milk%s.txt";

	public static final int BEANS_INVENTORY_LIMIT = 10;
	public static final int MILK_INVENTORY_LIMIT = 10;
	
	public static final int MACHINES_LIMIT =4;
	public static final int CUSTOMERS = 4;
	
	public static final int MULTI_SHARED_INSTANCE = 4;
	public static final int STEP_PROCESSING_TIME = 250;

	// +- Jitter gets added randomly to each step
	public static final int JITTER = 0;

	public static final boolean IS_DEBUG_ENABLED = true;
	public static final String STATS_FORMAT = "Load:%s, Max:%sms, Avg:%sms, Total:%s, Samples(ms): %s";
}