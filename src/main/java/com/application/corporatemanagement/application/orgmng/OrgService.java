package com.application.corporatemanagement.application.orgmng;

import com.application.corporatemanagement.domain.orgmng.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrgService {
    private final OrgRepository orgRepository;
    private final OrgBuilderFactory orgBuilderFactory;

    @Autowired
    public OrgService(OrgRepository orgRepository, OrgBuilderFactory orgBuilderFactory) {
        this.orgRepository = orgRepository;
        this.orgBuilderFactory = orgBuilderFactory;
    }

    public Optional<Org> add(OrgDto request, Long userId) {
        OrgBuilder orgBuilder = orgBuilderFactory.create();
        Org org = orgBuilder
                .name(request.getName())
                .orgType(request.getOrgType())
                .leader(request.getLeader())
                .tenant(request.getTenant())
                .status(request.getStatus())
                .superior(request.getSuperior())
                .build();
        return orgRepository.save(org, userId);
    }
}