package com.simulation.shop.service;

public class MachineBenchmark {

    private String transactionId;
    private long timeTakenByGrinderMachine;
    private long timeTakenByEspressoMachine;
    private long timeTakenBySteamerMachine;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public long getTimeTakenByGrinderMachine() {
        return timeTakenByGrinderMachine;
    }

    public void setTimeTakenByGrinderMachine(long timeTakenByGrinderMachine) {
        this.timeTakenByGrinderMachine = timeTakenByGrinderMachine;
    }

    public long getTimeTakenByEspressoMachine() {
        return timeTakenByEspressoMachine;
    }

    public void setTimeTakenByEspressoMachine(long timeTakenByEspressoMachine) {
        this.timeTakenByEspressoMachine = timeTakenByEspressoMachine;
    }

    public long getTimeTakenBySteamerMachine() {
        return timeTakenBySteamerMachine;
    }

    public void setTimeTakenBySteamerMachine(long timeTakenBySteamerMachine) {
        this.timeTakenBySteamerMachine = timeTakenBySteamerMachine;
    }

    @Override
    public String toString() {
        return "MachineBenchmark{" +
                "transactionId='" + transactionId + '\'' +
                ", timeTakenByGrinderMachine=" + timeTakenByGrinderMachine +
                ", timeTakenByEspressoMachine=" + timeTakenByEspressoMachine +
                ", timeTakenBySteamerMachine=" + timeTakenBySteamerMachine +
                '}';
    }
}
