package com.application.corporatemanagement.common.framework;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


@Getter
@SuperBuilder
public class AuditableEntity {
    protected LocalDateTime createdAt;
    protected Long createdBy;
    protected LocalDateTime lastUpdatedAt;
    protected Long lastUpdatedBy;

    public AuditableEntity(LocalDateTime createdAt, Long createdBy) {
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }
}
