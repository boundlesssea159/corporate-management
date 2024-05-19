package com.application.corporatemanagement.domain.orgmng.emp;

import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

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
        emp.addWorkExperience(WorkExperience.builder()
                .tenant(1L)
                .startDate(LocalDate.now().minusYears(-5))
                .endDate(LocalDate.now().minusYears(1))
                .company("company")
                .build());
        assertThrows(BusinessException.class, () -> {
            emp.addWorkExperience(WorkExperience.builder()
                    .tenant(1L)
                    .startDate(LocalDate.now().minusYears(3))
                    .endDate(LocalDate.now().minusYears(2))
                    .company("company")
                    .build());
        });
    }
}