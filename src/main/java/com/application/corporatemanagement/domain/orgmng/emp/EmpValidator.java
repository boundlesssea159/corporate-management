package com.application.corporatemanagement.domain.orgmng.emp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpValidator {

    private final EmpRepository empRepository;

    @Autowired
    public EmpValidator(EmpRepository empRepository) {
        this.empRepository = empRepository;
    }

}
