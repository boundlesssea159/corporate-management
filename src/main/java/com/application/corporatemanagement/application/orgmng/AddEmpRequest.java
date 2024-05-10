package com.application.corporatemanagement.application.orgmng;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class AddEmpRequest {

    @JsonProperty("tenant")
    public Long tenant;

    @JsonProperty("name")
    public String name;

    @JsonProperty("skills")
    public List<Skill> skills;

    @JsonProperty("workExperiences")
    public List<WorkExperience> workExperiences;

    @JsonProperty("org_id")
    public Long orgId;

    @JsonProperty("post_codes")
    public List<Long> postCodes;

    @Builder
    @Getter
    static class Skill {
        @JsonProperty("skill_type")
        public Long skillType;

        @JsonProperty("skill_level")
        public Long skillLevel;

        @JsonProperty("duration")
        public Long duration;

    }

    @Builder
    @Getter
    static class WorkExperience {
        @JsonProperty("start_date")
        public LocalDate startDate;

        @JsonProperty("end_date")
        public LocalDate endDate;

        @JsonProperty("company")
        public String company;
    }
}
