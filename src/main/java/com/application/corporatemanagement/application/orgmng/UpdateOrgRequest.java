package com.application.corporatemanagement.application.orgmng;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UpdateOrgRequest {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("tenant")
    private Long tenant;

    @JsonProperty("name")
    private String name;

    @JsonProperty("superior")
    private Long superior;
}
