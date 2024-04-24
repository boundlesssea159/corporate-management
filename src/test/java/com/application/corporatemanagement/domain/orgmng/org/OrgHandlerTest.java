package com.application.corporatemanagement.domain.orgmng.org;

import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import com.application.corporatemanagement.domain.orgmng.org.validators.OrgNameValidator;
import com.application.corporatemanagement.domain.orgmng.org.validators.SuperiorValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

class OrgHandlerTest {

    protected Org org;
    protected OrgHandler orgHandler;
    protected OrgNameValidator orgNameValidator;
    protected SuperiorValidator superiorValidator;

    @BeforeEach
    void setUp() {
        org = Org.builder().id(100L).tenant(1L).name("org").superior(2L).orgType("DEV").build();
        orgNameValidator = Mockito.mock(OrgNameValidator.class);
        superiorValidator = Mockito.mock(SuperiorValidator.class);
        orgHandler = new OrgHandler(orgNameValidator, superiorValidator);
    }

    @Test
    void should_update_org() {
        orgHandler.update(org, "new name", 3L, 10010L);
        assertEquals("new name", org.getName());
        assertEquals(3L, org.getSuperior());
    }

    @Test
    void should_throw_exception_if_name_check_fail() {
        doThrow(BusinessException.class).when(orgNameValidator).validate(org.getTenant(), 3L, "new name");
        assertThrows(BusinessException.class, () -> orgHandler.update(org, "new name", 3L, 10010L));
    }

    @Test
    void should_throw_exception_if_superior_check_fail() {
        doThrow(BusinessException.class).when(superiorValidator).validate(org.getTenant(), org.getSuperior(), org.getOrgType());
        assertThrows(BusinessException.class, () -> orgHandler.update(org, "new name", 3L, 10010L));
    }
}