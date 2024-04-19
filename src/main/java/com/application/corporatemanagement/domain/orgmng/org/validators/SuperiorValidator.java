package com.application.corporatemanagement.domain.orgmng.org.validators;

import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import com.application.corporatemanagement.domain.common.exceptions.DirtyDataException;
import com.application.corporatemanagement.domain.orgmng.org.Org;
import com.application.corporatemanagement.domain.orgmng.org.OrgRepository;
import com.application.corporatemanagement.domain.orgmng.org.OrgStatus;
import com.application.corporatemanagement.domain.orgmng.org.OrgTypeStatus;
import com.application.corporatemanagement.domain.orgmng.orgType.OrgType;
import com.application.corporatemanagement.domain.orgmng.orgType.OrgTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuperiorValidator {


    private final OrgRepository orgRepository;

    private final OrgTypeRepository orgTypeRepository;

    @Autowired
    public SuperiorValidator(OrgRepository orgRepository, OrgTypeRepository orgTypeRepository) {
        this.orgRepository = orgRepository;
        this.orgTypeRepository = orgTypeRepository;
    }

    public void validate(Long tenant, Long superior, String orgType) {
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

    private static void superiorOfDevGroupShouldBeDevCenter(String orgType, String superiorOrgType, Long superior) {
        if ("DEVGRP".equals(orgType) && !"DEVCENT".equals(superiorOrgType)) {
            throw new BusinessException("开发组的上级(id = '" + superior + "')不是开发中心！");
        }
    }

    private static void superiorOfDevCenterAndDirectDeptShouldBeEntp(String orgType, String superiorOrgType, Long superior) {
        if (("DEVCENT".equals(orgType) || "DIRDEP".equals(orgType)) && !"ENTP".equals(superiorOrgType)) {
            throw new BusinessException("开发中心或直属部门的上级(id = '" + superior + "')不是企业！");
        }
    }
}
