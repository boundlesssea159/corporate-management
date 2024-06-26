package com.application.corporatemanagement.domain.projectmng.innerproject;

import com.application.corporatemanagement.domain.projectmng.common.project.Project;

import java.util.List;

public interface InnerProjectRepository {
    List<Project> findAssignmentsProjectByEmpId(long empId);

    List<InnerProject> findAll();
}
