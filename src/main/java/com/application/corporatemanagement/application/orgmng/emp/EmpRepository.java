package com.application.corporatemanagement.application.orgmng.emp;

import com.application.corporatemanagement.domain.orgmng.emp.Emp;
import com.application.corporatemanagement.domain.orgmng.emp.EmpStatus;

import java.util.List;
import java.util.Optional;

public interface EmpRepository {

    boolean existsByIdAndStatus(Long tenant, Long id, EmpStatus... status);

    Optional<List<Emp>> findOrgEmps(Long tenant, Long orgId);

    void save(Emp emp, long userId);

    Optional<Emp> findById(Long tenant, Long empId);

    void update(Emp emp, Long userId);
}
