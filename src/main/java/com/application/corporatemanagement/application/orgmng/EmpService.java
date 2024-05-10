package com.application.corporatemanagement.application.orgmng;

import com.application.corporatemanagement.domain.orgmng.emp.Emp;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmpService {
    public EmpService() {
    }

    public Optional<Emp> addEmp(AddEmpRequest addEmpRequest, long userId) {
        return Optional.empty();
    }

//    public Optional<Emp> addSkill(AddSkillRequest addSkillRequest) {
//        // init emp aggregate root -> use repository to init
//        // call root's add skill function
//        // use repository to save emp
//        return Optional.empty();
//    }

}
