package com.coffee.shared.entity;

import com.coffee.shared.model.Status;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table
public class CoffeeOrder {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column
    private String orderId;
    @Column
    private String customerId;
    @Column
    private Date orderCreatedDate;
    @Column
    private Date orderEndedDate;
    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<TransactionSequence> transactionSequences;

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

    public List<TransactionSequence> getTransactionSequences() {
        return transactionSequences;
    }

    public void setTransactionSequences(List<TransactionSequence> transactionSequences) {
        this.transactionSequences = transactionSequences;
    }
}
