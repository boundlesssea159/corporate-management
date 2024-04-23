package com.application.corporatemanagement.application.orgmng;

import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import com.application.corporatemanagement.domain.orgmng.org.*;
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
    protected OrgHandler orgHandler;
    private OrgService orgService;
    private final Long userId = 1L;

    private CreateOrgRequest orgDto;

    @BeforeEach
    void setUp() {
        orgDto = CreateOrgRequest.builder().name("org").build();
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
        orgHandler = Mockito.mock(OrgHandler.class);
        orgService = new OrgService(orgRepository, orgBuilderFactory, orgHandler);
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
        Optional<OrgResponse> result = orgService.add(orgDto, userId);
        assertTrue(result.isPresent());
        assertEquals(orgDto.getName(), result.get().getName());
    }

    @Test
    void should_update_org_success() {
        UpdateOrgRequest updateOrgDto = UpdateOrgRequest.builder().id(1L).tenant(2L).name("updated name").superior(3L).build();
        Org org = Org.builder().id(updateOrgDto.getId()).tenant(updateOrgDto.getTenant()).build();
        when(orgRepository.findById(updateOrgDto.getTenant(), updateOrgDto.getId())).thenReturn(Optional.of(org));
        doAnswer(invocation -> {
            Object argumentOne = invocation.getArgument(0);
            assert argumentOne instanceof Org;
            ((Org) argumentOne).name(invocation.getArgument(1));
            return null;
        }).when(orgHandler).update(org, "updated name", updateOrgDto.getSuperior());
        when(orgRepository.update(org, userId)).thenReturn(Optional.of(
                Org.builder().id(updateOrgDto.getId()).tenant(updateOrgDto.getTenant()).name("updated name").superior(updateOrgDto.getSuperior()).build()
        ));
        Optional<OrgResponse> optionalOrgResponse = orgService.update(updateOrgDto, userId);
        assertTrue(optionalOrgResponse.isPresent());
        OrgResponse orgResponse = optionalOrgResponse.get();
        assertEquals("updated name", orgResponse.getName());
        assertEquals(updateOrgDto.getSuperior(), orgResponse.getSuperior());
    }

//    @Test
//    void should_throw_exception_if_org_is_not_exist() {
//
//    }


}