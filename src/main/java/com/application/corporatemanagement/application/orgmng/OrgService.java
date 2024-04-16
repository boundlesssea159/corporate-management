package com.application.corporatemanagement.application.orgmng;

import com.application.corporatemanagement.domain.orgmng.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrgService {

    private final OrgRepository orgRepository;

    private final OrgValidator orgValidator;

    @Autowired
    public OrgService(OrgRepository orgRepository, OrgValidator orgValidator) {
        this.orgRepository = orgRepository;
        this.orgValidator = orgValidator;
    }

    public Optional<Org> add(OrgDto request, Long userId) {
        orgValidator.validate(request);
        Org org = Org.builder()
                .name(request.getName())
                .tenantId(request.getTenant())
                .leaderId(request.getLeader())
                .superiorId(request.getSuperior())
                .orgType(request.getOrgType())
                .build();
        return orgRepository.save(org, userId);
    }
}