package com.application.corporatemanagement.application.orgmng;

import com.application.corporatemanagement.domain.orgmng.emp.Emp;
import com.application.corporatemanagement.domain.orgmng.emp.SkillLevel;
import com.application.corporatemanagement.domain.orgmng.emp.SkillType;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class EmpServiceTest {
    private EmpService empService;

    private final AddEmpRequestParam addEmpRequestParam = new AddEmpRequestParam();

    @BeforeEach
    void setUp() {
        empService = new EmpService();
    }

    @Getter
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

    // 不应该有单独的添加skill的用例，应该放到修改员工这个用例里面，因为员工及其关联对象是一个原子整体，应该对员工这个聚合对象进行操作
//    @Test
//    void should_add_skill_to_emp() {
//        AddSkillRequest addSkillRequest = AddSkillRequest.builder()
//                .tenant(1L)
//                .empId(2L)
//                .skills(List.of(AddSkillRequest.Skill.builder()
//                        .skillType(SkillType.JAVA.getText())
//                        .skillLevel(SkillLevel.ADVANCED.getText())
//                        .duration(5L)
//                        .build()))
//                .build();
//        Optional<Emp> optionalEmp = empService.addSkill(addSkillRequest);
//        assertTrue(optionalEmp.isPresent());
//        Emp emp = optionalEmp.get();
//
////        assertEquals(SkillType.JAVA.getValue(), emp.getSkillType());
////        assertEquals(SkillLevel.ADVANCED, emp.getSkillLevel());
////        assertEquals(5L, emp.getDuration());
//
//    }

    // todo 整体把握一个大方向即可：尽可能让业务逻辑校验不要绕过聚合根或聚合服务
    @Test
    void should_add_emp() {
        AddEmpRequest addEmpRequest = buildAddEmpRequest();
        Optional<Emp> optionalEmp = empService.addEmp(addEmpRequest, 10010L);
        assertTrue(optionalEmp.isPresent());
        Emp emp = optionalEmp.get();
        assertEquals(addEmpRequest.getName(), emp.getName());
        assertEquals(addEmpRequestParam.getOrgId(), emp.getOrgId());
        assertSkills(emp);
        assertWorkExperiences(emp);
        assertArrayEquals(addEmpRequestParam.getPostCodes().toArray(new Long[0]), emp.getPostCodes().toArray(new Long[0]));
    }

    private AddEmpRequest buildAddEmpRequest() {
        return AddEmpRequest.builder()
                .tenant(addEmpRequestParam.getTenant())
                .name(addEmpRequestParam.name)
                .orgId(addEmpRequestParam.orgId)
                .skills(addEmpRequestParam.getSkills())
                .workExperiences(addEmpRequestParam.getWorkExperiences())
                .postCodes(addEmpRequestParam.getPostCodes())
                .build();
    }

    private void assertSkills(Emp emp) {
        assertEquals(1, emp.getSkills().size());
        emp.getSkills().forEach(skill -> {
            AddEmpRequest.Skill expected = addEmpRequestParam.getSkills().stream().findFirst().get();
            assertEquals(expected.getSkillType(), skill.getSkillType());
            assertEquals(expected.getSkillLevel(), skill.getSkillLevel().getValue());
            assertEquals(expected.getDuration(), skill.getDuration());
        });
    }

    private void assertWorkExperiences(Emp emp) {
        assertEquals(1, emp.getWorkExperiences().size());
        emp.getWorkExperiences().forEach(workExperience -> {
            AddEmpRequest.WorkExperience expected = addEmpRequestParam.getWorkExperiences().stream().findFirst().get();
            assertEquals(expected.getStartDate(), workExperience.getStartDate());
            assertEquals(expected.getEndDate(), workExperience.getEndDate());
            assertEquals(expected.getCompany(), workExperience.getCompany());
        });
    }


    // todo update emp
}