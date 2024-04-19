package com.application.corporatemanagement.domain.orgmng;

import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import com.application.corporatemanagement.domain.orgmng.emp.EmpRepository;
import com.application.corporatemanagement.domain.orgmng.emp.EmpStatus;
import com.application.corporatemanagement.domain.orgmng.org.validators.OrgLeaderValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrgLeaderValidatorTest {
    @Test
    void should_throw_exception_if_leader_not_on_working() {
        EmpRepository empRepository = mock(EmpRepository.class);
        when(empRepository.existsByIdAndStatus(1L, 2L, EmpStatus.REGULAR, EmpStatus.PROBATION)).thenReturn(false);
        OrgLeaderValidator orgLeaderValidator = new OrgLeaderValidator(empRepository);
        assertThrows(BusinessException.class, () -> orgLeaderValidator.orgLeaderShouldBeOnWorking(1L, 2L));
    }
}