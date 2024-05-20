package com.application.corporatemanagement.adapter.driven.persistence.orgmng;

import com.application.corporatemanagement.common.framework.ChangingStatus;
import com.application.corporatemanagement.domain.orgmng.emp.*;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmpJdbcTest {

    @Autowired
    private EmpJdbc empJdbc;

    @Spy
    private JdbcTemplate jdbcTemplate;


    private final EmpParameter empParameter = new EmpParameter();

    @Getter
    static class EmpParameter {
        Long tenant = 1L;
        String name = "emp";

        EmpStatus empStatus = EmpStatus.REGULAR;

        Long orgId = 2L;

        List<Long> postCodes = List.of(1L, 2L);

        WorkExperience workExperience = WorkExperience.builder()
                .tenant(1L)
                .company("company")
                .startDate(LocalDate.now().minusYears(-1))
                .endDate(LocalDate.now())
                .build();

        Skill skill = Skill.builder()
                .tenant(1L)
                .skillType(2L)
                .skillLevel(SkillLevel.ADVANCED)
                .duration(5L)
                .build();

        ChangingStatus changingStatus = ChangingStatus.NEW;

        Long userId = 10010L;
    }

    @Test
    void should_find_empty() {
        Optional<Emp> optionalEmp = empJdbc.findById(empParameter.tenant, 1L);
        assertTrue(optionalEmp.isEmpty());
    }

    @Test
    @Transactional
    void should_find_empty_with_find_all() {
        Optional<List<Emp>> optionalEmps = empJdbc.findAll();
        assertEquals(0, optionalEmps.get().size());
    }

    @Test
    @Transactional
    void should_save_emp() {
        Emp emp = buildEmp();
        empJdbc.save(emp);
        assertTrue(emp.getId() > 0);
        Optional<Emp> optionalEmp = empJdbc.findById(empParameter.tenant, emp.getId());
        assertTrue(optionalEmp.isPresent());
        Emp insertedEmp = optionalEmp.get();
        assertEmp(insertedEmp);
        assertSkills(insertedEmp);
        assertWorkExperiences(insertedEmp);
    }

    private Emp buildEmp() {
        return Emp.builder()
                .tenant(empParameter.tenant)
                .name(empParameter.name)
                .status(empParameter.empStatus)
                .orgId(empParameter.orgId)
                .postCodes(empParameter.postCodes)
                .workExperiences(List.of(empParameter.workExperience))
                .skills(List.of(empParameter.skill))
                .changingStatus(empParameter.changingStatus)
                .createdBy(empParameter.userId)
                .lastUpdatedBy(empParameter.userId)
                .build();
    }

    private void assertWorkExperiences(Emp insertedEmp) {
        assertEquals(1, insertedEmp.getWorkExperiences().size());
        WorkExperience workExperience = insertedEmp.getWorkExperiences().get(0);
        assertTrue(workExperience.getId() > 0);
        assertEquals(empParameter.tenant, workExperience.getTenant());
        assertEquals(empParameter.workExperience.getCompany(), workExperience.getCompany());
        assertEquals(empParameter.workExperience.getStartDate(), workExperience.getStartDate());
        assertEquals(empParameter.workExperience.getEndDate(), workExperience.getEndDate());
    }

    private void assertSkills(Emp insertedEmp) {
        assertEquals(1, insertedEmp.getSkills().size());
        Skill skill = insertedEmp.getSkills().get(0);
        assertTrue(skill.getId() > 0);
        assertEquals(empParameter.tenant, skill.getTenant());
        assertEquals(empParameter.skill.getSkillType(), skill.getSkillType());
        assertEquals(empParameter.skill.getSkillLevel(), skill.getSkillLevel());
        assertEquals(empParameter.skill.getDuration(), skill.getDuration());
    }

    private void assertEmp(Emp insertedEmp) {
        assertEquals(empParameter.tenant, insertedEmp.getTenant());
        assertEquals(empParameter.name, insertedEmp.getName());
        assertEquals(empParameter.empStatus, insertedEmp.getStatus());
        assertEquals(empParameter.orgId, insertedEmp.getOrgId());
        assertEquals(empParameter.postCodes, insertedEmp.getPostCodes());
    }

    @Test
    @Transactional
    void should_update_emp() {

    }
}