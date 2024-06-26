package com.application.corporatemanagement.domain.projectmng;

import java.util.List;

public interface InnerProjectRepository {
    List<Project> findAssignmentsProjectByEmpId(long empId);

    List<InnerProject> findAll();
}
