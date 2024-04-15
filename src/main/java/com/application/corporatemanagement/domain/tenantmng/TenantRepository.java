package com.application.corporatemanagement.domain.tenantmng;

public interface TenantRepository {
    boolean existsByIdAndStatus(Long tenant, TenantStatus tenantStatus);
}
