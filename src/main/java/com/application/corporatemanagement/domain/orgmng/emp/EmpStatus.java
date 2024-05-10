package com.application.corporatemanagement.domain.orgmng.emp;

public enum EmpStatus {
    REGULAR("1", "正式员工"),

    PROBATION("2", "试用期员工"),

    TERMINATED("3","终止");

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
