package com.application.corporatemanagement.domain.orgmng.emp;

import com.application.corporatemanagement.common.framework.AuditableEntity;
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

    private LocalDate startDate;

    private LocalDate endDate;

    private String company;

    WorkExperience(Long tenant, LocalDate startDate, LocalDate endDate, String company, LocalDateTime createdAt, Long createdBy) {
        super(createdAt, createdBy);
        this.tenant = tenant;
        this.startDate = startDate;
        this.endDate = endDate;
        this.company = company;
    }
}
