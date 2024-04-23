package com.application.corporatemanagement.domain.orgmng.org.validators;

import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import com.application.corporatemanagement.domain.common.exceptions.DirtyDataException;
import com.application.corporatemanagement.domain.orgmng.org.*;
import com.application.corporatemanagement.domain.orgmng.org.validators.SuperiorValidator;
import com.application.corporatemanagement.domain.orgmng.orgType.OrgType;
import com.application.corporatemanagement.domain.orgmng.orgType.OrgTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SuperiorValidatorTest {
    protected OrgTypeRepository orgTypeRepository;
    protected SuperiorValidator superiorValidator;
    protected OrgRepository orgRepository;

    private Param param;

    private static class Param {
        public Long tenant = 1L;
        public String orgType = "DEV";
        public OrgTypeStatus orgTypeStatus = OrgTypeStatus.EFFECTIVE;
        public OrgStatus orgStatus = OrgStatus.EFFECTIVE;
        public Long superior = 2L;
        public String superiorOrgType = "CENTER";

    }


    @BeforeEach
    void setUp() {
        orgTypeRepository = mock(OrgTypeRepository.class);
        orgRepository = mock(OrgRepository.class);
        param = new Param();
        mockSuperior(param.superiorOrgType);
        superiorValidator = new SuperiorValidator(orgRepository, orgTypeRepository);
    }


    @Test
    void should_throw_exception_if_superior_org_type_is_invalid() {
        when(orgTypeRepository.findByCodeAndStatus(param.tenant, param.superiorOrgType, param.orgTypeStatus)).thenReturn(Optional.empty());
        assertThrows(DirtyDataException.class, () -> superiorValidator.validate(param.tenant, param.superior, param.orgType));
    }

    @Test
    void should_throw_exception_if_superior_is_not_valid() {
        when(orgRepository.findByIdAndStatus(param.tenant, param.superior, param.orgStatus)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> superiorValidator.validate(param.tenant, param.superior, param.orgType));
    }

    @Test
    void should_throw_exception_if_superior_of_dev_group_is_not_dev_center() {
        checkSubAndUpOrgTypeRelation("DEVGRP", "NO_CENTER");
    }

    @Test
    void should_throw_exception_if_superior_of_dev_center_is_not_entp() {
        checkSubAndUpOrgTypeRelation("DEVCENT", "NO_ENTP");
    }

    @Test
    void should_throw_exception_if_superior_of_direct_dept_is_not_entp() {
        checkSubAndUpOrgTypeRelation("DIRDEP", "NO_ENTP");
    }

    private void checkSubAndUpOrgTypeRelation(String orgType, String superiorOrgType) {
        mockSuperior(superiorOrgType);
        assertThrows(BusinessException.class, () -> superiorValidator.validate(param.tenant, param.superior, orgType));
    }

    private void mockSuperior(String superiorOrgType) {
        Org superiorOrg = Org.builder()
                .orgType(superiorOrgType)
                .build();
        when(orgRepository.findByIdAndStatus(param.tenant, param.superior, param.orgStatus)).thenReturn(Optional.of(superiorOrg));
        OrgType orgType = OrgType.builder()
                .code(superiorOrgType)
                .build();
        when(orgTypeRepository.findByCodeAndStatus(param.tenant, superiorOrgType, param.orgTypeStatus)).thenReturn(Optional.of(orgType));
    }
}