package com.application.corporatemanagement.domain.orgmng;

import java.util.Optional;

public interface OrgRepository {
    Optional<Org> save(Org org, Long userId);

    Optional<Org> findByIdAndStatus(Long tenant, Long id, OrgStatus status);

    boolean existsBySuperiorAndName(Long tenant, Long id, String name);
}
