package com.application.corporatemanagement.application.orgmng.emp;

import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import com.application.corporatemanagement.domain.orgmng.emp.Emp;
import com.application.corporatemanagement.domain.orgmng.emp.EmpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpService {

    private final EmpRepository empRepository;

    private final EmpAssembler assembler;

    @Autowired
    public EmpService(EmpRepository empRepository, EmpAssembler assembler) {
        this.empRepository = empRepository;
        this.assembler = assembler;
    }

    public void addEmp(AddEmpRequest addEmpRequest, Long userId) {
        empRepository.save(assembler.fromCreateRequest(addEmpRequest, userId));
    }

    @Transactional
    public boolean updateSkill(UpdateSkillRequest request, Long userId) {
        Emp emp = empRepository.findById(request.tenant, request.empId).orElseThrow(() -> new BusinessException("员工不存在"));
        request.skills.forEach(skill -> emp.updateSkill(skill.getSkillType(), skill.getSkillLevel(), skill.getDuration(), userId));
        empRepository.update(emp);
        return true;
    }
}
