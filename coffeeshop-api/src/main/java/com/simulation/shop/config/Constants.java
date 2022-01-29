package com.simulation.shop.config;

public class Constants {

    private Constants() {
        throw new IllegalArgumentException("Constant File");
    }

    public static final String BEANS_INVENTORY = "target/beans%s.txt";
    public static final String MILK_INVENTORY = "target/milk%s.txt";
    public static final String COFFEE_JAR_ENTRY = "data.txt";

    public static final String CUSTOMER_PREFIX = "customer-%s";
    public static final String COFFEE_FORMAT = "target/coffee-%s.zip";

    public static final String GRINDER_PREFIX = "grindermachine";
    public static final String ESPRESSO_PREFIX = "espressomachine";
    public static final String STEAMER_PREFIX = "steamermachine";
    public static final String MACHINEID_FORMAT = "%s-%s";

    // +- Jitter gets added randomly to each step
    public static final int JITTER = 0;

    public static final boolean IS_DEBUG_ENABLED = true;
    public static final String STATS_FORMAT = "Load:%s, Max:%sms, Avg:%sms, Total:%s, Samples(ms): %s";
    public static final String DATA_FORMAT = "Customer ID: %s, " +
            "Grinding Machine Name: %s, " +
            "Espresso Machine Name: %s, " +
            "Steaming Machine Name: %s";
}