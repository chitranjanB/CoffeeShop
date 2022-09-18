package com.simulation.shop.service;

import com.coffee.shared.entity.AuditLog;

public class OrderTimeline {
    private AuditLog grindCoffeeAuditLog;
    private AuditLog makeEspressoAuditLog;
    private AuditLog steamMilkAuditLog;

    public AuditLog getGrindCoffeeAuditLog() {
        return grindCoffeeAuditLog;
    }

    public void setGrindCoffeeAuditLog(AuditLog grindCoffeeAuditLog) {
        this.grindCoffeeAuditLog = grindCoffeeAuditLog;
    }

    public AuditLog getMakeEspressoAuditLog() {
        return makeEspressoAuditLog;
    }

    public void setMakeEspressoAuditLog(AuditLog makeEspressoAuditLog) {
        this.makeEspressoAuditLog = makeEspressoAuditLog;
    }

    public AuditLog getSteamMilkAuditLog() {
        return steamMilkAuditLog;
    }

    public void setSteamMilkAuditLog(AuditLog steamMilkAuditLog) {
        this.steamMilkAuditLog = steamMilkAuditLog;
    }
}
