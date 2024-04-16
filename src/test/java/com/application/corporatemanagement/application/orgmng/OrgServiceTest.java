package com.application.corporatemanagement.application.orgmng;

import com.application.corporatemanagement.domain.exceptions.BusinessException;
import com.application.corporatemanagement.domain.orgmng.Org;
import com.application.corporatemanagement.domain.orgmng.OrgRepository;
import com.application.corporatemanagement.domain.orgmng.OrgValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class OrgServiceTest {
    protected OrgValidator orgValidator;
    protected OrgRepository orgRepository;
    private OrgService orgService;
    private final Long userId = 1L;

    @BeforeEach
    void setUp() {
        orgValidator = Mockito.mock(OrgValidator.class);
        orgRepository = Mockito.mock(OrgRepository.class);
        orgService = new OrgService(orgRepository, orgValidator);
    }

    @Test
    void should_throw_exception_if_verify_fail() {
        OrgDto orgDto = OrgDto.builder().build();
        doThrow(new BusinessException("check fail")).when(orgValidator).validate(orgDto);
        assertThrows(BusinessException.class, () -> orgService.add(orgDto, userId));
    }

    @Test
    void should_add_success_if_pass_verify() {
        OrgDto orgDto = OrgDto.builder()
                .name("org")
                .build();
        when(orgRepository.save(any(), any())).then(invocation -> Optional.of(invocation.getArgument(0)));
        Optional<Org> result = orgService.add(orgDto, userId);
        assertTrue(result.isPresent());
        assertEquals(orgDto.getName(), result.get().getName());
    }
}