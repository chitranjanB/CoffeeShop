package com.coffee.shared.model;

import java.time.Instant;

public class Grounds {

    private String coffeeBeans;
    private int coffeeBeansInGms;

    private String customerId;
    private String machineName;
    private Instant start;
    private Instant end;

    public Grounds() {

    }

    public Grounds(String beans) {
        this.coffeeBeans = beans;
        this.coffeeBeansInGms = beans.length();
    }

    public String getCoffeeBeans() {
        return coffeeBeans;
    }

    public void setCoffeeBeans(String coffeeBeans) {
        this.coffeeBeans = coffeeBeans;
    }

    public int getCoffeeBeansInGms() {
        return coffeeBeansInGms;
    }

    public void setCoffeeBeansInGms(int coffeeBeansInGms) {
        this.coffeeBeansInGms = coffeeBeansInGms;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public Instant getStart() {
        return start;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Instant getEnd() {
        return end;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Grounds{" +
                "coffeeBeans='" + coffeeBeans + '\'' +
                ", coffeeBeansInGms=" + coffeeBeansInGms +
                ", customerId='" + customerId + '\'' +
                ", machineName='" + machineName + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}