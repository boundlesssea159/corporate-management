package com.application.corporatemanagement.domain.orgmng.emp;

import com.application.corporatemanagement.common.framework.AuditableEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class Skill extends AuditableEntity {

    private Long id;

    private Long tenant;

    private Long skillType;

    private SkillLevel skillLevel; // todo 目前文章里面对它单独设计了一张表，实际感觉不用这么做，看后续

    private Long duration;

    Skill(Long tenant, Long skillType, LocalDateTime createdAt, Long createdBy) {
        super(createdAt, createdBy);
        this.tenant = tenant;
        this.skillType = skillType;
    }

    void setLevel(SkillLevel level) {
        this.skillLevel = level;
    }

    void setDuration(Long duration) {
        this.duration = duration;
    }
}
