package com.application.corporatemanagement.adapter.driven.persistence.orgmng;

import com.application.corporatemanagement.domain.orgmng.OrgType;
import com.application.corporatemanagement.domain.orgmng.OrgTypeRepository;
import com.application.corporatemanagement.domain.orgmng.OrgTypeStatus;
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
