package com.lisandor.volcano.bookingapi.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Represent a Calendar Date with its availability status. Status must be one of the following: AVAILABLE or UNAVAILABLE
 */
@Getter
@Setter
@NoArgsConstructor
public class DateInfoDto {

    private LocalDate calendarDate;
    private String status;

    public DateInfoDto(LocalDate date, String status) {
        this.setCalendarDate(date);
        this.setStatus(status);
    }

    public LocalDate getCalendarDate() {
        return calendarDate;
    }

    public void setCalendarDate(LocalDate calendarDate) {
        this.calendarDate = calendarDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
