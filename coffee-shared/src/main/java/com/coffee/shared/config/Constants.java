package com.coffee.shared.config;


public class Constants {

    private Constants() {
        throw new IllegalArgumentException("Constant File");
    }

    public static final String COFFEE_JAR_ENTRY = "data.txt";
    public static final String GRINDER_PREFIX = "grindermachine";
    public static final String ESPRESSO_PREFIX = "espressomachine";
    public static final String STEAMER_PREFIX = "steamermachine";
    public static final String MACHINEID_FORMAT = "%s-%s";

    // +- Jitter gets added randomly to each step
    public static final int JITTER = 1000;

    public static final String DATA_FORMAT = "Customer ID: %s, " +
            "Grinding Machine Name: %s, Thread: %s, Time taken: %s | " +
            "Espresso Machine Name: %s, Thread: %s, Time taken: %s | " +
            "Steaming Machine Name: %s, Thread: %s, Time taken: %s ";
}