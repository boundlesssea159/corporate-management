package com.application.corporatemanagement.domain.orgmng.org.validators;

import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import com.application.corporatemanagement.domain.orgmng.emp.Emp;
import com.application.corporatemanagement.application.orgmng.emp.EmpRepository;
import com.application.corporatemanagement.domain.orgmng.org.Org;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CancelValidator {

    private final EmpRepository empRepository;

    @Autowired
    public CancelValidator(EmpRepository empRepository) {
        this.empRepository = empRepository;
    }

    public void validate(Org org) {
        canceledOrgShouldBeEffective(org);
        canceledOrgShouldHaveNoEmp(org);
    }

    private void canceledOrgShouldBeEffective(Org org) {
        if (!org.isEffective()) {
            throw new BusinessException("该组织不是有效状态，不能撤销");
        }
    }

    private void canceledOrgShouldHaveNoEmp(Org org) {
        Optional<List<Emp>> emps = empRepository.findOrgEmps(org.getTenant(), org.getId());
        if (emps.isEmpty()) {
            throw new BusinessException("该组织下还有员工不能撤销");
        }
    }
}
