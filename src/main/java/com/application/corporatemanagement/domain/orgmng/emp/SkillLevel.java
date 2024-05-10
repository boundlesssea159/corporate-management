package com.application.corporatemanagement.domain.orgmng.emp;

public enum SkillLevel {

    ADVANCED(1L, "ADVANCED");
    private final Long value;

    private final String text;

    SkillLevel(Long value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public Long getValue() {
        return value;
    }
}
