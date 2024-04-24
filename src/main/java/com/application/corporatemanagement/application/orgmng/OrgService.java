package com.application.corporatemanagement.application.orgmng;

import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import com.application.corporatemanagement.domain.orgmng.org.*;
import com.application.corporatemanagement.domain.orgmng.org.validators.CancelValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrgService {
    private final OrgRepository orgRepository;
    private final OrgBuilderFactory orgBuilderFactory;
    private final OrgHandler orgHandler;

    private final CancelValidator cancelValidator;

    @Autowired
    public OrgService(OrgRepository orgRepository, OrgBuilderFactory orgBuilderFactory, OrgHandler orgHandler, CancelValidator cancelValidator) {
        this.orgRepository = orgRepository;
        this.orgBuilderFactory = orgBuilderFactory;
        this.orgHandler = orgHandler;
        this.cancelValidator = cancelValidator;
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
                .createAt(LocalDateTime.now())
                .creator(userId)
                .build();
        return orgRepository.save(org).flatMap(OrgService::buildOrgResponse);
    }

    public Optional<OrgResponse> update(UpdateOrgRequest updateOrgDto, Long userId) {
        Org org = findOrg(updateOrgDto.getTenant(), updateOrgDto.getId());
        orgHandler.update(org, updateOrgDto.getName(), updateOrgDto.getSuperior(), userId);
        return orgRepository.update(org).flatMap(OrgService::buildOrgResponse);
    }

    private Org findOrg(Long tenant, Long id) {
        return orgRepository.findById(tenant, id)
                .orElseThrow(() -> new BusinessException("组织（id=" + id + "）不存在！"));
    }

    private static Optional<OrgResponse> buildOrgResponse(Org org) {
        return Optional.of(
                OrgResponse.builder()
                        .id(org.getId())
                        .name(org.getName())
                        .orgType(org.getOrgType())
                        .leader(org.getLeader())
                        .tenant(org.getTenant())
                        .status(org.getStatus().getValue())
                        .superior(org.getSuperior())
                        .build()
        );
    }

    public Long cancel(Long tenant, Long id, long userId) {
        Org org = findOrg(tenant, id);
        cancelValidator.validate();
        org.updator(userId);
        org.updatedAt(LocalDateTime.now());
        org.cancel();
        orgRepository.update(org);
        return org.getId();
    }
}