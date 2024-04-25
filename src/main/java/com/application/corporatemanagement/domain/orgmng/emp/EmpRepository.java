package com.application.corporatemanagement.domain.orgmng.emp;

import java.util.List;
import java.util.Optional;

public interface EmpRepository {

    boolean existsByIdAndStatus(Long tenant, Long id, EmpStatus... status);

    Optional<List<Emp>> findOrgEmps(Long tenant, Long id);
}
