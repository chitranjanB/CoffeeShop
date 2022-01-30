package com.simulation.shop.entity;

import com.simulation.shop.model.Status;

import javax.persistence.*;

@Entity
@Table
public class MilkStock {

    @Id
    @Column
    private String stockId;

    @Column
    private String milk;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getMilk() {
        return milk;
    }

    public void setMilk(String milk) {
        this.milk = milk;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
