package com.application.corporatemanagement.domain.orgmng;

public enum EmpStatus {
    REGULAR("1", "正式员工"),

    PROBATION("2", "试用期员工");

    private final String value;

    private final String text;

    EmpStatus(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }
}
