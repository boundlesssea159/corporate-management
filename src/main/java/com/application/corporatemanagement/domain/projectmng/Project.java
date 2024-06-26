package com.application.corporatemanagement.domain.projectmng;

import com.application.corporatemanagement.common.framework.AggregateRoot;
import com.application.corporatemanagement.domain.common.valueobjs.Period;
import com.application.corporatemanagement.domain.effortmng.EffortItem;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.*;

@SuperBuilder
@Getter
public class Project extends AggregateRoot implements EffortItem {
    private final Long id;
    private final Long tenantId; // 租户ID
    private final Long effortItemId; // 工时项ID
    private String name; // 项目名称
    private Period period; // 起止时间段
    private ProjectStatus status; // 项目状态
    private Boolean shouldAssignMember; // 是否要分配人员
    private EffortGranularity effoRtGranulArIty; // 工时粒度
    // 子项目
    private Collection<SubProject> subProjects;
    // 项目成员
    private List<Member> members;

    @Override
    public String getName() {
        return name;
    }
}
