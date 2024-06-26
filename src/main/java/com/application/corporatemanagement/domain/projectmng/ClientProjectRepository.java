package com.application.corporatemanagement.domain.projectmng;

import java.util.List;

public interface ClientProjectRepository {
    List<Project> findAssignmentsProjectByEmpId(long empId);

    List<ClientProject> findAll();
}
