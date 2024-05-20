package com.application.corporatemanagement.application.orgmng.emp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
class WorkExperienceRequest {
    @JsonProperty("start_date")
    public LocalDate startDate;

    @JsonProperty("end_date")
    public LocalDate endDate;

    @JsonProperty("company")
    public String company;
}
