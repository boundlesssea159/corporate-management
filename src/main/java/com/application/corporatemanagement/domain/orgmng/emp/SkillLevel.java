package com.application.corporatemanagement.domain.orgmng.emp;

import java.util.Arrays;
import java.util.Optional;

public enum SkillLevel {
    PRIMARY(1L, "PRIMARY"),
    ADVANCED(2L, "ADVANCED"),
    SENIOR(3L, "SENIOR");
    private final Long value;

    private final String text;

    SkillLevel(Long value, String text) {
        this.value = value;
        this.text = text;
    }
    public Long getValue() {
        return value;
    }

    public static Optional<SkillLevel> valueOf(Long id) {
        return Arrays.stream(SkillLevel.values()).filter(skillLevel -> skillLevel.value.equals(id)).findFirst();
    }
}
