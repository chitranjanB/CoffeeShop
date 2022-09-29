package com.coffee.shared.entity;

import com.coffee.shared.model.Status;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class TransactionSequence {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column
    private String transactionId;
    @Column
    private String customerId;
    @Column
    private Date timeStarted;
    @Column
    private Date timeEnded;
    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(cascade = CascadeType.ALL)
    private CoffeeOrder order;

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

    public CoffeeOrder getOrder() {
        return order;
    }

    public void setOrder(CoffeeOrder order) {
        this.order = order;
    }
}
