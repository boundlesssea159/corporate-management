package com.application.corporatemanagement.domain.common.valueobjs;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Duration {
    private final LocalDate startDate;

    private final LocalDate endDate;


    public Duration(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
