package com.application.corporatemanagement.domain.tenantmng;

public enum TenantStatus {

    EFFECTIVE("1", "有效");
    private final String value;

    private final String text;

    TenantStatus(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }
}
