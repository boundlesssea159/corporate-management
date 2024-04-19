package com.application.corporatemanagement.domain.orgmng.org.validators;

import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import com.application.corporatemanagement.domain.orgmng.org.OrgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrgNameValidator {

    private final OrgRepository orgRepository;

    @Autowired
    public OrgNameValidator(OrgRepository orgRepository) {
        this.orgRepository = orgRepository;
    }

    public void validate(Long tenant, Long superior, String orgName) {
        orgShouldHaveName(orgName);
        subOrgNameShouldNotBeDuplicated(tenant, superior, orgName);
    }

    private static void orgShouldHaveName(String orgName) throws BusinessException {
        if (orgName.equals("")) {
            throw new BusinessException("组织没有名称！");
        }
    }

    private void subOrgNameShouldNotBeDuplicated(Long tenant, Long superior, String orgName) throws BusinessException {
        if (orgRepository.existsBySuperiorAndName(tenant, superior, orgName)) {
            throw new BusinessException("同一上级下已经有名为'" + orgName + "'的组织存在！");
        }
    }
}
