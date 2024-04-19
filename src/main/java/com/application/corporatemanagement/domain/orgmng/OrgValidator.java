package com.application.corporatemanagement.domain.orgmng;

import com.application.corporatemanagement.application.orgmng.OrgDto;
import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import com.application.corporatemanagement.domain.common.validator.TenantValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrgValidator {
    private final TenantValidator tenantValidator;
    private final OrgTypeValidator orgTypeValidator;
    private final SuperiorValidator superiorValidator;
    private final OrgLeaderValidator orgLeaderValidator;

    private final OrgNameValidator orgNameValidator;

    @Autowired
    OrgValidator(TenantValidator tenantValidator
            , OrgTypeValidator orgTypeValidator
            , SuperiorValidator superiorValidator
            , OrgLeaderValidator orgLeaderValidator
            , OrgNameValidator orgNameValidator) {
        this.tenantValidator = tenantValidator;
        this.orgTypeValidator = orgTypeValidator;
        this.superiorValidator = superiorValidator;
        this.orgLeaderValidator = orgLeaderValidator;
        this.orgNameValidator = orgNameValidator;
    }

    public void validate(OrgDto request) {
        tenantValidator.tenantShouldBeValid(request.getTenant());
        orgTypeValidator.validate(request.getTenant(), request.getOrgType());
        superiorValidator.validate(request.getTenant(), request.getSuperior(), request.getOrgType());
        orgLeaderValidator.orgLeaderShouldBeOnWorking(request.getTenant(), request.getLeader());
        orgNameValidator.validate(request.getTenant(), request.getSuperior(), request.getName());
    }
}