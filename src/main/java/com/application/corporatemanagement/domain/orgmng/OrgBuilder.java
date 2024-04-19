package com.application.corporatemanagement.domain.orgmng;

import com.application.corporatemanagement.domain.common.validator.TenantValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrgBuilder {
    private final TenantValidator tenantValidator;
    private final OrgTypeValidator orgTypeValidator;
    private final SuperiorValidator superiorValidator;
    private final OrgLeaderValidator orgLeaderValidator;
    private final OrgNameValidator orgNameValidator;
    private String name;
    private Long tenant;
    private Long leader;
    private Long superior;
    private String orgType;
    private String status;

    @Autowired
    public OrgBuilder(TenantValidator tenantValidator
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

    public OrgBuilder name(String name) {
        this.name = name;
        return this;
    }

    public OrgBuilder tenant(Long tenant) {
        this.tenant = tenant;
        return this;
    }


    public OrgBuilder leader(Long leader) {
        this.leader = leader;
        return this;
    }

    public OrgBuilder superior(Long superior) {
        this.superior = superior;
        return this;
    }

    public OrgBuilder orgType(String orgType) {
        this.orgType = orgType;
        return this;
    }

    public OrgBuilder status(String status) {
        this.status = status;
        return this;
    }

    public Org build() {
        validate();
        return Org.builder()
                .name(name)
                .orgType(orgType)
                .tenantId(tenant)
                .superiorId(superior)
                .leaderId(leader)
                .status(status)
                .build();
    }

    private void validate() {
        tenantValidator.tenantShouldBeValid(tenant);
        orgTypeValidator.validate(tenant, orgType);
        superiorValidator.validate(tenant, superior, orgType);
        orgLeaderValidator.orgLeaderShouldBeOnWorking(tenant, leader);
        orgNameValidator.validate(tenant, superior, name);
    }
}
