package com.application.corporatemanagement.adapter.driven.persistence.orgmng;

import com.application.corporatemanagement.domain.orgmng.EmpRepository;
import com.application.corporatemanagement.domain.orgmng.EmpStatus;
import org.springframework.stereotype.Repository;

@Repository
public class EmpJdbc implements EmpRepository {
    @Override
    public boolean existsByIdAndStatus(Long tenant, Long id, EmpStatus... status) {
        return false;
    }
}
