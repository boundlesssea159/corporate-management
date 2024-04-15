package com.application.corporatemanagement.domain.orgmng;

public interface EmpRepository {

    boolean existsByIdAndStatus(Long tenant, Long id, EmpStatus... status);
}
