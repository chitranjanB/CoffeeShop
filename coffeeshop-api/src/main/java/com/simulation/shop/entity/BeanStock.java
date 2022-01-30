package com.simulation.shop.entity;

import com.simulation.shop.model.Status;

import javax.persistence.*;

@Entity
@Table
public class BeanStock {

    @Id
    @Column
    private String stockId;

    @Column
    private String beans;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getBeans() {
        return beans;
    }

    public void setBeans(String beans) {
        this.beans = beans;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
