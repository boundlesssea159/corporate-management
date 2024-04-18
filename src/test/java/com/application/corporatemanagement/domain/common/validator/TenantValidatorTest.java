package com.application.corporatemanagement.domain.common.validator;

import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import com.application.corporatemanagement.domain.tenantmng.TenantRepository;
import com.application.corporatemanagement.domain.tenantmng.TenantStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TenantValidatorTest {
    @Test
    void should_throw_exception_if_tenant_is_invalid() {
        TenantRepository tenantRepository = Mockito.mock(TenantRepository.class);
        when(tenantRepository.existsByIdAndStatus(1L, TenantStatus.EFFECTIVE)).thenReturn(false);
        TenantValidator tenantValidator = new TenantValidator(tenantRepository);
        assertThrows(BusinessException.class, () -> tenantValidator.tenantShouldBeValid(1L));
    }

}