package com.application.corporatemanagement.domain.orgmng.org.validators;

import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import com.application.corporatemanagement.domain.orgmng.org.Org;
import com.application.corporatemanagement.domain.orgmng.org.OrgRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrgValidator {

    private final OrgRepository orgRepository;

    public OrgValidator(OrgRepository orgRepository) {
        this.orgRepository = orgRepository;
    }

    public void check(Long tenantId, Long orgId) {
        orgShouldBeValid(tenantId, orgId);
    }

    private void orgShouldBeValid(Long tenantId, Long orgId) {
        Optional<Org> optionalOrg = orgRepository.findById(tenantId, orgId);
        if (optionalOrg.isEmpty()) {
            throw new BusinessException("组织必须有效");
        }
    }
}
