package com.application.corporatemanagement.adapter.driven.persistence.orgmng;


import com.application.corporatemanagement.domain.orgmng.org.Org;
import com.application.corporatemanagement.domain.orgmng.org.OrgStatus;
import com.application.corporatemanagement.domain.orgmng.org.OrgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public class OrgRepositoryJdbc implements OrgRepository {

    private final JdbcTemplate jdbcTemplate;

    private final String table = "org";


    @Autowired
    public OrgRepositoryJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Org> save(Org org) {
        String sql = "insert into " + table + " (`tenant_id`,`org_type_code`,`superior_id`,`leader_id`,`name`,`status`,`created_by`,`last_updated_by`) values(?,?,?,?,?,?,?,?)";
        int update = jdbcTemplate.update(sql, org.getTenant(), org.getOrgType(), org.getSuperior(), org.getLeader(), org.getName(), Status.EFFECTIVE.getValue(), org.getCreatedBy(), org.getLastUpdatedBy());
        if (update > 0) return Optional.of(org);
        return Optional.empty();
    }

    @Override
    public Optional<Org> findByIdAndStatus(Long tenant, Long id, OrgStatus status) {
        return Optional.empty();
    }

    @Override
    public boolean existsBySuperiorAndName(Long tenant, Long id, String name) {
        return false;
    }

    @Override
    public Optional<Org> findById(Long tenant, Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Org> update(Org org) {
        return null;
    }
}
