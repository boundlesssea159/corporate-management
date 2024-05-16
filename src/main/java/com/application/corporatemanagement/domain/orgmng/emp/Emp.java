package com.application.corporatemanagement.domain.orgmng.emp;

import com.application.corporatemanagement.common.framework.AuditableEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
public class Emp extends AuditableEntity {

    private Long id;

    private String name;

    private Long tenant;

    private Long orgId; // associate external object by id

    private List<Long> postCodes; // any emp can work on several posts

    private List<Skill> skills; // associate inner object with real object

    private List<WorkExperience> workExperiences;

    private EmpStatus status;

    public void becomeRegular() {
        status = EmpStatus.REGULAR;
    }

    public void terminate() {
        status = EmpStatus.TERMINATED;
    }


    public void addSkill(Skill skill) {
        this.skills.add(skill);
    }

    public void addWorkExperience(WorkExperience workExperience) {
        this.workExperiences.add(workExperience);
    }

    public void addPostCode(Long postCode) {
        this.postCodes.add(postCode);
    }
}
