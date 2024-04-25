package com.application.corporatemanagement.domain.orgmng.org.validators;

import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import com.application.corporatemanagement.domain.orgmng.emp.EmpRepository;
import com.application.corporatemanagement.domain.orgmng.org.Org;
import com.application.corporatemanagement.domain.orgmng.org.OrgStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CancelValidatorTest {

    protected EmpRepository empRepository;
    private CancelValidator cancelValidator;

    @BeforeEach
    void setUp() {
        empRepository = Mockito.mock(EmpRepository.class);
        cancelValidator = new CancelValidator(empRepository);
    }

    @Test
    void should_throw_exception_if_org_is_invalid() {
        Org org = Org.builder().status(OrgStatus.CANCELED).build();
        assertThrows(BusinessException.class, () -> cancelValidator.validate(org));
    }

    @Test
    void should_throw_exception_if_org_has_emp() {
        Org org = Org.builder().status(OrgStatus.EFFECTIVE).build();
        when(empRepository.findOrgEmps(any(), any())).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> cancelValidator.validate(org));
    }
}