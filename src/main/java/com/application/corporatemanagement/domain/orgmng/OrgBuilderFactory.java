package com.application.corporatemanagement.domain.orgmng;

import com.application.corporatemanagement.domain.common.validator.TenantValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrgBuilderFactory {

    private final TenantValidator tenantValidator;
    private final OrgTypeValidator orgTypeValidator;
    private final SuperiorValidator superiorValidator;
    private final OrgLeaderValidator orgLeaderValidator;
    private final OrgNameValidator orgNameValidator;

    @Autowired
    public OrgBuilderFactory(TenantValidator tenantValidator, OrgTypeValidator orgTypeValidator, SuperiorValidator superiorValidator, OrgLeaderValidator orgLeaderValidator, OrgNameValidator orgNameValidator) {
        this.tenantValidator = tenantValidator;
        this.orgTypeValidator = orgTypeValidator;
        this.superiorValidator = superiorValidator;
        this.orgLeaderValidator = orgLeaderValidator;
        this.orgNameValidator = orgNameValidator;
    }

    public OrgBuilder create() {
        return new OrgBuilder(tenantValidator, orgTypeValidator, superiorValidator, orgLeaderValidator, orgNameValidator);
    }
}
