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
    protected ChangingStatus changingStatus;

    public AuditableEntity(LocalDateTime createdAt, Long createdBy) {
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.changingStatus = ChangingStatus.NEW;
    }

    public void toUpdate() {
        this.changingStatus = ChangingStatus.UPDATED;
    }

    public void toDelete() {
        this.changingStatus = ChangingStatus.DELETED;
    }

    public void toUnChange() {
        this.changingStatus = ChangingStatus.UNCHANGED;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
}
