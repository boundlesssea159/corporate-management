package com.application.corporatemanagement.application.effortmng;

import com.application.corporatemanagement.domain.effortmng.CommonEffortItem;
import com.application.corporatemanagement.domain.effortmng.CommonEffortItemRepository;
import com.application.corporatemanagement.domain.projectmng.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class EffortServiceTest {
    private EffortService effortService;
    private CommonEffortItemRepository commonEffortItemRepository;
    private InnerProjectRepository innerProjectRepository;
    private ClientProjectRepository clientProjectRepository;

    @BeforeEach
    void setUp() {
        commonEffortItemRepository = Mockito.mock(CommonEffortItemRepository.class);
        innerProjectRepository = Mockito.mock(InnerProjectRepository.class);
        clientProjectRepository = Mockito.mock(ClientProjectRepository.class);
        effortService = new EffortService(commonEffortItemRepository, clientProjectRepository, innerProjectRepository);
    }

    @Nested
    class FindAvailableEffortItems {
        @Test
        void should_not_find_effort_items() {
            AvailableEffortItems availableEffortItems = effortService.findAvailableEffortItemsByEmpId(10010L);
            assertEffortItemsIsEmpty(availableEffortItems);
        }

        private void assertEffortItemsIsEmpty(AvailableEffortItems availableEffortItems) {
            List<EffortItemDTO> emptyItems = new ArrayList<>();
            assertEquals(emptyItems, availableEffortItems.innerProjects);
            assertEquals(emptyItems, availableEffortItems.clientProjects);
            assertEquals(emptyItems, availableEffortItems.subProjects);
            assertEquals(emptyItems, availableEffortItems.commonEffortItems);
        }

        @Test
        void should_find_inner_projects_from_emp() {
            when(innerProjectRepository.findAll()).thenReturn(List.of(
                    InnerProject.builder().id(1L).name("project1").shouldAssignMember(false).build(),
                    InnerProject.builder().id(2L).name("project2").shouldAssignMember(true).members(List.of(Member.builder().empId(10010L).build())).build())
            );
            AvailableEffortItems availableEffortItems = effortService.findAvailableEffortItemsByEmpId(10010L);
            List<EffortItemDTO> innerProjects = availableEffortItems.innerProjects;
            assertEffortItemDTOList(innerProjects);
        }

        private void assertEffortItemDTOList(List<EffortItemDTO> innerProjects) {
            assertEquals(2, innerProjects.size());
            assertNotEquals(innerProjects.get(0), innerProjects.get(1));
            innerProjects.forEach(project -> {
                assertTrue(project.equals(EffortItemDTO.builder()
                        .id(1L)
                        .name("project1")
                        .build()) || project.equals(EffortItemDTO.builder()
                        .id(2L)
                        .name("project2")
                        .build()));
            });
        }

        @Test
        void should_not_find_inner_project_from_emp() {
            when(innerProjectRepository.findAll()).thenReturn(List.of(
                    InnerProject.builder().id(1L).name("project1").shouldAssignMember(true).build()
            ));
            assertEffortItemsIsEmpty(effortService.findAvailableEffortItemsByEmpId(10010L));
        }

        @Test
        void should_find_client_projects_from_emp() {
            when(clientProjectRepository.findAll()).thenReturn(List.of(
                    ClientProject.builder().id(1L).name("project1").shouldAssignMember(false).build(),
                    ClientProject.builder().id(2L).name("project2").shouldAssignMember(true).members(List.of(Member.builder().empId(10010L).build())).build())
            );
            AvailableEffortItems availableEffortItems = effortService.findAvailableEffortItemsByEmpId(10010L);
            assertEffortItemDTOList(availableEffortItems.clientProjects);
        }

        @Test
        void should_not_find_client_project_from_emp() {
            when(clientProjectRepository.findAll()).thenReturn(List.of(
                    ClientProject.builder().id(1L).name("project1").shouldAssignMember(true).build()
            ));
            assertEffortItemsIsEmpty(effortService.findAvailableEffortItemsByEmpId(10010L));
        }

        @Test
        void should_find_common_effort_items_from_emp() {
            when(commonEffortItemRepository.findAll()).thenReturn(List.of(
                    CommonEffortItem.builder()
                            .id(1L)
                            .name("project")
                            .build()
            ));
            AvailableEffortItems availableEffortItems = effortService.findAvailableEffortItemsByEmpId(10010L);
            assertEquals(1, availableEffortItems.commonEffortItems.size());
            assertEquals(EffortItemDTO.builder().id(1L).name("project").build(), availableEffortItems.commonEffortItems.get(0));
        }

    }


}