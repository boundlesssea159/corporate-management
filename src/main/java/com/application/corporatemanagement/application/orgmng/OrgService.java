package com.application.corporatemanagement.application.orgmng;

import com.application.corporatemanagement.domain.orgmng.Org;
import com.application.corporatemanagement.domain.orgmng.OrgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrgService {

    private final OrgRepository orgRepository;

    @Autowired
    public OrgService(OrgRepository orgRepository) {
        this.orgRepository = orgRepository;
    }

    public Optional<Org> add(OrgDto request, Long userId) {
        Org org = Org.builder()
                .name(request.getName())
                .tenantId(request.getTenantId())
                .leaderId(request.getLeaderId())
                .superiorId(request.getSuperiorId())
                .orgTypeCode(request.getOrgTypeCode())
                .build();
        return orgRepository.save(org, userId);
    }
}
