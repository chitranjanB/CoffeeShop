package com.coffee.shared.entity;

import javax.persistence.*;

@Entity
@Table
public class CoffeeEntity {
    @Id
    @Column
    private String transactionId;

    @Lob
    private byte[] data;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
