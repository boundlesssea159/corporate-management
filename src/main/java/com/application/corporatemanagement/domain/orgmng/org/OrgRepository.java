package com.application.corporatemanagement.domain.orgmng.org;

import java.util.Optional;

public interface OrgRepository {
    Optional<Org> save(Org org, Long userId);

    Optional<Org> findByIdAndStatus(Long tenant, Long id, OrgStatus status);

    boolean existsBySuperiorAndName(Long tenant, Long id, String name);

    Optional<Org> findById(Long tenant, Long id);

    Optional<Org> update(Org org, Long userId);
}
