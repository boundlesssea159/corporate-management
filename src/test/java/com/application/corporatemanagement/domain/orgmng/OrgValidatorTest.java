package com.application.corporatemanagement.domain.orgmng;

import com.application.corporatemanagement.application.orgmng.OrgDto;
import com.application.corporatemanagement.domain.exceptions.BusinessException;
import com.application.corporatemanagement.domain.exceptions.DirtyDataException;
import com.application.corporatemanagement.domain.tenantmng.TenantRepository;
import com.application.corporatemanagement.domain.tenantmng.TenantStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrgValidatorTest {

    protected NormalOrgStub normalOrgStub;
    protected SuperiorOrgStub superiorOrgStub;
    protected Org superiorOrg;
    protected OrgDto orgDto;
    private OrgValidator orgValidator;

    private TenantRepository tenantRepository;

    private OrgTypeRepository orgTypeRepository;

    private OrgRepository orgRepository;

    private EmpRepository empRepository;

    @BeforeEach
    void setUp() {
        tenantRepository = Mockito.mock(TenantRepository.class);
        orgTypeRepository = Mockito.mock(OrgTypeRepository.class);
        orgRepository = Mockito.mock(OrgRepository.class);
        empRepository = Mockito.mock(EmpRepository.class);
        orgDto = mockOrgDto();
        stub();
        when(empRepository.existsByIdAndStatus(orgDto.getTenant(), orgDto.getLeader(), EmpStatus.REGULAR, EmpStatus.PROBATION)).thenReturn(true);
        when(orgRepository.existsBySuperiorAndName(orgDto.getTenant(), orgDto.getSuperior(), orgDto.getName())).thenReturn(false);
        orgValidator = new OrgValidator(tenantRepository, orgTypeRepository, orgRepository, empRepository);
    }

    private void stub() {
        normalOrgStub = new NormalOrgStub(mockOrgDto());
        normalOrgStub
                .assignName("normal_org")
                .assignOrgType("DEVGRP")
                .stub();
        OrgDto orgDtoFroSuperior = mockOrgDto();
        superiorOrgStub = new SuperiorOrgStub(mockOrgDto());
        superiorOrgStub
                .assignId(orgDtoFroSuperior.getSuperior())
                .assignOrgType("DEVCENT")
                .assignName("superior_org")
                .stub();
    }

    private OrgDto mockOrgDto() {
        return OrgDto.builder()
                .name("org")
                .tenant(1L)
                .leader(2L)
                .superior(3L)
                .id(4L)
                .orgType("DEVGRP")
                .status("effective")
                .build();
    }


    abstract static class OrgStub {

        protected OrgDto request;

        OrgStub(OrgDto request) {
            this.request = request;
        }

        public OrgStub assignOrgType(String orgType) {
            request.setOrgType(orgType);
            return this;
        }

        public OrgStub assignName(String name) {
            request.setName(name);
            return this;
        }

        public OrgStub assignId(Long id) {
            request.setId(id);
            return this;
        }

        abstract void stub();
    }

    class NormalOrgStub extends OrgStub {
        NormalOrgStub(OrgDto orgDto) {
            super(orgDto);
        }

        void stub() {
            when(tenantRepository.existsByIdAndStatus(request.getTenant(), TenantStatus.EFFECTIVE)).thenReturn(true);
            when(orgTypeRepository.existsByCodeAndStatus(request.getTenant(), request.getOrgType(), OrgTypeStatus.EFFECTIVE)).thenReturn(true);
        }
    }

    class SuperiorOrgStub extends OrgStub {

        SuperiorOrgStub(OrgDto orgDto) {
            super(orgDto);
        }

        void stub() {
            superiorOrg = Org.builder()
                    .id(request.getSuperior())
                    .name(request.getName())
                    .tenantId(request.getTenant())
                    .orgType(request.getOrgType())
                    .status(request.getStatus())
                    .build();
            when(orgRepository.findByIdAndStatus(superiorOrg.getTenantId(), superiorOrg.getId(), OrgStatus.EFFECTIVE)).thenReturn(Optional.of(superiorOrg));
            when(orgTypeRepository
                    .findByCodeAndStatus(superiorOrg.getTenantId(), superiorOrg.getOrgType(), OrgTypeStatus.EFFECTIVE))
                    .thenReturn(
                            Optional.of(
                                    OrgType.builder()
                                            .code(superiorOrg.getOrgType())
                                            .build())
                    );
        }
    }

    @Test
    void should_throw_exception_if_tenant_is_invalid() {
        when(tenantRepository.existsByIdAndStatus(orgDto.getTenant(), TenantStatus.EFFECTIVE)).thenReturn(false);
        assertThrowException();
    }

    private void assertThrowException() {
        assertThrows(BusinessException.class, () -> orgValidator.validate(orgDto));
    }

    @Test
    void should_throw_exception_if_org_type_is_empty() {
        orgDto.setOrgType("");
        assertThrowException();
    }

    @Test
    void should_throw_exception_if_corporate_created_alone() {
        orgDto.setOrgType("ENTP");
        assertThrowException();
    }

    @Test
    void should_throw_exception_if_org_type_is_invalid() {
        when(orgTypeRepository.existsByCodeAndStatus(orgDto.getTenant(), orgDto.getOrgType(), OrgTypeStatus.EFFECTIVE)).thenReturn(false);
        assertThrowException();
    }

    @Test
    void should_throw_exception_if_superior_is_not_valid() {
        when(orgRepository.findByIdAndStatus(orgDto.getTenant(), orgDto.getSuperior(), OrgStatus.EFFECTIVE)).thenReturn(Optional.empty());
        assertThrowException();
    }

    @Test
    void should_throw_exception_if_superior_org_type_is_invalid() {
        when(orgTypeRepository.findByCodeAndStatus(superiorOrg.getTenantId(), superiorOrg.getOrgType(), OrgTypeStatus.EFFECTIVE)).thenReturn(Optional.empty());
        assertThrows(DirtyDataException.class, () -> orgValidator.validate(orgDto));
    }

    @Test
    void should_throw_exception_if_superior_of_dev_group_is_not_dev_center() {
        superiorOrgStub.assignOrgType("NO_CENTER").stub();
        assertThrowException();
    }

    @Test
    void should_throw_exception_if_superior_of_dev_center_is_not_entp() {
        superiorOrgStub.assignOrgType("NO_ENTP").stub();
        assertThrowException();
    }

    @Test
    void should_throw_exception_if_superior_of_direct_dept_is_not_entp() {
        normalOrgStub.assignOrgType("DIRDEP").stub();
        superiorOrgStub.assignOrgType("NO_ENTP").stub();
        assertThrowException();
    }

    @Test
    void should_throw_exception_if_leader_not_on_working() {
        Long leader = orgDto.getLeader();
        orgDto.setLeader(null);
        assertThrowException();
        orgDto.setLeader(leader);
        when(empRepository.existsByIdAndStatus(orgDto.getTenant(), orgDto.getLeader(), EmpStatus.REGULAR, EmpStatus.PROBATION)).thenReturn(false);
        assertThrowException();
    }

    @Test
    void should_throw_exception_if_org_name_is_emtpy() {
        orgDto.setName("");
        assertThrowException();
    }

    @Test
    void should_throw_exception_if_sub_org_name_is_duplicated() {
        when(orgRepository.existsBySuperiorAndName(orgDto.getTenant(), orgDto.getSuperior(), orgDto.getName())).thenReturn(true);
        assertThrowException();
    }
}