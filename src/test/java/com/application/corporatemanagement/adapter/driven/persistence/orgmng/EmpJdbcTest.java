package com.application.corporatemanagement.adapter.driven.persistence.orgmng;

import com.application.corporatemanagement.domain.orgmng.emp.Emp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmpJdbcTest {

    @Autowired
    private EmpJdbc empJdbc;

    @Spy
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    void should_find_empty() {
        Optional<Emp> optionalEmp = empJdbc.findById(1L);
        assertTrue(optionalEmp.isEmpty());
    }
}