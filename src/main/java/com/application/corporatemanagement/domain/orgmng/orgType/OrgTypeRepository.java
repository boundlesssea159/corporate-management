package com.application.corporatemanagement.domain.orgmng.orgType;

import com.application.corporatemanagement.domain.orgmng.org.OrgTypeStatus;

import java.util.Optional;

public interface OrgTypeRepository {
    boolean existsByCodeAndStatus(Long tenant, String type, OrgTypeStatus status);

    Optional<OrgType> findByCodeAndStatus(Long tenant, String type, OrgTypeStatus status);
}
