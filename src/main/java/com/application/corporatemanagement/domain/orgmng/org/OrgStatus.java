package com.application.corporatemanagement.domain.orgmng.org;

public enum OrgStatus {
    EFFECTIVE("1", "EFFECTIVE");
    private final String value;

    private final String text;

    OrgStatus(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
