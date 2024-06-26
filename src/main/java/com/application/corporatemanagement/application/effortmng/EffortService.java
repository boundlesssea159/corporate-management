package com.application.corporatemanagement.application.effortmng;

import com.application.corporatemanagement.domain.effortmng.CommonEffortItem;
import com.application.corporatemanagement.domain.effortmng.CommonEffortItemRepository;
import com.application.corporatemanagement.domain.projectmng.clientproject.ClientProjectRepository;
import com.application.corporatemanagement.domain.projectmng.innerproject.InnerProjectRepository;
import com.application.corporatemanagement.domain.projectmng.common.project.Project;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

@Getter
public class EffortService {

    private final CommonEffortItemRepository commonEffortItemRepository;

    private final ClientProjectRepository clientProjectRepository;

    private final InnerProjectRepository innerProjectRepository;

    public EffortService(CommonEffortItemRepository effortItemRepository, ClientProjectRepository clientProjectRepository, InnerProjectRepository innerProjectRepository) {
        this.commonEffortItemRepository = effortItemRepository;
        this.clientProjectRepository = clientProjectRepository;
        this.innerProjectRepository = innerProjectRepository;
    }

    public AvailableEffortItems findAvailableEffortItemsByEmpId(long empId) {
        // available projects contains: assignments projects and no-member projects
        List<Project> innerAvailableProjects = getAvailableProjects(empId, innerProjectRepository.findAll().stream().map(Project.class::cast).toList());
        List<Project> clientAvailableProjects = getAvailableProjects(empId, clientProjectRepository.findAll().stream().map(Project.class::cast).toList());
        List<CommonEffortItem> commonEffortItems = commonEffortItemRepository.findAll();
        AvailableEffortItems availableEffortItems = new AvailableEffortItems();
        addEffortItems(availableEffortItems, innerAvailableProjects, EffortItemType.INNER_PROJECT);
        addEffortItems(availableEffortItems, clientAvailableProjects, EffortItemType.CLIENT_PROJECT);
        commonEffortItems.forEach(commonEffortItem -> availableEffortItems.addEffortItem(EffortItemType.COMMON_EFFORT_ITEM, commonEffortItem.getId(), commonEffortItem.getName()));
        return availableEffortItems;
    }

    private List<Project> getAvailableProjects(long empId, List<Project> projects) {
        return projects.stream().filter(
                project -> !project.getShouldAssignMember()
                        || Optional.ofNullable(project.getMembers())
                        .map(members -> members.stream().anyMatch(member -> member.getEmpId().equals(empId)))
                        .orElse(false)
        ).toList();
    }

    private void addEffortItems(AvailableEffortItems availableEffortItems, List<Project> innerAvailableProjects, EffortItemType innerProject) {
        innerAvailableProjects.forEach(project -> availableEffortItems.addEffortItem(innerProject, project.getId(), project.getName()));
    }

    public enum EffortItemType {
        INNER_PROJECT,
        CLIENT_PROJECT,
        SUB_PROJECT,
        COMMON_EFFORT_ITEM
    }
}
