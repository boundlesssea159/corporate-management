package com.application.corporatemanagement.adapter.driven.persistence.orgmng;

import com.application.corporatemanagement.common.framework.ChangingStatus;
import com.application.corporatemanagement.domain.common.valueobjs.Duration;
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

        Long userId = 10010L;

        WorkExperience workExperience = WorkExperience.builder()
                .tenant(1L)
                .company("company")
                .duration(new Duration(LocalDate.now().minusYears(1), LocalDate.now()))
                .createdBy(userId)
                .lastUpdatedBy(userId)
                .build();

        Skill skill = Skill.builder()
                .tenant(1L)
                .skillType(2L)
                .skillLevel(SkillLevel.ADVANCED)
                .duration(5L)
                .createdBy(userId)
                .lastUpdatedBy(userId)
                .build();

        ChangingStatus changingStatus = ChangingStatus.NEW;

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
        empJdbc.create(emp);
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
                .version(System.currentTimeMillis())
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
        long version = System.currentTimeMillis();
        Emp newEmp = newEmp(version);
        empJdbc.create(newEmp);
        Emp updatingEmp = updatingEmp(newEmp.getId(), version);
        empJdbc.update(updatingEmp);
        Optional<Emp> optionalEmp = empJdbc.findById(updatingEmp.getTenant(), updatingEmp.getId());
        assertTrue(optionalEmp.isPresent());
        Emp emp = optionalEmp.get();
        // assert emp
        assertEquals("updatedName", emp.getName());
        assertEquals(EmpStatus.TERMINATED, emp.getStatus());
        assertEquals(5L, emp.getOrgId());
        assertArrayEquals(List.of(3L, 4L).toArray(), emp.getPostCodes().toArray());
        // assert skills
        assertEquals(2, emp.getSkills().size());
        assertTrue(emp.getSkills().stream().noneMatch(skill -> skill.getSkillType().equals(4L)));
        emp.getSkills().forEach(skill -> {
            if (skill.getSkillType().equals(2L)) {
                assertEquals(SkillLevel.SENIOR, skill.getSkillLevel());
                assertEquals(10L, skill.getDuration());
            } else {
                assertEquals(3L, skill.getSkillType());
                assertEquals(SkillLevel.ADVANCED, skill.getSkillLevel());
                assertEquals(1L, skill.getDuration());
            }
        });
        // assert work experiences
        assertEquals(2, emp.getWorkExperiences().size());
        assertTrue(emp.getWorkExperiences().stream().noneMatch(workExperience -> workExperience.getCompany().equals("company3")));
        emp.getWorkExperiences().forEach(workExperience -> {
            if (workExperience.getCompany().equals("company")) {
                assertEquals(LocalDate.now().minusYears(5), workExperience.getStartDate());
                assertEquals(LocalDate.now().minusYears(1), workExperience.getEndDate());
            } else {
                assertEquals("company2", workExperience.getCompany());
                assertEquals(LocalDate.now().minusYears(5), workExperience.getStartDate());
                assertEquals(LocalDate.now(), workExperience.getEndDate());
            }
        });
    }

    private Emp newEmp(long version) {
        return Emp.builder()
                .tenant(empParameter.tenant)
                .name(empParameter.name)
                .status(empParameter.empStatus)
                .orgId(empParameter.orgId)
                .postCodes(empParameter.postCodes)
                .workExperiences(List.of(empParameter.workExperience, WorkExperience.builder()
                        .tenant(empParameter.tenant)
                        .company("company3")
                        .duration(new Duration(LocalDate.now().minusYears(5), LocalDate.now()))
                        .changingStatus(ChangingStatus.NEW)
                        .createdBy(empParameter.userId)
                        .lastUpdatedBy(empParameter.userId)
                        .build()))
                .skills(List.of(empParameter.skill, Skill.builder()
                        .tenant(empParameter.tenant)
                        .skillType(4L)
                        .skillLevel(SkillLevel.SENIOR)
                        .duration(10L)
                        .changingStatus(ChangingStatus.NEW)
                        .createdBy(empParameter.userId)
                        .lastUpdatedBy(empParameter.userId)
                        .build()))
                .changingStatus(empParameter.changingStatus)
                .createdBy(empParameter.userId)
                .lastUpdatedBy(empParameter.userId)
                .version(version)
                .build();
    }

    private Emp updatingEmp(Long empId, long version) {
        return Emp.builder()
                .id(empId)
                .tenant(empParameter.tenant)
                .name("updatedName")
                .status(EmpStatus.TERMINATED)
                .orgId(5L)
                .postCodes(List.of(3L, 4L))
                .changingStatus(ChangingStatus.UPDATED)
                .createdBy(empParameter.userId)
                .lastUpdatedBy(empParameter.userId)
                .skills(List.of(
                        // update
                        Skill.builder()
                                .tenant(empParameter.tenant)
                                .skillType(empParameter.skill.getSkillType())
                                .skillLevel(SkillLevel.SENIOR)
                                .duration(10L)
                                .changingStatus(ChangingStatus.UPDATED)
                                .lastUpdatedBy(empParameter.userId)
                                .build()
                        // new
                        , Skill.builder()
                                .tenant(empParameter.tenant)
                                .skillType(3L)
                                .skillLevel(SkillLevel.ADVANCED)
                                .duration(1L)
                                .changingStatus(ChangingStatus.NEW)
                                .createdBy(empParameter.userId)
                                .lastUpdatedBy(empParameter.userId)
                                .build()
                        ,// delete
                        Skill.builder()
                                .tenant(empParameter.tenant)
                                .skillType(4L)
                                .changingStatus(ChangingStatus.DELETED)
                                .build()))
                .workExperiences(List.of(
                        // update
                        WorkExperience.builder()
                                .tenant(empParameter.tenant)
                                .company(empParameter.workExperience.getCompany())
                                .duration(new Duration(LocalDate.now().minusYears(5), LocalDate.now().minusYears(1)))
                                .changingStatus(ChangingStatus.UPDATED)
                                .lastUpdatedBy(empParameter.userId)
                                .build()
                        // new
                        , WorkExperience.builder()
                                .tenant(empParameter.tenant)
                                .company("company2")
                                .duration(new Duration(LocalDate.now().minusYears(5), LocalDate.now()))
                                .changingStatus(ChangingStatus.NEW)
                                .createdBy(empParameter.userId)
                                .lastUpdatedBy(empParameter.userId)
                                .build()
                        ,// delete
                        WorkExperience.builder()
                                .tenant(empParameter.tenant)
                                .company("company3")
                                .changingStatus(ChangingStatus.DELETED)
                                .build()
                ))
                .version(version)
                .build();
    }
}