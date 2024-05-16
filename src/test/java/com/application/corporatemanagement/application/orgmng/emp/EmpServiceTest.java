package com.application.corporatemanagement.application.orgmng.emp;

import com.application.corporatemanagement.application.orgmng.emp.AddEmpRequest;
import com.application.corporatemanagement.application.orgmng.emp.EmpService;
import com.application.corporatemanagement.domain.orgmng.emp.*;
import lombok.Data;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
        Emp emp = Emp.builder().build();
        when(empAssembler.fromCreateRequest(any())).thenReturn(emp);
        empService.addEmp(AddEmpRequest.builder().build(), 10010L);
        verify(empRepository).save(emp, 10010L);
    }

    // todo update emp
}