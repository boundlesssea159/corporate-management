package com.application.corporatemanagement.domain.orgmng.emp;

public interface EmpRepository {

    boolean existsByIdAndStatus(Long tenant, Long id, EmpStatus... status);
}
