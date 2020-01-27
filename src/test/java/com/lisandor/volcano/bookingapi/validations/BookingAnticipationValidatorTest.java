package com.lisandor.volcano.bookingapi.validations;

import com.lisandor.volcano.bookingapi.domain.dto.BookingDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BookingAnticipationValidatorTest {


    @Test
    public void bookingRequestWithOneDayOfAnticipationShouldBeOK() {
        //Given
        BookingDto bookingDto = BookingDto.builder().fromDate(LocalDate.now().plusDays(1)).toDate(LocalDate.now().plusDays(4)).build();
        BookingAnticipationValidator bav = new BookingAnticipationValidator(1l, 30l);
        //When
        Optional<String> errorMessage = bav.valid(bookingDto);
        //Then
        assertFalse(errorMessage.isPresent());

    }

    @Test
    public void bookingRequestWithThirtyDaysOfAnticipationShouldBeOk() {
        //Given
        BookingDto bookingDto = BookingDto.builder().fromDate(LocalDate.now().plusDays(30)).toDate(LocalDate.now().plusDays(34)).build();
        BookingAnticipationValidator bav = new BookingAnticipationValidator(1l, 30l);

        //When
        Optional<String> errorMessage = bav.valid(bookingDto);
        //Then
        assertFalse(errorMessage.isPresent());
    }

    @Test
    public void bookingRequestWithThirtyOneDaysOfAnticipationShouldFail() {
        //Given
        BookingDto bookingDto = BookingDto.builder().fromDate(LocalDate.now().plusDays(31)).toDate(LocalDate.now().plusDays(34)).build();
        BookingAnticipationValidator bav = new BookingAnticipationValidator(1l, 30l);
        //When
        Optional<String> errorMessage = bav.valid(bookingDto);
        //Then
        assertTrue(errorMessage.isPresent());
        assertEquals(ValidationErrorMessages.BOOKING_ANTICIPATION_ERROR, errorMessage.get());

    }
}
