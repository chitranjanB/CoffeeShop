package com.simulation.shop.model;

public class Grounds {

    private String coffeeBeans;
    private int coffeeBeansInGms;

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

    @Override
    public String toString() {
        return "Grounds [coffeeBeans=" + coffeeBeans + ", coffeeBeansInGms=" + coffeeBeansInGms + "]";
    }
}