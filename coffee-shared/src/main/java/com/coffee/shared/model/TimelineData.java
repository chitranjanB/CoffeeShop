package com.coffee.shared.model;

import java.util.Date;

public class TimelineData {
    private String customerId;
    private Date startDate;
    private Date endDate;

    public TimelineData(){
        //empty constructor
    }

    public TimelineData(String customerId, Date startDate, Date endDate) {
        this.customerId = customerId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Date getStartTimestamp() {
        return startDate;
    }

    public void setStartTimestamp(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndTimestamp() {
        return endDate;
    }

    public void setEndTimestamp(Date endDate) {
        this.endDate = endDate;
    }
}