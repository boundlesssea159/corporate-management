package com.application.corporatemanagement.adapter.driven.persistence.orgmng;

public enum Status {
    EFFECTIVE("1", "有效");
    private final String value;

    private final String text;

    Status(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }
}