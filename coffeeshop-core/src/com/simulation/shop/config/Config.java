package com.simulation.shop.config;

public class Config {
	public static final int CUSTOMERS = 4;
	public static final int MULTI_SHARED_INSTANCE = 4;
	public static final int STEP_PROCESSING_TIME = 250;

	// +- Jitter gets added randomly to each step
	public static final int JITTER = 0;

	public static final boolean IS_DEBUG_ENABLED = true;
	public static final String STATS_FORMAT = "Load:%s, Max:%sms, Avg:%sms, Total:%s, Samples(ms): %s";
}