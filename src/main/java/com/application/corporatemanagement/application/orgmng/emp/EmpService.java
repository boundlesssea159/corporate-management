package com.application.corporatemanagement.application.orgmng.emp;

import com.application.corporatemanagement.domain.orgmng.emp.EmpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpService {

    private final EmpRepository empRepository;

    private final EmpAssembler assembler;

    @Autowired
    public EmpService(EmpRepository empRepository, EmpAssembler assembler) {
        this.empRepository = empRepository;
        this.assembler = assembler;
    }

    public void addEmp(AddEmpRequest addEmpRequest, long userId) {
        empRepository.save(assembler.fromCreateRequest(addEmpRequest), userId);
    }
}
