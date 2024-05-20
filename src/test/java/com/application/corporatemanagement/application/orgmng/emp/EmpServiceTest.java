package com.application.corporatemanagement.application.orgmng.emp;

import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import com.application.corporatemanagement.domain.orgmng.emp.Emp;
import com.application.corporatemanagement.domain.orgmng.emp.Skill;
import com.application.corporatemanagement.domain.orgmng.emp.SkillLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;


class EmpServiceTest {
    protected EmpRepository empRepository;
    protected EmpAssembler empAssembler;
    private EmpService empService;

    @BeforeEach
    void setUp() {
        empRepository = Mockito.mock(EmpRepository.class);
        empAssembler = Mockito.mock(EmpAssembler.class);
        empService = new EmpService(empRepository, empAssembler);
    }

    @Test
    void should_add_emp() {
        AddEmpRequest request = AddEmpRequest.builder().build();
        empService.addEmp(request, 10010L);
        verify(empAssembler).fromCreateRequest(request, 10010L);
        verify(empRepository).save(any(), anyLong());
    }


    @Nested
    class UpdateSkill {

        private final Long tenantId = 1L;

        private final Long empId = 1L;

        private final Long userId = 10010L;

        private final Long skillType = 1L;

        @Test
        void should_update_skill() {
            Skill skill = Skill.builder().skillType(skillType).skillLevel(SkillLevel.PRIMARY).duration(5L).build();
            Emp emp = Emp.builder().skills(List.of(skill)).build();
            when(empRepository.findById(tenantId, empId)).thenReturn(Optional.of(emp));
            UpdateSkillRequest request = buildUpdateSkillRequest(skillType, 2L, 10L);
            boolean result = empService.updateSkill(request, userId);
            assertTrue(result);
            assertEquals(SkillLevel.ADVANCED, skill.getSkillLevel());
            assertEquals(10L, skill.getDuration());
        }

        private UpdateSkillRequest buildUpdateSkillRequest(long skillType, long skillLevel, long duration) {
            return UpdateSkillRequest.builder()
                    .tenant(tenantId)
                    .empId(empId)
                    .skills(List.of(
                            SkillRequest.builder()
                                    .skillType(skillType)
                                    .skillLevel(skillLevel)
                                    .duration(duration)
                                    .build()))
                    .build();
        }

        @Test
        void should_throw_exception_if_emp_is_not_exist() {
            when(empRepository.findById(tenantId, empId)).thenReturn(Optional.empty());
            assertThrows(BusinessException.class, () -> {
                UpdateSkillRequest request = UpdateSkillRequest.builder()
                        .tenant(tenantId)
                        .empId(empId)
                        .build();
                empService.updateSkill(request, userId);
            });
        }

        @Test
        void should_throw_exception_if_skill_is_not_exist() {
            Emp emp = Emp.builder().skills(List.of(Skill.builder().skillType(2L).skillLevel(SkillLevel.PRIMARY).duration(5L).build())).build();
            when(empRepository.findById(tenantId, empId)).thenReturn(Optional.of(emp));
            UpdateSkillRequest request = buildUpdateSkillRequest(skillType, 2L, 10L);
            assertThrows(BusinessException.class, () -> empService.updateSkill(request, userId));
        }

        @Test
        void should_throw_exception_if_skill_level_is_not_defined() {
            Emp emp = Emp.builder().skills(List.of(Skill.builder().skillType(skillType).skillLevel(SkillLevel.PRIMARY).duration(5L).build())).build();
            when(empRepository.findById(tenantId, empId)).thenReturn(Optional.of(emp));
            UpdateSkillRequest request = buildUpdateSkillRequest(skillType, 5L, 10L);
            assertThrows(BusinessException.class, () -> empService.updateSkill(request, userId));
        }
    }


    // todo add transaction to keep atomic
}