package com.simulation.shop.model;

public class Coffee {
    private int tempInC;
    private int coffeeVolumeInML;

    public Coffee() {
        this.tempInC = 120;
        this.coffeeVolumeInML = 20;
    }

    public int getTempInC() {
        return tempInC;
    }

    public void setTempInC(int tempInC) {
        this.tempInC = tempInC;
    }

    public int getCoffeeVolumeInML() {
        return coffeeVolumeInML;
    }

    public void setCoffeeVolumeInML(int coffeeVolumeInML) {
        this.coffeeVolumeInML = coffeeVolumeInML;
    }

    @Override
    public String toString() {
        return "Coffee [tempInC=" + tempInC + ", coffeeVolumeInML=" + coffeeVolumeInML + "]";
    }

}