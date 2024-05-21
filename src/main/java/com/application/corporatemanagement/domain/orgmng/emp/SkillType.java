package com.application.corporatemanagement.domain.orgmng.emp;

public enum SkillType {

    JAVA(1L, "JAVA");
    private final Long value;

    private final String text;

    SkillType(Long value, String text) {
        this.value = value;
        this.text = text;
    }
    public Long getValue() {
        return value;
    }
}
