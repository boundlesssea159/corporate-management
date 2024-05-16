package com.application.corporatemanagement.application.orgmng.emp;

import com.application.corporatemanagement.domain.orgmng.emp.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmpAssemblerTest {

    private final AddEmpRequestParam addEmpRequestParam = new AddEmpRequestParam();
    protected EmpAssembler empAssembler;

    @BeforeEach
    void setUp() {
        empAssembler = new EmpAssembler();
    }

    static class AddEmpRequestParam {
        Long tenant = 1L;
        String name = "emp";
        Long orgId = 2L;
        List<Long> postCodes = List.of(1L);
        List<AddEmpRequest.Skill> skills = List.of(AddEmpRequest.Skill.builder()
                .skillType(SkillType.JAVA.getValue())
                .skillLevel(SkillLevel.ADVANCED.getValue())
                .duration(5L)
                .build());

        List<AddEmpRequest.WorkExperience> workExperiences = List.of(
                AddEmpRequest.WorkExperience.builder()
                        .startDate(LocalDate.now().minusYears(5))
                        .endDate(LocalDate.now())
                        .company("company")
                        .build());
    }

    @Test
    void should_create_emp() {
        AddEmpRequest addEmpRequest = buildAddEmpRequest();
        Emp createdEmp = empAssembler.fromCreateRequest(addEmpRequest);
        assertEquals(addEmpRequest.getName(), createdEmp.getName());
        assertEquals(addEmpRequestParam.orgId, createdEmp.getOrgId());
        assertEquals(EmpStatus.REGULAR, createdEmp.getStatus());
        assertSkills(createdEmp);
        assertWorkExperiences(createdEmp);
        assertArrayEquals(addEmpRequestParam.postCodes.toArray(new Long[0]), createdEmp.getPostCodes().toArray(new Long[0]));
    }

    private AddEmpRequest buildAddEmpRequest() {
        return AddEmpRequest.builder()
                .tenant(addEmpRequestParam.tenant)
                .name(addEmpRequestParam.name)
                .orgId(addEmpRequestParam.orgId)
                .skills(addEmpRequestParam.skills)
                .workExperiences(addEmpRequestParam.workExperiences)
                .postCodes(addEmpRequestParam.postCodes)
                .build();
    }

    private void assertSkills(Emp emp) {
        assertEquals(addEmpRequestParam.skills.size(), emp.getSkills().size());
        emp.getSkills().forEach(skill -> {
            AddEmpRequest.Skill expected = addEmpRequestParam.skills.stream().findFirst().get();
            assertEquals(expected.getSkillType(), skill.getSkillType());
            assertEquals(expected.getSkillLevel(), skill.getSkillLevel().getValue());
            assertEquals(expected.getDuration(), skill.getDuration());
        });
    }

    private void assertWorkExperiences(Emp emp) {
        assertEquals(addEmpRequestParam.workExperiences.size(), emp.getWorkExperiences().size());
        emp.getWorkExperiences().forEach(workExperience -> {
            AddEmpRequest.WorkExperience expected = addEmpRequestParam.workExperiences.stream().findFirst().get();
            assertEquals(expected.getStartDate(), workExperience.getStartDate());
            assertEquals(expected.getEndDate(), workExperience.getEndDate());
            assertEquals(expected.getCompany(), workExperience.getCompany());
        });
    }
}