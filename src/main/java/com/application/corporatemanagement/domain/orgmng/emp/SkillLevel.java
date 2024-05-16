package com.application.corporatemanagement.domain.orgmng.emp;

import java.util.Arrays;
import java.util.Optional;

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

    public static Optional<SkillLevel> valueOf(Long id) {
        return Arrays.stream(SkillLevel.values()).filter(skillLevel -> skillLevel.value.equals(id)).findFirst();
    }
}
