package com.application.corporatemanagement.application.orgmng.emp;

import com.application.corporatemanagement.domain.orgmng.emp.*;
import com.application.corporatemanagement.domain.orgmng.org.validators.OrgValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * this is a different way to build domain aggregator (another way is builder,e.g. OrgBuilder)
 * this way will export little domain logic to application layer like calling validator logic , but it is actually simpler
 */
@Service
public class EmpAssembler {

    private final OrgValidator orgValidator;

    @Autowired
    public EmpAssembler(OrgValidator orgValidator) {
        this.orgValidator = orgValidator;
    }

    public Emp fromCreateRequest(AddEmpRequest addEmpRequest, Long userId) {
        this.orgValidator.check(addEmpRequest.getTenant(), addEmpRequest.getOrgId());
        Emp emp = Emp.builder()
                .tenant(addEmpRequest.tenant)
                .orgId(addEmpRequest.orgId)
                .name(addEmpRequest.name)
                .status(EmpStatus.REGULAR)
                .postCodes(addEmpRequest.postCodes)
                .build();
        buildSkills(emp, addEmpRequest, userId);
        buildWorkExperiences(emp, addEmpRequest, userId);
        return emp;
    }

    private void buildSkills(Emp emp, AddEmpRequest addEmpRequest, Long userId) {
        addEmpRequest.getSkills().forEach(skill -> emp.addSkill(skill.getSkillType(), skill.getSkillLevel(), skill.getDuration(), userId));
    }

    private void buildWorkExperiences(Emp emp, AddEmpRequest addEmpRequest, Long userId) {
        addEmpRequest.getWorkExperiences().forEach(workExperience -> emp.addWorkExperience(workExperience.getStartDate(), workExperience.getEndDate(), workExperience.getCompany(), userId));
    }
}
