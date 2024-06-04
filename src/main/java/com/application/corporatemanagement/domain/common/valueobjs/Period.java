package com.application.corporatemanagement.domain.common.valueobjs;

import com.application.corporatemanagement.domain.common.exceptions.BusinessException;

import java.time.LocalDate;

public class Period {
    protected final LocalDate startDate;
    protected final LocalDate endDate;

    public Period(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new BusinessException("开始时间不能大于结束时间");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isOverlap(Period date) {
        return (this.startDate.isBefore(date.endDate()) && this.endDate.isAfter(date.startDate())) ||
                (date.startDate().isBefore(this.endDate) && date.endDate().isAfter(this.startDate));
    }

    public LocalDate startDate() {
        return startDate;
    }

    public LocalDate endDate() {
        return endDate;
    }
}