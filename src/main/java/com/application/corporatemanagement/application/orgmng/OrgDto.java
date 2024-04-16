package com.application.corporatemanagement.application.orgmng;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class OrgDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("tenant")
    private Long tenant;
    @JsonProperty("leader")
    private Long leader;
    @JsonProperty("superior")
    private Long superior;
    @JsonProperty("org_type")
    private String orgType;
    @JsonProperty("status")
    private String status;
}
