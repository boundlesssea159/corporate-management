package com.application.corporatemanagement.domain.orgmng.org;

import com.application.corporatemanagement.domain.orgmng.org.validators.OrgNameValidator;
import com.application.corporatemanagement.domain.orgmng.org.validators.SuperiorValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrgHandler {

    private final OrgNameValidator orgNameValidator;

    private final SuperiorValidator superiorValidator;

    public OrgHandler(OrgNameValidator orgNameValidator, SuperiorValidator superiorValidator) {
        this.orgNameValidator = orgNameValidator;
        this.superiorValidator = superiorValidator;
    }

    public void update(Org org, String name, Long superior, Long userId) {
        orgNameValidator.validate(org.getTenant(), superior, name);
        superiorValidator.validate(org.getTenant(), org.getSuperior(), org.getOrgType());
        org.name(name).superior(superior).updator(userId).updatedAt(LocalDateTime.now());
    }
}
