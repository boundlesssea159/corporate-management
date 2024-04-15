package com.application.corporatemanagement.application.orgmng;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.*;

@Component
class OrgServiceTest {

    @Autowired
    OrgService orgService;

    @Test
    void should_throw_exception_if_verify_fail() {
    }

    @Test
    void should_add_success_if_pass_verify() {

    }
}