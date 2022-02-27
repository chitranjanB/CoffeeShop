package com.coffee.shared.model;

import java.time.Instant;

public class SteamedMilk {
    private String rawMilk;
    private int tempInC;
    private int milkVolumeInML;

    private String machineName;
    private String customerId;
    private Instant start;
    private Instant end;

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
        return "SteamedMilk{" +
                "rawMilk='" + rawMilk + '\'' +
                ", tempInC=" + tempInC +
                ", milkVolumeInML=" + milkVolumeInML +
                ", machineName='" + machineName + '\'' +
                ", customerId='" + customerId + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}