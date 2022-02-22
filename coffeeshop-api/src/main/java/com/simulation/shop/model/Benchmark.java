package com.simulation.shop.model;

import com.simulation.shop.config.Step;

import java.util.List;

public class Benchmark {
    private List<Data> data;
    private Step name;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public Step getName() {
        return name;
    }

    public void setName(Step name) {
        this.name = name;
    }
}