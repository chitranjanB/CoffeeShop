package com.coffee.shared.model;

public class SteamedMilk {
    private String rawMilk;
    private int tempInC;
    private int milkVolumeInML;

    public SteamedMilk() {

    }

    public SteamedMilk(String rawMilk) {
        this.rawMilk = rawMilk;
        this.tempInC = 130;
        this.milkVolumeInML = 150;
    }

    public String getRawMilk() {
        return rawMilk;
    }

    public void setRawMilk(String rawMilk) {
        this.rawMilk = rawMilk;
    }

    public int getTempInC() {
        return tempInC;
    }

    public void setTempInC(int tempInC) {
        this.tempInC = tempInC;
    }

    public int getMilkVolumeInML() {
        return milkVolumeInML;
    }

    public void setMilkVolumeInML(int milkVolumeInML) {
        this.milkVolumeInML = milkVolumeInML;
    }

    @Override
    public String toString() {
        return "SteamedMilk [rawMilk=" + rawMilk + ", tempInC=" + tempInC + ", milkVolumeInML=" + milkVolumeInML + "]";
    }

}