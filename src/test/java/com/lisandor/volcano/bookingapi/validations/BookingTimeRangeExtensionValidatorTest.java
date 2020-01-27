package com.lisandor.volcano.bookingapi.validations;

import com.lisandor.volcano.bookingapi.domain.dto.BookingDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingTimeRangeExtensionValidatorTest {

    @Test
    public void threeDaysPeriodWithStartAndEndMonthsDaysShouldReturnErrorMessage() {
        BookingTimeRangeExtensionValidator btrev = new BookingTimeRangeExtensionValidator(1l, 3l);
        BookingDto dto =
                BookingDto.builder().fromDate(LocalDate.of(2020, 01, 01)).toDate(
                        LocalDate.of(2020, 01, 31)).build();

        Optional<String> message = btrev.valid(dto);
        assertTrue(message.isPresent());
    }

    @Test
    public void fourDaysPeriodFromOneToFourShouldReturnErrorMessage() {
        BookingTimeRangeExtensionValidator btrev = new BookingTimeRangeExtensionValidator(1l, 3l);
        LocalDate from = LocalDate.of(2020, 01, 01);
        LocalDate to = from.plusDays(4);
        BookingDto dto = BookingDto.builder().fromDate(from).toDate(to).build();

        Optional<String> message = btrev.valid(dto);
        assertTrue(message.isPresent());
    }

    @Test
    public void threeDaysPeriodFromOneToThreeShouldNotReturnErrorMessage() {
        BookingTimeRangeExtensionValidator btrev = new BookingTimeRangeExtensionValidator(1l, 3l);
        BookingDto dto = BookingDto.builder()
                .fromDate(LocalDate.of(2020, 01, 01))
                .toDate(LocalDate.of(2020, 01, 03))
                .build();

        Optional<String> message = btrev.valid(dto);
        assertFalse(message.isPresent());
    }

}
