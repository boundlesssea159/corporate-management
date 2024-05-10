package com.application.corporatemanagement.domain.orgmng.org;

import com.application.corporatemanagement.common.framework.AuditableEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@Builder
public class Org {
// todo extend AuditableEntity

    private Long id;
    private Long tenant;
    private Long superior;
    private String orgType;
    private Long leader;
    private String name;
    private OrgStatus status;
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime lastUpdatedAt;
    private Long lastUpdatedBy;

    public Org id(Long id) {
        this.id = id;
        return this;
    }

    public Org name(String name) {
        this.name = name;
        return this;
    }


    public Org superior(Long superior) {
        this.superior = superior;
        return this;
    }

    public Org updator(Long updator) {
        this.lastUpdatedBy = updator;
        return this;
    }

    public Org updatedAt(LocalDateTime dateTime) {
        this.lastUpdatedAt = dateTime;
        return this;
    }


    public void cancel() {
        this.status = OrgStatus.CANCELED;
    }

    public boolean isEffective() {
        return status.equals(OrgStatus.EFFECTIVE);
    }
}
