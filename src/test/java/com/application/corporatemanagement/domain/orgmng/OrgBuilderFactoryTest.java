package com.application.corporatemanagement.domain.orgmng;

import com.application.corporatemanagement.domain.common.validator.TenantValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class OrgBuilderFactoryTest {
    @Test
    void should_create_builder() {
        OrgBuilderFactory orgBuilderFactory = new OrgBuilderFactory(
                Mockito.mock(TenantValidator.class),
                Mockito.mock(OrgTypeValidator.class),
                Mockito.mock(SuperiorValidator.class),
                Mockito.mock(OrgLeaderValidator.class),
                Mockito.mock(OrgNameValidator.class)
        );
        assertNotNull(orgBuilderFactory.create());
    }
}