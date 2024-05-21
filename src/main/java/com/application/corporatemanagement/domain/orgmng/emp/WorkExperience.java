package com.application.corporatemanagement.domain.orgmng.emp;

import com.application.corporatemanagement.common.framework.AuditableEntity;
import com.application.corporatemanagement.domain.common.valueobjs.Duration;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class WorkExperience extends AuditableEntity {
    private Long id;

    private Long tenant;

    private Duration duration;

    private String company;

    WorkExperience(Long tenant, LocalDate startDate, LocalDate endDate, String company, LocalDateTime createdAt, Long createdBy) {
        super(createdAt, createdBy);
        this.tenant = tenant;
        this.duration = new Duration(startDate, endDate);
        this.company = company;
    }

    public LocalDate getStartDate() {
        return this.duration.getStartDate();
    }

    public LocalDate getEndDate() {
        return this.duration.getEndDate();
    }
}
