package com.application.corporatemanagement.adapter.driven.persistence.orgmng;

import com.application.corporatemanagement.common.framework.ChangingStatus;
import com.application.corporatemanagement.domain.orgmng.emp.*;
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

    @Test
    @Transactional
    void should_find_empty() {
        Optional<Emp> optionalEmp = empJdbc.findById(1L);
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
        Emp emp = Emp.builder()
                .tenant(1L)
                .name("emp")
                .status(EmpStatus.REGULAR)
                .orgId(2L)
                .postCodes(List.of(1L, 2L))
                .workExperiences(List.of(WorkExperience.builder()
                        .tenant(1L)
                        .company("company")
                        .startDate(LocalDate.now().minusYears(-1))
                        .endDate(LocalDate.now())
                        .build()))
                .skills(List.of(Skill.builder()
                        .tenant(1L)
                        .skillType(2L)
                        .skillLevel(SkillLevel.ADVANCED)
                        .duration(5L)
                        .build()))
                .changingStatus(ChangingStatus.NEW)
                .createdBy(10010L)
                .lastUpdatedBy(10010L)
                .build();
        empJdbc.save(emp);
        assertTrue(emp.getId() > 0);
        Optional<List<Emp>> optionalEmps = empJdbc.findAll();
        assertTrue(optionalEmps.isPresent());
        assertEquals(1, optionalEmps.get().size());
        Emp insertedEmp = optionalEmps.get().get(0);
        assertEmp(insertedEmp);
        assertSkills(insertedEmp);
        assertWorkExperiences(insertedEmp);
    }

    private void assertWorkExperiences(Emp insertedEmp) {
        assertEquals(1, insertedEmp.getWorkExperiences().size());
        WorkExperience workExperience = insertedEmp.getWorkExperiences().get(0);
        assertTrue(workExperience.getId() > 0);
        assertEquals(1L, workExperience.getTenant());
        assertEquals("company", workExperience.getCompany());
        assertEquals(LocalDate.now().minusYears(-1), workExperience.getStartDate());
        assertEquals(LocalDate.now(), workExperience.getEndDate());
    }

    private void assertSkills(Emp insertedEmp) {
        assertEquals(1, insertedEmp.getSkills().size());
        Skill skill = insertedEmp.getSkills().get(0);
        assertTrue(skill.getId() > 0);
        assertEquals(1L, skill.getTenant());
        assertEquals(2L, skill.getSkillType());
        assertEquals(SkillLevel.ADVANCED, skill.getSkillLevel());
        assertEquals(5L, skill.getDuration());
    }

    private void assertEmp(Emp insertedEmp) {
        assertEquals(1L, insertedEmp.getTenant());
        assertEquals("emp", insertedEmp.getName());
        assertEquals(EmpStatus.REGULAR, insertedEmp.getStatus());
        assertEquals(2L, insertedEmp.getOrgId());
        assertEquals(List.of(1L, 2L), insertedEmp.getPostCodes());
    }
}