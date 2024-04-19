package com.application.corporatemanagement.domain.orgmng;

import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import com.application.corporatemanagement.domain.orgmng.org.validators.OrgNameValidator;
import com.application.corporatemanagement.domain.orgmng.org.OrgRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrgNameValidatorTest {


    protected OrgRepository orgRepository;
    private OrgNameValidator orgNameValidator;


    @BeforeEach
    void setUp() {
        orgRepository = mock(OrgRepository.class);
        orgNameValidator = new OrgNameValidator(orgRepository);
    }

    @Test
    void should_throw_exception_if_org_name_is_emtpy() {
        assertThrows(BusinessException.class, () -> orgNameValidator.validate(1L, 2L, ""));
    }

    @Test
    void should_throw_exception_if_sub_org_name_is_duplicated() {
        when(orgRepository.existsBySuperiorAndName(1L, 2L, "org")).thenReturn(true);
        assertThrows(BusinessException.class, () -> orgNameValidator.validate(1L, 2L, "org"));
    }

}