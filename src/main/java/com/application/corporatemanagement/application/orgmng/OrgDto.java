package com.application.corporatemanagement.application.orgmng;

import com.application.corporatemanagement.domain.orgmng.Org;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.service.annotation.GetExchange;

import javax.management.ConstructorParameters;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OrgDto {
    private String name;
    @JsonProperty("tenant_id")
    private Long tenantId;
    @JsonProperty("leader_id")
    private Long leaderId;
    @JsonProperty("superior_id")
    private Long superiorId;
    @JsonProperty("org_type_code")
    private String orgTypeCode;
    @JsonProperty("status")
    private String status;
}
