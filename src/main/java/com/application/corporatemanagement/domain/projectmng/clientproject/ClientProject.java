package com.application.corporatemanagement.domain.projectmng.clientproject;

import com.application.corporatemanagement.domain.common.valueobjs.Period;
import com.application.corporatemanagement.domain.projectmng.common.project.Project;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@SuperBuilder
public class ClientProject extends Project {

    // 项目经理
    private Map<Period, ProjectMng> clientMngs;
}
