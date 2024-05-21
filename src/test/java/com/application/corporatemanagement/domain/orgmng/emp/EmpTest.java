package com.application.corporatemanagement.domain.orgmng.emp;

import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EmpTest {
    @Test
    void should_throw_exception_if_add_same_skill() {
        Emp emp = Emp.builder().build();
        emp.addSkill(1L, 1L, 1L, 10010L);
        assertThrows(BusinessException.class, () -> emp.addSkill(1L, 2L, 4L, 10010L));
    }

    @Test
    void should_throw_exception_if_skill_level_is_invalid() {
        Emp emp = Emp.builder().build();
        assertThrows(BusinessException.class, () -> emp.addSkill(1L, 5L, 4L, 10010L));
    }

    @Test
    void should_throw_exception_if_add_work_experience_with_time_overlap() {
        Emp emp = Emp.builder().build();
        emp.addWorkExperience(LocalDate.now().minusYears(5), LocalDate.now().minusYears(1), "company", 10010L);
        assertThrows(BusinessException.class, () -> emp.addWorkExperience(LocalDate.now().minusYears(3), LocalDate.now().minusYears(2), "company", 10010L));
    }

    @Test
    void should_throw_exception_if_emp_has_no_skills() {
        Emp emp = Emp.builder().build();
        assertThrows(BusinessException.class, () -> emp.updateSkill(1L, 2L, 5L, 10020L));
    }

    @Test
    void should_throw_exception_if_updated_skill_is_not_exist() {
        Emp emp = Emp.builder().build();
        emp.addSkill(1L, 2L, 5L, 10010L);
        assertThrows(BusinessException.class, () -> emp.updateSkill(2L, 2L, 5L, 10020L));
    }

    @Test
    void should_throw_exception_if_skill_level_is_not_defined() {
        Emp emp = Emp.builder().build();
        emp.addSkill(1L, 2L, 5L, 10010L);
        assertThrows(BusinessException.class, () -> emp.updateSkill(1L, 5L, 5L, 10020L));
    }

    @Test
    void should_modify_nothing_if_all_fields_is_same() {
        Emp emp = Emp.builder().build();
        emp.addSkill(1L, 2L, 5L, 10010L);
        emp.updateSkill(1L, 2L, 5L, 10020L);
        Optional<Skill> optionalSkill = emp.getSkills().stream().filter(skill -> skill.getSkillType().equals(1L)).findFirst();
        assertTrue(optionalSkill.isPresent());
        Skill skill = optionalSkill.get();
        assertEquals(2L, skill.getSkillLevel().getValue());
        assertEquals(5L, skill.getDuration());
    }

    @Test
    void should_throw_exception_if_become_regular_from_not_probation() {
        Emp emp = Emp.builder().status(EmpStatus.REGULAR).build();
        assertThrows(BusinessException.class, emp::becomeRegular);
    }

    @Test
    void should_become_regular_from_probation() {
        Emp emp = Emp.builder().status(EmpStatus.PROBATION).build();
        emp.becomeRegular();
        assertEquals(EmpStatus.REGULAR, emp.getStatus());
    }

    @Test
    void should_throw_exception_if_become_terminated_from_terminated() {
        Emp emp = Emp.builder().status(EmpStatus.TERMINATED).build();
        assertThrows(BusinessException.class, emp::terminate);
    }

    @Test
    void should_become_terminated_from_not_terminated() {
        Emp emp = Emp.builder().status(EmpStatus.REGULAR).build();
        emp.terminate();
        assertEquals(EmpStatus.TERMINATED, emp.getStatus());
    }
}