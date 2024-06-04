package com.application.corporatemanagement.domain.common.valueobjs;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DurationTest {
    @Test
    void should_return_false_if_date_overlap() {
        Duration duration = new Duration(LocalDate.now().minusYears(3), LocalDate.now().minusYears(1));
        assertTrue(duration.isOverlap(new Duration(LocalDate.now().minusYears(4), LocalDate.now().minusYears(2))));
        assertTrue(duration.isOverlap(new Duration(LocalDate.now().minusYears(2), LocalDate.now())));
        assertFalse(duration.isOverlap(new Duration(LocalDate.now(), LocalDate.now().plusYears(1))));
    }
}