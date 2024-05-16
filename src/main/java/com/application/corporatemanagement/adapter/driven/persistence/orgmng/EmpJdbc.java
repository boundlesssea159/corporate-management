package com.application.corporatemanagement.adapter.driven.persistence.orgmng;

import com.application.corporatemanagement.domain.common.exceptions.DirtyDataException;
import com.application.corporatemanagement.domain.orgmng.emp.Emp;
import com.application.corporatemanagement.domain.orgmng.emp.EmpRepository;
import com.application.corporatemanagement.domain.orgmng.emp.EmpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EmpJdbc implements EmpRepository {

    private final JdbcTemplate jdbcTemplate;

    public EmpJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean existsByIdAndStatus(Long tenant, Long id, EmpStatus... status) {
        return false;
    }

    @Override
    public Optional<List<Emp>> findOrgEmps(Long tenant, Long orgId) {
        return Optional.empty();
    }

    @Override
    public void save(Emp emp, long userId) {
    }

    public Optional<Emp> findById(Long id) {
        try {
            Emp emp = jdbcTemplate.queryForObject("select * from emp where id=?", (rs, rowNum) ->
                            Emp.builder()
                                    .tenant(rs.getLong("tenant_id"))
                                    .name(rs.getString("name"))
                                    .orgId(rs.getLong("org"))
                                    .status(EmpStatus.getBy(rs.getString("status")).get())
                                    .build()
                    , id);
            return Optional.ofNullable(emp);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
