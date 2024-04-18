package com.application.corporatemanagement.domain.orgmng;

import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
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
        when(orgTypeRepository.existsByCodeAndStatus(1L, "DEV", OrgTypeStatus.EFFECTIVE)).thenReturn(false);
        assertThrows(BusinessException.class, () -> orgTypeValidator.validate(1L, "DEV"));
    }

}