package com.application.corporatemanagement.adapter.driven.persistence.orgmng;

import com.application.corporatemanagement.domain.orgmng.emp.Emp;
import com.application.corporatemanagement.domain.orgmng.emp.EmpRepository;
import com.application.corporatemanagement.domain.orgmng.emp.EmpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EmpJdbc implements EmpRepository {
    @Override
    public boolean existsByIdAndStatus(Long tenant, Long id, EmpStatus... status) {
        return false;
    }

    @Override
    public Optional<List<Emp>> findOrgEmps(Long tenant, Long id) {
        return Optional.empty();
    }
}
