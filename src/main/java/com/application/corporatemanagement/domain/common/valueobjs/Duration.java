package com.application.corporatemanagement.domain.common.valueobjs;

import java.time.LocalDate;

public record Duration(LocalDate startDate, LocalDate endDate) {
    public boolean isDateOverlap(Duration date) {
        return (this.startDate.isBefore(date.endDate()) && this.endDate.isAfter(date.startDate())) ||
                (date.startDate().isBefore(this.endDate) && date.endDate().isAfter(this.startDate));
    }
}
