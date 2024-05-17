package com.application.corporatemanagement.application.orgmng.emp;

import com.application.corporatemanagement.domain.orgmng.emp.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class EmpServiceTest {
    protected EmpRepository empRepository;
    protected EmpAssembler empAssembler;
    private EmpService empService;

    @BeforeEach
    void setUp() {
        empRepository = Mockito.mock(EmpRepository.class);
        empAssembler = Mockito.mock(EmpAssembler.class);
        empService = new EmpService(empRepository, empAssembler);
    }

    @Test
    void should_add_emp() {
        AddEmpRequest request = AddEmpRequest.builder().build();
        empService.addEmp(request, 10010L);
        verify(empAssembler).fromCreateRequest(request);
        verify(empRepository).save(any(),anyLong());
    }

    // todo update emp
}