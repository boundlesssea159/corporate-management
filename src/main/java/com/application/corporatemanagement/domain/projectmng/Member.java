package com.application.corporatemanagement.domain.projectmng;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Member {
    private Long projectId;
    private Long empId;
}
