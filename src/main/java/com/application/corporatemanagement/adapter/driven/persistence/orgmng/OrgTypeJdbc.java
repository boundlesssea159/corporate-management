package com.application.corporatemanagement.adapter.driven.persistence.orgmng;

import com.application.corporatemanagement.domain.orgmng.orgType.OrgType;
import com.application.corporatemanagement.domain.orgmng.orgType.OrgTypeRepository;
import com.application.corporatemanagement.domain.orgmng.org.OrgTypeStatus;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class OrgTypeJdbc implements OrgTypeRepository {
    @Override
    public boolean existsByCodeAndStatus(Long tenant, String type, OrgTypeStatus status) {
        return false;
    }

    @Override
    public Optional<OrgType> findByCodeAndStatus(Long tenant, String type, OrgTypeStatus status) {
        return Optional.empty();
    }
}
