package com.application.corporatemanagement.domain.orgmng.emp;

import com.application.corporatemanagement.common.framework.AuditableEntity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Emp extends AuditableEntity {

    private Long id;

    private String name;

    private Long tenant;

    private Long orgId; // associate external object by id

    private ArrayList<Long> postCodes; // any emp can work on several posts

    private List<Skill> skills; // associate inner object with real object

    private List<WorkExperience> workExperiences;

    private EmpStatus status;

    public Emp(LocalDateTime createdAt, Long createdBy) {
        super(createdAt, createdBy);
    }

    public void becomeRegular() {
        status = EmpStatus.REGULAR;
    }

    public void terminate() {
        status = EmpStatus.TERMINATED;
    }
}
