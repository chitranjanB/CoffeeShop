package com.coffee.shared.model;

public enum Status {
    PENDING("Pending"), COMPLETE("Complete"), ERROR("Error");

    private String displayValue;

    private Status(String displayValue) {
        this.displayValue = displayValue;
    }

    @Override
    public String toString() {
        return this.displayValue;
    }
}
