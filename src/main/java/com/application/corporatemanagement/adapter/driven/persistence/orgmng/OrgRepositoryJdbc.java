package com.application.corporatemanagement.adapter.driven.persistence.orgmng;


import com.application.corporatemanagement.domain.orgmng.Org;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class OrgRepositoryJdbc implements com.application.corporatemanagement.domain.orgmng.OrgRepository {

    private final JdbcTemplate jdbcTemplate;

    private final String table = "org";


    @Autowired
    public OrgRepositoryJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Org> save(Org org, Long userId) {
        String sql = "insert into " + table + " (`tenant_id`,`org_type_code`,`superior_id`,`leader_id`,`name`,`status`,`created_by`,`last_updated_by`) values(?,?,?,?,?,?,?,?)";
        int update = jdbcTemplate.update(sql, org.getTenantId(), org.getOrgTypeCode(), org.getSuperiorId(), org.getLeaderId(), org.getName(), Status.EFFECTIVE.getValue(), userId, userId);
        if (update > 0) return Optional.of(org);
        return Optional.empty();
    }
}