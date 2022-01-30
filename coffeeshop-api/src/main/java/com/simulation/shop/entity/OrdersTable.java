package com.simulation.shop.entity;

import com.simulation.shop.model.Status;

import javax.persistence.*;

@Entity
@Table
public class OrdersTable {

    @Id
    @Column
    private String transactionId;
    @Column
    private String customerId;
    @Column
    @Enumerated(EnumType.STRING)
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
