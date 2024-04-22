package com.application.corporatemanagement.application.orgmng;

import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import com.application.corporatemanagement.domain.orgmng.org.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrgService {
    private final OrgRepository orgRepository;
    private final OrgBuilderFactory orgBuilderFactory;
    private final OrgHandler orgHandler;

    @Autowired
    public OrgService(OrgRepository orgRepository, OrgBuilderFactory orgBuilderFactory, OrgHandler orgHandler) {
        this.orgRepository = orgRepository;
        this.orgBuilderFactory = orgBuilderFactory;
        this.orgHandler = orgHandler;
    }

    public Optional<OrgResponse> add(CreateOrgRequest request, Long userId) {
        OrgBuilder orgBuilder = orgBuilderFactory.create();
        Org org = orgBuilder
                .name(request.getName())
                .orgType(request.getOrgType())
                .leader(request.getLeader())
                .tenant(request.getTenant())
                .status(request.getStatus())
                .superior(request.getSuperior())
                .build();
        return orgRepository.save(org, userId).flatMap(OrgService::buildOrgResponse);
    }

    public Optional<OrgResponse> update(UpdateOrgRequest updateOrgDto, Long userId) {
        Org org = orgRepository.findById(updateOrgDto.getTenant(), updateOrgDto.getId())
                .orElseThrow(() -> new BusinessException("要修改的组织（id=" + updateOrgDto.getId() + "）不存在！"));
        orgHandler.update(org, updateOrgDto.getName(), updateOrgDto.getSuperior());
        return orgRepository.update(org, userId).flatMap(OrgService::buildOrgResponse);
    }

    private static Optional<OrgResponse> buildOrgResponse(Org org) {
        return Optional.of(
                OrgResponse.builder()
                        .id(org.getId())
                        .name(org.getName())
                        .superior(org.getSuperior())
                        .build()
        );
    }
}