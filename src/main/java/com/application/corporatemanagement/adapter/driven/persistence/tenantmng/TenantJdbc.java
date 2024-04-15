package com.application.corporatemanagement.adapter.driven.persistence.tenantmng;

import com.application.corporatemanagement.domain.tenantmng.TenantRepository;
import com.application.corporatemanagement.domain.tenantmng.TenantStatus;
import org.springframework.stereotype.Repository;

@Repository
public class TenantJdbc implements TenantRepository {
    @Override
    public boolean existsByIdAndStatus(Long tenant, TenantStatus tenantStatus) {
        return false;
    }
}
