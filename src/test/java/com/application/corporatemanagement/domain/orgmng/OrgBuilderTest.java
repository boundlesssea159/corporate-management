package com.application.corporatemanagement.domain.orgmng;

import com.application.corporatemanagement.application.orgmng.OrgDto;
import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import com.application.corporatemanagement.domain.common.validator.TenantValidator;
import com.application.corporatemanagement.domain.orgmng.org.*;
import com.application.corporatemanagement.domain.orgmng.org.validators.OrgLeaderValidator;
import com.application.corporatemanagement.domain.orgmng.org.validators.OrgNameValidator;
import com.application.corporatemanagement.domain.orgmng.org.validators.OrgTypeValidator;
import com.application.corporatemanagement.domain.orgmng.org.validators.SuperiorValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class OrgBuilderTest {


    protected OrgDto orgDto;
    protected TenantValidator tenantValidator;
    protected OrgTypeValidator orgTypeValidator;
    protected SuperiorValidator superiorValidator;
    protected OrgLeaderValidator orgLeaderValidator;
    protected OrgNameValidator orgNameValidator;
    private OrgBuilder orgBuilder;

    @BeforeEach
    void setUp() {
        tenantValidator = mock(TenantValidator.class);
        orgTypeValidator = mock(OrgTypeValidator.class);
        superiorValidator = mock(SuperiorValidator.class);
        orgLeaderValidator = mock(OrgLeaderValidator.class);
        orgNameValidator = mock(OrgNameValidator.class);
        orgDto = mockOrgDto();
        orgBuilder = new OrgBuilder(tenantValidator, orgTypeValidator, superiorValidator, orgLeaderValidator, orgNameValidator);
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
        assertThrows(BusinessException.class, () -> orgBuilder.build());
    }

    @Test
    void should_create_org() {
        Org org = orgBuilder
                .name("name")
                .orgType("type")
                .status("status")
                .leader(1L)
                .tenant(2L)
                .superior(3L)
                .build();
        assertEquals("name", org.getName());
        assertEquals("type", org.getOrgType());
        assertEquals("status", org.getStatus());
        assertEquals(1L, org.getLeaderId());
        assertEquals(2L, org.getTenantId());
        assertEquals(3L, org.getSuperiorId());
    }

}