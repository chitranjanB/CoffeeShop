package com.simulation.shop.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class MilkStock {

    @Id
    @Column
    private Integer stockId;
    
    @Column
    private String milk;

}
