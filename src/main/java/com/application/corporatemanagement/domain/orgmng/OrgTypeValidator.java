package com.application.corporatemanagement.domain.orgmng;

import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrgTypeValidator {

    private final OrgTypeRepository orgTypeRepository;

    @Autowired
    public OrgTypeValidator(OrgTypeRepository orgTypeRepository) {
        this.orgTypeRepository = orgTypeRepository;
    }

    public void validate(Long tenant, String orgType) {
        orgTypeShouldBeNotEmpty(orgType);
        orgTypeShouldBeValid(tenant, orgType);
    }

    private static void orgTypeShouldBeNotEmpty(String orgType) {
        if (orgType.isEmpty()) {
            throw new BusinessException("组织类别不能为空！");
        }
    }

    private void orgTypeShouldBeValid(Long tenant, String orgType) {
        if (!orgTypeRepository.existsByCodeAndStatus(tenant, orgType, OrgTypeStatus.EFFECTIVE)) {
            throw new BusinessException("'" + orgType + "'不是有效的组织类别代码！");
        }
    }
}
