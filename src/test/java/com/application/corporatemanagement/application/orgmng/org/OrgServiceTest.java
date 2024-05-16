package com.application.corporatemanagement.application.orgmng.org;

import com.application.corporatemanagement.application.orgmng.org.CreateOrgRequest;
import com.application.corporatemanagement.application.orgmng.org.OrgResponse;
import com.application.corporatemanagement.application.orgmng.org.OrgService;
import com.application.corporatemanagement.application.orgmng.org.UpdateOrgRequest;
import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import com.application.corporatemanagement.domain.orgmng.org.*;
import com.application.corporatemanagement.domain.orgmng.org.validators.CancelValidator;
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
    protected CancelValidator cancelValidator;
    private OrgService orgService;
    private final Long userId = 1L;

    private CreateOrgRequest orgDto;

    @BeforeEach
    void setUp() {
        orgDto = CreateOrgRequest.builder().tenant(1L).id(100L).name("org").status(OrgStatus.EFFECTIVE.getText()).build();
        orgRepository = Mockito.mock(OrgRepository.class);
        orgBuilder = Mockito.mock(OrgBuilder.class);
        stubOrgBuilder();
        orgBuilderFactory = mock(OrgBuilderFactory.class);
        when(orgBuilderFactory.create()).thenReturn(orgBuilder);
        orgHandler = Mockito.mock(OrgHandler.class);
        cancelValidator = Mockito.mock(CancelValidator.class);
        orgService = new OrgService(orgRepository, orgBuilderFactory, orgHandler, cancelValidator);
    }

    private void stubOrgBuilder() {
        when(orgBuilder.orgType(orgDto.getOrgType())).thenReturn(orgBuilder);
        when(orgBuilder.leader(orgDto.getLeader())).thenReturn(orgBuilder);
        when(orgBuilder.name(orgDto.getName())).thenReturn(orgBuilder);
        when(orgBuilder.tenant(orgDto.getTenant())).thenReturn(orgBuilder);
        when(orgBuilder.status(orgDto.getStatus())).thenReturn(orgBuilder);
        when(orgBuilder.superior(orgDto.getSuperior())).thenReturn(orgBuilder);
        when(orgBuilder.creator(any())).thenReturn(orgBuilder);
        when(orgBuilder.createAt(any())).thenReturn(orgBuilder);
    }

    @Test
    void should_throw_exception_if_verify_fail() {
        doThrow(new BusinessException("check fail")).when(orgBuilder).build();
        assertThrows(BusinessException.class, () -> orgService.add(orgDto, userId));
    }

    @Test
    void should_add_success_if_pass_verify() {
        Org org = Org.builder().name(orgDto.getName()).status(OrgStatus.EFFECTIVE).build();
        when(orgBuilder.build()).thenReturn(org);
        when(orgRepository.save(any())).then(invocation -> Optional.of(invocation.getArgument(0)));
        Optional<OrgResponse> result = orgService.add(orgDto, userId);
        assertTrue(result.isPresent());
        assertEquals(orgDto.getName(), result.get().getName());
    }

    @Test
    void should_update_org_success() {
        UpdateOrgRequest updateOrgDto = UpdateOrgRequest.builder().id(1L).tenant(2L).name("updated name").superior(3L).build();
        Org org = Org.builder().id(updateOrgDto.getId()).tenant(updateOrgDto.getTenant()).status(OrgStatus.EFFECTIVE).build();
        when(orgRepository.findById(updateOrgDto.getTenant(), updateOrgDto.getId())).thenReturn(Optional.of(org));
        doAnswer(invocation -> {
            Object argumentOne = invocation.getArgument(0);
            assert argumentOne instanceof Org;
            ((Org) argumentOne).name(invocation.getArgument(1));
            return null;
        }).when(orgHandler).update(org, "updated name", updateOrgDto.getSuperior(), 10010L);
        when(orgRepository.update(org)).thenReturn(Optional.of(
                Org.builder().id(updateOrgDto.getId()).tenant(updateOrgDto.getTenant()).name("updated name").superior(updateOrgDto.getSuperior()).status(OrgStatus.EFFECTIVE).build()
        ));
        Optional<OrgResponse> optionalOrgResponse = orgService.update(updateOrgDto, userId);
        assertTrue(optionalOrgResponse.isPresent());
        OrgResponse orgResponse = optionalOrgResponse.get();
        assertEquals("updated name", orgResponse.getName());
        assertEquals(updateOrgDto.getSuperior(), orgResponse.getSuperior());
    }

    @Test
    void should_throw_exception_if_org_is_not_exist() {
        UpdateOrgRequest updateOrgDto = UpdateOrgRequest.builder().id(1L).tenant(2L).name("updated name").superior(3L).build();
        when(orgRepository.findById(updateOrgDto.getTenant(), updateOrgDto.getId())).thenThrow(BusinessException.class);
        assertThrows(BusinessException.class, () -> orgService.update(updateOrgDto, userId));
    }


    @Test
    void should_cancel_success() {
        // todo should function return Org? since it is terrible that verify() is called too many times.
        Org org = Mockito.mock(Org.class);
        when(org.getId()).thenReturn(orgDto.getId());
        when(org.updator(any())).thenReturn(org);
        when(org.updatedAt(any())).thenReturn(org);
        when(orgRepository.findById(orgDto.getTenant(), orgDto.getId())).thenReturn(Optional.of(org));
        assertEquals(orgDto.getId(), orgService.cancel(orgDto.getTenant(), orgDto.getId(), userId));
        verify(org).updatedAt(any());
        verify(org).cancel();
        verify(org).updator(userId);
        verify(orgRepository).update(org);
    }


    @Test
    void should_throw_exception_if_org_not_exist() {
        when(orgRepository.findById(orgDto.getTenant(), orgDto.getId())).thenThrow(BusinessException.class);
        assertThrows(BusinessException.class, () -> orgService.cancel(orgDto.getTenant(), orgDto.getId(), userId));
    }

    @Test
    void should_throw_exception_if_cancel_check_fail() {
        doThrow(BusinessException.class).when(cancelValidator).validate(any());
        assertThrows(BusinessException.class, () -> orgService.cancel(orgDto.getTenant(), orgDto.getId(), userId));
    }
}