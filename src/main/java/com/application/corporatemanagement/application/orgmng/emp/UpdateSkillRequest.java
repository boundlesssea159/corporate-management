package com.application.corporatemanagement.application.orgmng.emp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public class UpdateSkillRequest {
    @JsonProperty("tenant")
    public Long tenant;
    @JsonProperty("emp_id")
    public Long empId;
    @JsonProperty("skills")
    public List<SkillRequest> skills;
}
