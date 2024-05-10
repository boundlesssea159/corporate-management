package com.application.corporatemanagement.domain.orgmng.emp;

import com.application.corporatemanagement.common.framework.AuditableEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Skill extends AuditableEntity {

    private Long id;

    private Long tenant;

    private Long skillType;

    private SkillLevel skillLevel; // todo 目前文章里面对它单独设计了一张表，实际感觉不用这么做，看后续

    private Long duration;

    // todo 将构造函数设置为包级别私有的目的：不让包外面直接创建它，尽可能引导应用程序通过聚合根或聚合服务创建它，从而尽可能保证相关的业务逻辑不被绕过
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
