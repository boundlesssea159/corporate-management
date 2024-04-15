package com.application.corporatemanagement.domain.orgmng;

import java.util.Optional;

public interface OrgTypeRepository {
    boolean existsByCodeAndStatus(Long tenant, String type, OrgTypeStatus status);

    Optional<OrgType> findByCodeAndStatus(Long tenant, String type, OrgTypeStatus status);
}
