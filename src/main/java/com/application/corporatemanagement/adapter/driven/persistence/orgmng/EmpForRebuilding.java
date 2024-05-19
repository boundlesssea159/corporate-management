package com.application.corporatemanagement.adapter.driven.persistence.orgmng;

import com.application.corporatemanagement.domain.orgmng.emp.Emp;
import com.application.corporatemanagement.domain.orgmng.emp.Skill;
import com.application.corporatemanagement.domain.orgmng.emp.WorkExperience;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;

@SuperBuilder
public class EmpForRebuilding extends Emp {
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
}
