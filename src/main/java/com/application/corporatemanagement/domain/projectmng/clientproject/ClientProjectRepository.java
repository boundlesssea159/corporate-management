package com.application.corporatemanagement.domain.projectmng.clientproject;

import com.application.corporatemanagement.domain.projectmng.common.project.Project;

import java.util.List;

public interface ClientProjectRepository {
    List<Project> findAssignmentsProjectByEmpId(long empId);

    List<ClientProject> findAll();
}
