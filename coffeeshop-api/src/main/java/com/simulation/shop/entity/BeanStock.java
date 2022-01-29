package com.simulation.shop.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class GrinderStock {

    @Id
    @Column
    private Integer beanQty;

    public Integer getBeanQty() {
        return beanQty;
    }

    public void setBeanQty(Integer beanQty) {
        this.beanQty = beanQty;
    }
}
