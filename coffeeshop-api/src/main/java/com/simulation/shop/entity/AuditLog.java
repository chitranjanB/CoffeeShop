package com.simulation.shop.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class AuditLog {

    @EmbeddedId
    private StepTransactionId stepTransactionId;

    private String customerId;
    private String machineName;
    private String threadName;
    private long timeElapsed;

    public AuditLog() {

    }

    public AuditLog(StepTransactionId stepTransactionId) {
        this.stepTransactionId = stepTransactionId;
    }

    public StepTransactionId getStepTransactionId() {
        return stepTransactionId;
    }

    public void setStepTransactionId(StepTransactionId stepTransactionId) {
        this.stepTransactionId = stepTransactionId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(long timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditLog auditLog = (AuditLog) o;
        return timeElapsed == auditLog.timeElapsed &&
                stepTransactionId.equals(auditLog.stepTransactionId) &&
                customerId.equals(auditLog.customerId) &&
                machineName.equals(auditLog.machineName) &&
                threadName.equals(auditLog.threadName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stepTransactionId, customerId, machineName, threadName, timeElapsed);
    }
}
