package com.lisandor.volcano.bookingapi.validations;

import com.lisandor.volcano.bookingapi.domain.dto.BookingDto;

import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * This {@link Validator} checks if a period of days is in an accepted range
 */
public class BookingTimeRangeExtensionValidator implements Validator<BookingDto> {

    private Long min, max;

    /**
     * @param minDays minimum amount of days accepted
     * @param maxDays maximum amount of days accepted
     */
    public BookingTimeRangeExtensionValidator(Long minDays, long maxDays) {
        this.min = minDays;
        this.max = maxDays;
    }

    @Override
    public Optional<String> valid(BookingDto bookingDto) {
        final long bookingTimeRange = DAYS.between(bookingDto.getFromDate(), bookingDto.getToDate());
        String message = (bookingTimeRange <= min || bookingTimeRange > max) ? ValidationErrorMessages.BOOKING_TIME_RANGE_ERROR : null;
        return Optional.ofNullable(message);
    }
}
