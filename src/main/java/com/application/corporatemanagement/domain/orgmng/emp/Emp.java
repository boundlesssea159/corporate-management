package com.application.corporatemanagement.domain.orgmng.emp;

import com.application.corporatemanagement.common.framework.AuditableEntity;
import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import lombok.Getter;
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
        if (this.skills == null) {
            this.skills = new ArrayList<>();
        }
        skillShouldNotBeDuplicated(skill);
        this.skills.add(skill);
    }

    private void skillShouldNotBeDuplicated(Skill skill) {
        if (this.skills.stream().anyMatch(skl -> skl.getSkillType().equals(skill.getSkillType()))) {
            throw new BusinessException("同一个技能不能录入两次");
        }
    }

    public void addWorkExperience(WorkExperience workExperience) {
        if (this.workExperiences == null) {
            this.workExperiences = new ArrayList<>();
        }
        workExperienceTimeShouldNotOverlap(workExperience);
        this.workExperiences.add(workExperience);
    }

    private void workExperienceTimeShouldNotOverlap(WorkExperience workExperience) {
        this.workExperiences.forEach(we -> {
            if (we.getStartDate().isBefore(workExperience.getEndDate()) ||
                    workExperience.getStartDate().isBefore(we.getEndDate())) {
                throw new BusinessException("工作经验时间不能重叠");
            }
        });
    }

    public void addPostCode(Long postCode) {
        this.postCodes.add(postCode);
    }
}
