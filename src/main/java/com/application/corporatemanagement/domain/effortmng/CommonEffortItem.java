package com.application.corporatemanagement.domain.effortmng;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CommonEffortItem implements EffortItem {
    private Long id;
    private String name;
    private List<EffortRecord> effortRecords;

    @Override
    public String getName() {
        return name;
    }
}
