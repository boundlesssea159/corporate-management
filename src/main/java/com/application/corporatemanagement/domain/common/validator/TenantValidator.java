package com.application.corporatemanagement.domain.common.validator;

import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import com.application.corporatemanagement.domain.tenantmng.TenantRepository;
import com.application.corporatemanagement.domain.tenantmng.TenantStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TenantValidator {

    private final TenantRepository tenantRepository;

    @Autowired
    public TenantValidator(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    public void tenantShouldBeValid(Long tenant) {
        if (!tenantRepository.existsByIdAndStatus(tenant, TenantStatus.EFFECTIVE)) {
            throw new BusinessException("id为'" + tenant + "'的租户不是有效租户！");
        }
    }
}
