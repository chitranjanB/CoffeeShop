package com.simulation.shop.model;

public class OrderStatus {
    private String transactionId;
    private Status status;

    public OrderStatus() {
    }

    public OrderStatus(String transactionId) {
        this.transactionId = transactionId;
        this.status = Status.PENDING;
    }

    public OrderStatus(String transactionId, Status status) {
        this.transactionId = transactionId;
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
