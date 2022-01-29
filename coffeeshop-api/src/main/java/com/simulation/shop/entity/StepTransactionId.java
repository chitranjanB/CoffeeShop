package com.simulation.shop.entity;

import com.simulation.shop.config.Step;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class StepTransactionId implements Serializable {
    private Step step;
    private String transactionId;

    public StepTransactionId() {

    }

    public StepTransactionId(String transactionId, Step step) {
        this.transactionId = transactionId;
        this.step = step;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTransactionId that = (StepTransactionId) o;
        return step == that.step &&
                Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(step, transactionId);
    }
}