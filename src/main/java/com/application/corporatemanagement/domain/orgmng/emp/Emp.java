package com.application.corporatemanagement.domain.orgmng.emp;

import com.application.corporatemanagement.common.framework.AuditableEntity;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public String convertPostCodesToString() {
        return postCodes.stream().map(Object::toString).collect(Collectors.joining(","));
    }

    public void becomeRegular() {
        status = EmpStatus.REGULAR;
    }

    public void terminate() {
        status = EmpStatus.TERMINATED;
    }


    public void addSkill(Skill skill) {
        if (this.skills == null) {
            this.skills = new ArrayList<>();
        }
        this.skills.add(skill);
    }

    public void addWorkExperience(WorkExperience workExperience) {
        if (this.workExperiences == null) {
            this.workExperiences = new ArrayList<>();
        }
        this.workExperiences.add(workExperience);
    }

    public void addPostCode(Long postCode) {
        this.postCodes.add(postCode);
    }
}
