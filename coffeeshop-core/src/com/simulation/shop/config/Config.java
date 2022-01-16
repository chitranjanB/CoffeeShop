package com.simulation.shop.config;

public class Config {
	public static final String BEANS_INVENTORY = "resources/beans%s.txt";
	public static final String MILK_INVENTORY = "resources/milk%s.txt";
	public static final String COFFEE_INFO = "data.txt";

	public static final String CUSTOMER_PREFIX ="customer-%s";
	public static final String COFFEE_FORMAT ="resources/coffee-%s.zip";

	public static final String GRINDER_PREFIX = "grindermachine";
	public static final String ESPRESSO_PREFIX = "espressomachine";
	public static final String STEAMER_PREFIX = "steamermachine";
	public static final String MACHINEID_FORMAT = "%s-%s";

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
	public static final String DATA_FORMAT = "Customer ID: %s, " +
			"Grinding Machine Name: %s, " +
			"Espresso Machine Name: %s, " +
			"Steaming Machine Name: %s";
}