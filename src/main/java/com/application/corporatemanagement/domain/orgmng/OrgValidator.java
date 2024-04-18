package com.application.corporatemanagement.domain.orgmng;

import com.application.corporatemanagement.application.orgmng.OrgDto;
import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import com.application.corporatemanagement.domain.common.validator.TenantValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrgValidator {
    private final OrgTypeRepository orgTypeRepository;
    private final OrgRepository orgRepository;
    private final EmpRepository empRepository;

    private final TenantValidator tenantValidator;
    private final OrgTypeValidator orgTypeValidator;

    private final SuperiorValidator superiorValidator;

    @Autowired
    OrgValidator(OrgTypeRepository orgTypeRepository, OrgRepository orgRepository, EmpRepository empRepository, TenantValidator tenantValidator, OrgTypeValidator orgTypeValidator, SuperiorValidator superiorValidator) {
        this.orgTypeRepository = orgTypeRepository;
        this.orgRepository = orgRepository;
        this.empRepository = empRepository;
        this.tenantValidator = tenantValidator;
        this.orgTypeValidator = orgTypeValidator;
        this.superiorValidator = superiorValidator;
    }

    public void validate(OrgDto request) {
        tenantValidator.tenantShouldBeValid(request.getTenant());
        orgTypeValidator.validate(request.getTenant(), request.getOrgType());
        superiorValidator.validate(request.getTenant(), request.getSuperior(), request.getOrgType());

        corporateShouldNotBeCreatdAlone(request.getOrgType());
        orgLeaderShouldBeOnWorking(request.getTenant(), request.getLeader());
        orgShouldHaveName(request.getName());
        subOrgNameShouldNotBeDuplicated(request.getTenant(), request.getSuperior(), request.getName());
    }

    private static void corporateShouldNotBeCreatdAlone(String orgType) {
        if (orgType.equals("ENTP")) {
            throw new BusinessException("企业是在创建租户的时候创建好的，因此不能单独创建企业!");
        }
    }

    private void orgLeaderShouldBeOnWorking(Long tenant, Long leader) {
        if (leader == null || !empRepository.existsByIdAndStatus(tenant, leader, EmpStatus.REGULAR, EmpStatus.PROBATION)) {
            throw new BusinessException("组织负责人(id='" + leader + "')不是在职员工！");
        }
    }

    private static void orgShouldHaveName(String name) throws BusinessException {
        if (name.equals("")) {
            throw new BusinessException("组织没有名称！");
        }
    }

    private void subOrgNameShouldNotBeDuplicated(Long tenant, Long superior, String name) throws BusinessException {
        if (orgRepository.existsBySuperiorAndName(tenant, superior, name)) {
            throw new BusinessException("同一上级下已经有名为'" + name + "'的组织存在！");
        }
    }
}

// todo split validator to validators