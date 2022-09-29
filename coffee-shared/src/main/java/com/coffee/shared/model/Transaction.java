package com.coffee.shared.model;

import java.util.Date;

public class Transaction {
    private String transactionId;
    private String customerId;
    private Date timeStarted;
    private Date timeEnded;
    private Status status;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Date getTimeStarted() {
        return timeStarted;
    }

    public void setTimeStarted(Date timeStarted) {
        this.timeStarted = timeStarted;
    }

    public Date getTimeEnded() {
        return timeEnded;
    }

    public void setTimeEnded(Date timeEnded) {
        this.timeEnded = timeEnded;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
