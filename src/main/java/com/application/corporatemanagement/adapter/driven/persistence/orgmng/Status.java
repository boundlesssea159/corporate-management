package com.application.corporatemanagement.adapter.driven.persistence.orgmng;

public enum Status {
    Effective("1");

    private final String value;

    Status(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
