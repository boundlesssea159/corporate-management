package com.application.corporatemanagement.domain.orgmng;

import com.application.corporatemanagement.application.orgmng.OrgDto;
import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import com.application.corporatemanagement.domain.common.validator.TenantValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrgValidatorTest {
    protected OrgDto orgDto;
    protected TenantValidator tenantValidator;
    protected OrgTypeValidator orgTypeValidator;
    protected SuperiorValidator superiorValidator;
    protected OrgLeaderValidator orgLeaderValidator;
    protected OrgNameValidator orgNameValidator;
    private OrgValidator orgValidator;

    @BeforeEach
    void setUp() {
        tenantValidator = mock(TenantValidator.class);
        orgTypeValidator = mock(OrgTypeValidator.class);
        superiorValidator = mock(SuperiorValidator.class);
        orgLeaderValidator = mock(OrgLeaderValidator.class);
        orgNameValidator = mock(OrgNameValidator.class);
        orgDto = mockOrgDto();
        orgValidator = new OrgValidator(tenantValidator, orgTypeValidator, superiorValidator, orgLeaderValidator, orgNameValidator);
    }

    private OrgDto mockOrgDto() {
        return OrgDto.builder().name("org").tenant(1L).leader(2L).superior(3L).id(4L).orgType("DEVGRP").status("effective").build();
    }

    @Test
    void should_throw_exception_if_check_tenant_fail() {
        doThrow(BusinessException.class).when(tenantValidator).tenantShouldBeValid(orgDto.getTenant());
        assertThrowException();
    }


    @Test
    void should_throw_exception_if_check_org_type_fail() {
        doThrow(BusinessException.class).when(orgTypeValidator).validate(any(), any());
        assertThrowException();
    }

    @Test
    void should_throw_exception_if_check_superior_fail() {
        doThrow(BusinessException.class).when(superiorValidator).validate(any(), any(), any());
        assertThrowException();
    }

    @Test
    void should_throw_exception_if_check_org_leader_fail() {
        doThrow(BusinessException.class).when(orgLeaderValidator).orgLeaderShouldBeOnWorking(any(), any());
        assertThrowException();
    }

    @Test
    void should_throw_exception_if_check_org_name_fail() {
        doThrow(BusinessException.class).when(orgNameValidator).validate(any(), any(), any());
        assertThrowException();
    }

    private void assertThrowException() {
        assertThrows(BusinessException.class, () -> orgValidator.validate(orgDto));
    }
}