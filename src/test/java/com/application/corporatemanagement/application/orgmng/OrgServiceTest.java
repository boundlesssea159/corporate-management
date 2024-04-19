package com.application.corporatemanagement.application.orgmng;

import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import com.application.corporatemanagement.domain.orgmng.Org;
import com.application.corporatemanagement.domain.orgmng.OrgBuilder;
import com.application.corporatemanagement.domain.orgmng.OrgBuilderFactory;
import com.application.corporatemanagement.domain.orgmng.OrgRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrgServiceTest {
    protected OrgRepository orgRepository;
    protected OrgBuilder orgBuilder;
    protected OrgBuilderFactory orgBuilderFactory;
    private OrgService orgService;
    private final Long userId = 1L;

    private OrgDto orgDto;

    @BeforeEach
    void setUp() {
        orgDto = OrgDto.builder().name("org").build();
        orgRepository = Mockito.mock(OrgRepository.class);
        orgBuilder = Mockito.mock(OrgBuilder.class);
        when(orgBuilder.orgType(orgDto.getOrgType())).thenReturn(orgBuilder);
        when(orgBuilder.leader(orgDto.getLeader())).thenReturn(orgBuilder);
        when(orgBuilder.name(orgDto.getName())).thenReturn(orgBuilder);
        when(orgBuilder.tenant(orgDto.getTenant())).thenReturn(orgBuilder);
        when(orgBuilder.status(orgDto.getStatus())).thenReturn(orgBuilder);
        when(orgBuilder.superior(orgDto.getSuperior())).thenReturn(orgBuilder);
        orgBuilderFactory = mock(OrgBuilderFactory.class);
        when(orgBuilderFactory.create()).thenReturn(orgBuilder);
        orgService = new OrgService(orgRepository, orgBuilderFactory);
    }

    @Test
    void should_throw_exception_if_verify_fail() {
        doThrow(new BusinessException("check fail")).when(orgBuilder).build();
        assertThrows(BusinessException.class, () -> orgService.add(orgDto, userId));
    }

    @Test
    void should_add_success_if_pass_verify() {
        Org org = Org.builder()
                .name(orgDto.getName())
                .build();
        when(orgBuilder.build()).thenReturn(org);
        when(orgRepository.save(any(), any())).then(invocation -> Optional.of(invocation.getArgument(0)));
        Optional<Org> result = orgService.add(orgDto, userId);
        assertTrue(result.isPresent());
        assertEquals(orgDto.getName(), result.get().getName());
    }
}