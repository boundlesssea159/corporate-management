package com.application.corporatemanagement.domain.orgmng.org.validators;

import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import com.application.corporatemanagement.domain.orgmng.org.OrgTypeStatus;
import com.application.corporatemanagement.domain.orgmng.orgType.OrgTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrgTypeValidatorTest {

    protected OrgTypeRepository orgTypeRepository;
    protected OrgTypeValidator orgTypeValidator;

    @BeforeEach
    void setUp() {
        orgTypeRepository = mock(OrgTypeRepository.class);
        orgTypeValidator = new OrgTypeValidator(orgTypeRepository);
    }

    @Test
    void should_throw_exception_if_org_type_is_empty() {
        assertThrows(BusinessException.class, () -> orgTypeValidator.validate(1L, ""));
    }

    @Test
    void should_throw_exception_if_org_type_is_invalid() {
        assertThrows(BusinessException.class, () -> orgTypeValidator.validate(1L, "DEV"));
    }

    @Test
    void should_throw_exception_if_ENTP_created_alone() {
        when(orgTypeRepository.existsByCodeAndStatus(1L, "ENTP", OrgTypeStatus.EFFECTIVE)).thenReturn(true);
        assertThrows(BusinessException.class, () -> orgTypeValidator.validate(1L, "ENTP"));
    }
}