package com.application.corporatemanagement.domain.orgmng;

import com.application.corporatemanagement.application.orgmng.OrgDto;
import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import com.application.corporatemanagement.domain.common.exceptions.DirtyDataException;
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

//        superiorCheck(request.getTenant(), request.getOrgType(), request.getSuperior());
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


    private void superiorCheck(Long tenant, String orgType, Long superior) {
        Org superiorOrg = superiorShouldBeEffective(tenant, superior);
        OrgType superiorOrgType = superiorOrgTypeShouldBeValid(tenant, superior, superiorOrg.getOrgType());
        superiorOfDevGroupShouldBeDevCenter(orgType, superiorOrgType.getCode(), superior);
        superiorOfDevCenterAndDirectDeptShouldBeEntp(orgType, superiorOrgType.getCode(), superior);
    }

    private Org superiorShouldBeEffective(Long tenant, Long superior) {
        return orgRepository
                .findByIdAndStatus(tenant, superior, OrgStatus.EFFECTIVE)
                .orElseThrow(() -> new BusinessException("'" + superior + "' 不是有效的组织 id !"));
    }

    private OrgType superiorOrgTypeShouldBeValid(Long tenant, Long superior, String superiorOrgType) {
        return orgTypeRepository
                .findByCodeAndStatus(tenant, superiorOrgType, OrgTypeStatus.EFFECTIVE)
                .orElseThrow(() -> new DirtyDataException("id 为 '" + superior + "' 的组织的组织类型代码 '" + superiorOrgType + "' 无效!"));
    }

    private static void superiorOfDevGroupShouldBeDevCenter(String orgType, String superiorOrgTypeCode, Long superior) {
        if ("DEVGRP".equals(orgType) && !"DEVCENT".equals(superiorOrgTypeCode)) {
            throw new BusinessException("开发组的上级(id = '" + superior + "')不是开发中心！");
        }
    }

    private static void superiorOfDevCenterAndDirectDeptShouldBeEntp(String orgType, String superiorOrgTypeCode, Long superior) {
        if (("DEVCENT".equals(orgType) || "DIRDEP".equals(orgType)) && !"ENTP".equals(superiorOrgTypeCode)) {
            throw new BusinessException("开发中心或直属部门的上级(id = '" + superior + "')不是企业！");
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