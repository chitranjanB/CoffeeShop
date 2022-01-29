package com.simulation.shop.model;

public class Latte {

    private Coffee coffee;
    private SteamedMilk milk;

    public Latte(Coffee coffee, SteamedMilk milk) {
        this.coffee = coffee;
        this.milk = milk;
    }

    public Coffee getCoffee() {
        return coffee;
    }

    public void setCoffee(Coffee coffee) {
        this.coffee = coffee;
    }

    public SteamedMilk getMilk() {
        return milk;
    }

    public void setMilk(SteamedMilk milk) {
        this.milk = milk;
    }

    @Override
    public String toString() {
        return "Latte [coffee=" + coffee + ", milk=" + milk + "]";
    }

}