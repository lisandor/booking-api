package com.lisandor.volcano.bookingapi.validations;

import com.lisandor.volcano.bookingapi.domain.dto.BookingDto;

import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * This Validator checks if user requests a booking too early (more than one month in advance)
 * or too late (booking request should happen at least 1 day ahead of arrival)
 */
public class BookingAnticipationValidator implements Validator<BookingDto> {

    private final Long minAnticipation, maxAnticipation;

    /**
     * @param minAnticipation minimum days of anticipation allowed
     * @param maxAnticipation max days of anticipation allowed
     */
    public BookingAnticipationValidator(Long minAnticipation, Long maxAnticipation) {
        this.minAnticipation = minAnticipation;
        this.maxAnticipation = maxAnticipation;
    }


    @Override
    public Optional<String> valid(BookingDto bookingDto) {
        final long bookingAnticipation = DAYS.between(bookingDto.getBookingDate().toLocalDate(), bookingDto.getFromDate());
        String message = bookingAnticipation < minAnticipation || bookingAnticipation > maxAnticipation ?
                ValidationErrorMessages.BOOKING_ANTICIPATION_ERROR : null;
        return Optional.ofNullable(message);
    }

}
