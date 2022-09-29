package com.coffee.shared.model;

import java.util.Date;
import java.util.List;

public class CoffeeOrder {
    private String orderId;
    private String customerId;
    private Date orderCreatedDate;
    private Date orderEndedDate;
    private Status status;
    private List<Transaction> transactions;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Date getOrderCreatedDate() {
        return orderCreatedDate;
    }

    public void setOrderCreatedDate(Date orderCreatedDate) {
        this.orderCreatedDate = orderCreatedDate;
    }

    public Date getOrderEndedDate() {
        return orderEndedDate;
    }

    public void setOrderEndedDate(Date orderEndedDate) {
        this.orderEndedDate = orderEndedDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
