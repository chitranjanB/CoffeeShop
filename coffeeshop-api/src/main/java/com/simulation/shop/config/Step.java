package com.simulation.shop.config;

public enum Step {
    BREW("brew"), GRIND_COFFEE("grindCoffee"), MAKE_ESPRESSO("makeEspreso"), STEAM_MILK("steaminMilk"),
    MAKE_LATTE("makeLatte");

    private String displayValue;

    private Step(String displayValue) {
        this.displayValue = displayValue;
    }

    @Override
    public String toString() {
        return this.displayValue;
    }

}