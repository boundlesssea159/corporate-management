package com.application.corporatemanagement.domain.orgmng.org.validators;

import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import com.application.corporatemanagement.domain.orgmng.emp.EmpRepository;
import com.application.corporatemanagement.domain.orgmng.emp.EmpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrgLeaderValidator {

    private final EmpRepository empRepository;

    @Autowired
    public OrgLeaderValidator(EmpRepository empRepository) {
        this.empRepository = empRepository;
    }

    public void orgLeaderShouldBeOnWorking(Long tenant, Long leader) {
        if (leader == null || !empRepository.existsByIdAndStatus(tenant, leader, EmpStatus.REGULAR, EmpStatus.PROBATION)) {
            throw new BusinessException("组织负责人(id='" + leader + "')不是在职员工！");
        }
    }
}
