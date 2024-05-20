package com.application.corporatemanagement.application.orgmng.emp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AddEmpRequest {

    @JsonProperty("tenant")
    public Long tenant;

    @JsonProperty("name")
    public String name;

    @JsonProperty("skills")
    public List<SkillRequest> skills;

    @JsonProperty("workExperiences")
    public List<WorkExperienceRequest> workExperiences;

    @JsonProperty("org_id")
    public Long orgId;

    @JsonProperty("post_codes")
    public List<Long> postCodes;
}
