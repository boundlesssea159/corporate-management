package com.application.corporatemanagement.domain.orgmng.org.validators;

import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import com.application.corporatemanagement.domain.orgmng.org.OrgRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class OrgValidatorTest {

    private OrgValidator orgValidator;

    private OrgRepository orgRepository;

    @BeforeEach
    void setUp() {
        orgRepository = Mockito.mock(OrgRepository.class);
        orgValidator = new OrgValidator(orgRepository);
    }
    @Test
    void should_throw_exception_if_org_is_invalid() {
        when(orgRepository.findById(1L, 2L)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> orgValidator.check(1L, 2L));
    }
}