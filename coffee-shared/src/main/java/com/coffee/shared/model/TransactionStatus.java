package com.coffee.shared.model;

public class TransactionStatus {
    private String transactionId;
    private Status status;

    public TransactionStatus() {
    }

    public TransactionStatus(String transactionId) {
        this.transactionId = transactionId;
        this.status = Status.PENDING;
    }

    public TransactionStatus(String transactionId, Status status) {
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
