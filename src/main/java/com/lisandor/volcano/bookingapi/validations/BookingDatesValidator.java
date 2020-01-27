package com.lisandor.volcano.bookingapi.validations;

import com.lisandor.volcano.bookingapi.domain.dto.BookingDto;

import java.util.Optional;

/**
 * Checks for null dates or inverted date range.
 */
public class BookingDatesValidator implements Validator<BookingDto> {

    public BookingDatesValidator() {
    }

    @Override
    public Optional<String> valid(BookingDto bookingDto) {
        String message = null;
        if (bookingDto.getFromDate() == null || bookingDto.getToDate() == null)
            message = ValidationErrorMessages.BOOKING_DATE_MISSING;
        else {
            if (bookingDto.getFromDate().isAfter(bookingDto.getToDate()))
                message = ValidationErrorMessages.INVALID_BOOKING_DATES;
        }
        return Optional.ofNullable(message);
    }
}
