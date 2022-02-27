package com.coffee.shared.model;

import com.coffee.shared.entity.StepTransactionId;

import java.time.Instant;

public class Coffee {
    private int tempInC;
    private int coffeeVolumeInML;

    private String machineName;
    private String customerId;
    private Instant start;
    private Instant end;

    public Coffee() {
        this.tempInC = 120;
        this.coffeeVolumeInML = 20;
    }

    public Coffee(int tempInC, int coffeeVolumeInML) {
        this.tempInC = tempInC;
        this.coffeeVolumeInML = coffeeVolumeInML;
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

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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
        return "Coffee{" +
                "tempInC=" + tempInC +
                ", coffeeVolumeInML=" + coffeeVolumeInML +
                ", machineName='" + machineName + '\'' +
                ", customerId='" + customerId + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}