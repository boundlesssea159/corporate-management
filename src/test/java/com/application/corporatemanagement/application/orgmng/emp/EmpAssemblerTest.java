package com.application.corporatemanagement.application.orgmng.emp;

import com.application.corporatemanagement.domain.orgmng.emp.*;
import com.application.corporatemanagement.domain.orgmng.org.validators.OrgValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;


class EmpAssemblerTest {

    private final AddEmpRequestParam addEmpRequestParam = new AddEmpRequestParam();
    protected EmpAssembler empAssembler;
    protected OrgValidator orgValidator;

    @BeforeEach
    void setUp() {
        orgValidator = Mockito.mock(OrgValidator.class);
        empAssembler = new EmpAssembler(orgValidator);
    }

    static class AddEmpRequestParam {
        Long tenant = 1L;
        String name = "emp";
        Long orgId = 2L;
        List<Long> postCodes = List.of(1L);
        List<SkillRequest> skills = List.of(SkillRequest.builder()
                .skillType(SkillType.JAVA.getValue())
                .skillLevel(SkillLevel.ADVANCED.getValue())
                .duration(5L)
                .build());

        List<WorkExperienceRequest> workExperiences = List.of(
                WorkExperienceRequest.builder()
                        .startDate(LocalDate.now().minusYears(5))
                        .endDate(LocalDate.now())
                        .company("company")
                        .build());
    }

    @Test
    void should_create_emp() {
        AddEmpRequest addEmpRequest = buildAddEmpRequest();
        Emp createdEmp = empAssembler.fromCreateRequest(addEmpRequest, 10010L);
        verify(orgValidator).check(addEmpRequestParam.tenant, addEmpRequestParam.orgId);
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
            SkillRequest expected = addEmpRequestParam.skills.stream().findFirst().get();
            assertEquals(expected.getSkillType(), skill.getSkillType());
            assertEquals(expected.getSkillLevel(), skill.getSkillLevel().getValue());
            assertEquals(expected.getDuration(), skill.getDuration());
        });
    }

    private void assertWorkExperiences(Emp emp) {
        assertEquals(addEmpRequestParam.workExperiences.size(), emp.getWorkExperiences().size());
        emp.getWorkExperiences().forEach(workExperience -> {
            WorkExperienceRequest expected = addEmpRequestParam.workExperiences.stream().findFirst().get();
            assertEquals(expected.getStartDate(), workExperience.getStartDate());
            assertEquals(expected.getEndDate(), workExperience.getEndDate());
            assertEquals(expected.getCompany(), workExperience.getCompany());
        });
    }
}