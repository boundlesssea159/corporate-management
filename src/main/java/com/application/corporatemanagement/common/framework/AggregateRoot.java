package com.application.corporatemanagement.common.framework;

import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
public class AggregateRoot extends AuditableEntity {

    protected Long version;

    public AggregateRoot(LocalDateTime createdAt, Long createdBy) {
        super(createdAt, createdBy);
    }

    public Long getVersion() {
        return version;
    }
}
