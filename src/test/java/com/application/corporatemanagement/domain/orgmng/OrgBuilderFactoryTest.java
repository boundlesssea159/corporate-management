package com.application.corporatemanagement.domain.orgmng;

import com.application.corporatemanagement.domain.common.validator.TenantValidator;
import com.application.corporatemanagement.domain.orgmng.org.*;
import com.application.corporatemanagement.domain.orgmng.org.validators.OrgLeaderValidator;
import com.application.corporatemanagement.domain.orgmng.org.validators.OrgNameValidator;
import com.application.corporatemanagement.domain.orgmng.org.validators.OrgTypeValidator;
import com.application.corporatemanagement.domain.orgmng.org.validators.SuperiorValidator;
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