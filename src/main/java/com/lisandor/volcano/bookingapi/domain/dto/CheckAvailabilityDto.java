package com.lisandor.volcano.bookingapi.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Dto for represent a date range in check availability endpoint
 */
@Getter
@Setter
@NoArgsConstructor
public class CheckAvailabilityDto {

    private LocalDate fromDate;
    private LocalDate toDate;

    public CheckAvailabilityDto(LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }
}
