package com.simulation.shop.config;

import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class CoffeeShopRuntime {
    private LocalTime shopOpenTimestamp;

    public LocalTime getShopOpenTimestamp() {
        return shopOpenTimestamp;
    }

    public void setShopOpenTimestamp(LocalTime shopOpenTimestamp) {
        this.shopOpenTimestamp = shopOpenTimestamp;
    }

}