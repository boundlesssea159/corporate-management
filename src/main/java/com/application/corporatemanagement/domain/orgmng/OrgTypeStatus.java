package com.application.corporatemanagement.domain.orgmng;

public enum OrgTypeStatus {
    EFFECTIVE("1", "有效");
    private final String value;

    private final String text;

    OrgTypeStatus(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }
}
