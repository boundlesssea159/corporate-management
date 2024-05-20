package com.application.corporatemanagement.application.orgmng.emp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
class SkillRequest {
    @JsonProperty("skill_type")
    public Long skillType;

    @JsonProperty("skill_level")
    public Long skillLevel;

    @JsonProperty("duration")
    public Long duration;

}
