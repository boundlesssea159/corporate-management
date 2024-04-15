package com.application.corporatemanagement.application.orgmng;

import com.application.corporatemanagement.domain.exceptions.BusinessException;
import com.application.corporatemanagement.domain.exceptions.DirtyDataException;
import com.application.corporatemanagement.domain.orgmng.*;
import com.application.corporatemanagement.domain.tenantmng.TenantRepository;
import com.application.corporatemanagement.domain.tenantmng.TenantStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrgService {

    private final OrgRepository orgRepository;

    private final TenantRepository tenantRepository;

    private final OrgTypeRepository orgTypeRepository;

    private final EmpRepository empRepository;

    @Autowired
    public OrgService(
            OrgRepository orgRepository
            , TenantRepository tenantRepository
            , OrgTypeRepository orgTypeRepository
            , EmpRepository empRepository) {
        this.orgRepository = orgRepository;
        this.tenantRepository = tenantRepository;
        this.orgTypeRepository = orgTypeRepository;
        this.empRepository = empRepository;
    }

    public Optional<Org> add(OrgDto request, Long userId) {
        validate(request);
        Org org = Org.builder()
                .name(request.getName())
                .tenantId(request.getTenant())
                .leaderId(request.getLeader())
                .superiorId(request.getSuperior())
                .orgType(request.getOrgType())
                .build();
        return orgRepository.save(org, userId);
    }

    // todo 这些领域逻辑的校验放到领域服务区，相关的测试也应该放到领域服务对象上去
    //  对于application的service而言，它不包括这些逻辑，它只涉及一个接口调用，因此它的测试用例不用罗列的这么详细，只需要包含对verify调用成功和对verify调用失败两种情况即可
    private void validate(OrgDto request) {
        final var tenant = request.getTenant();// 租户必须有效
        tenantShouldBeValid(tenant);
        orgTypeShouldNotBeEmpty(request);
        corporateShouldNotBeCreatdAlone(request);
        orgTypeShouldBeValid(request, tenant);
        superiorCheck(request, tenant);
        orgLeaderShouldBeOnWorking(request, tenant);
        orgShouldHaveName(request);
        subOrgNameShouldNotBeDuplicated(request, tenant);
    }

    private void orgLeaderShouldBeOnWorking(OrgDto request, Long tenant) {
        if (request.getLeader() != null && !empRepository.existsByIdAndStatus(tenant, request.getLeader(), EmpStatus.REGULAR, EmpStatus.PROBATION)) {
            throw new BusinessException("组织负责人(id='" + request.getLeader() + "')不是在职员工！");
        }
    }

    private void superiorCheck(OrgDto request, Long tenant) {
        // 上级组织应该是有效组织
        Org superior = superiorShouldBeEffective(request, tenant);
        // 取上级组织的组织类别
        OrgType superiorOrgType = superiorOrgTypeShouldBeValid(request, tenant, superior);
        // 开发组的上级只能是开发中心
        superiorOfDevGroupShouldBeDevCenter(request, superiorOrgType);
        // 开发中心和直属部门的上级只能是企业
        superiorOfDevCenterAndDirectDeptShouldBeEntp(request, superiorOrgType);
    }

    private void superiorOfDevCenterAndDirectDeptShouldBeEntp(OrgDto request, OrgType superiorOrgType) {
        if (("DEVCENT".equals(request.getOrgType()) || "DIRDEP".equals(request.getOrgType())) && !"ENTP".equals(superiorOrgType.getCode())) {
            throw new BusinessException("开发中心或直属部门的上级(id = '" + request.getSuperior() + "')不是企业！");
        }
    }

    private void superiorOfDevGroupShouldBeDevCenter(OrgDto request, OrgType superiorOrgType) {
        if ("DEVGRP".equals(request.getOrgType()) && !"DEVCENT".equals(superiorOrgType.getCode())) {
            throw new BusinessException("开发组的上级(id = '" + request.getSuperior() + "')不是开发中心！");
        }
    }

    private OrgType superiorOrgTypeShouldBeValid(OrgDto request, Long tenant, Org superior) {
        return orgTypeRepository.findByCodeAndStatus(tenant, superior.getOrgType(), OrgTypeStatus.EFFECTIVE)
                .orElseThrow(() -> new DirtyDataException("id 为 '" + request.getSuperior() + "' 的组织的组织类型代码 '" + superior.getOrgType() + "' 无效!"));
    }

    private Org superiorShouldBeEffective(OrgDto request, Long tenant) {
        return orgRepository.findByIdAndStatus(tenant, request.getSuperior(), OrgStatus.EFFECTIVE)
                .orElseThrow(() -> new BusinessException("'" + request.getSuperior() + "' 不是有效的组织 id !"));
    }

    private void orgTypeShouldBeValid(OrgDto request, Long tenant) {
        if (!orgTypeRepository.existsByCodeAndStatus(tenant, request.getOrgType(), OrgTypeStatus.EFFECTIVE)) {
            throw new BusinessException("'" + request.getOrgType() + "'不是有效的组织类别代码！");
        }
    }

    private void tenantShouldBeValid(Long tenant) {
        if (!tenantRepository.existsByIdAndStatus(tenant, TenantStatus.EFFECTIVE)) {
            throw new BusinessException("id为'" + tenant + "'的租户不是有效租户！");
        }
    }

    private void subOrgNameShouldNotBeDuplicated(OrgDto request, Long tenant) throws BusinessException {
        if (orgRepository.existsBySuperiorAndName(tenant, request.getSuperior(), request.getName())) {
            throw new BusinessException("同一上级下已经有名为'" + request.getName() + "'的组织存在！");
        }
    }

    private void orgShouldHaveName(OrgDto request) throws BusinessException {
        if (request.getName().equals("")) {
            throw new BusinessException("组织没有名称！");
        }
    }

    private void corporateShouldNotBeCreatdAlone(OrgDto request) throws BusinessException {
        if ("ENTP".equals(request.getOrgType())) {
            throw new BusinessException("企业是在创建租户的时候创建好的，因此不能单独创建企业!");
        }
    }

    private void orgTypeShouldNotBeEmpty(OrgDto request) throws BusinessException {
        if (request.getOrgType().equals("")) {
            throw new BusinessException("组织类别不能为空！");
        }
    }
}