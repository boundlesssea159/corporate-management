package com.application.corporatemanagement.application.orgmng.emp;

import com.application.corporatemanagement.domain.orgmng.emp.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * this is a different way to build domain aggregator (another way is builder,e.g. OrgBuilder)
 * this way will export little domain logic to application layer like calling validator logic , but it is actually simpler
 */
@Service
public class EmpAssembler {

    public Emp fromCreateRequest(AddEmpRequest addEmpRequest) {
        return Emp.builder()
                .tenant(addEmpRequest.tenant)
                .orgId(addEmpRequest.orgId)
                .skills(buildSkills(addEmpRequest))
                .workExperiences(buildWorkExperiences(addEmpRequest))
                .name(addEmpRequest.name)
                .status(EmpStatus.REGULAR)
                .postCodes(addEmpRequest.postCodes)
                .build();
    }

    private List<Skill> buildSkills(AddEmpRequest addEmpRequest) {
        return addEmpRequest.getSkills().stream().map(skill ->
                {
                    Optional<SkillLevel> optionalSkillLevel = SkillLevel.valueOf(skill.skillLevel);
                    return (Skill) optionalSkillLevel.map(skillLevel -> Skill.builder()
                            .tenant(addEmpRequest.tenant)
                            .skillType(skill.skillType)
                            .skillLevel(skillLevel)
                            .duration(skill.duration)
                            .build()).orElse(null);
                }
        ).toList();
    }

    private List<WorkExperience> buildWorkExperiences(AddEmpRequest addEmpRequest) {
        return addEmpRequest.getWorkExperiences().stream().map(workExperience ->
                (WorkExperience) WorkExperience.builder()
                        .tenant(addEmpRequest.tenant)
                        .company(workExperience.company)
                        .startDate(workExperience.startDate)
                        .endDate(workExperience.endDate)
                        .build()
        ).toList();
    }
}
