package com.lisandor.volcano.bookingapi.validations;

import com.lisandor.volcano.bookingapi.domain.dto.BookingDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BookingDatesValidatorTest {

    @Test
    public void existingAndConsecutiveDatesShouldBeOk() {
        //Given
        BookingDto bookingDto = BookingDto.builder().fromDate(LocalDate.now()).toDate(LocalDate.now().plusDays(1)).build();
        BookingDatesValidator bdv = new BookingDatesValidator();
        //When
        Optional<String> errorMessage = bdv.valid(bookingDto);
        //Then
        assertFalse(errorMessage.isPresent());
    }

    @Test
    public void nonExistingFromDateShouldFail() {
        //Given
        BookingDto bookingDto = BookingDto.builder().toDate(LocalDate.now().plusDays(1)).build();
        BookingDatesValidator bdv = new BookingDatesValidator();
        //When
        Optional<String> errorMessage = bdv.valid(bookingDto);
        //Then
        assertTrue(bdv.valid(bookingDto).isPresent());
        assertEquals(ValidationErrorMessages.BOOKING_DATE_MISSING, errorMessage.get());
    }

    @Test
    public void nonExistingTODateShouldFail() {
        //Given
        BookingDto bookingDto = BookingDto.builder().fromDate(LocalDate.now()).build();
        BookingDatesValidator bdv = new BookingDatesValidator();
        //When
        Optional<String> errorMessage = bdv.valid(bookingDto);
        //Then
        assertTrue(bdv.valid(bookingDto).isPresent());
        assertEquals(ValidationErrorMessages.BOOKING_DATE_MISSING, errorMessage.get());
    }

    @Test
    public void existingDatesWithToDateBeforeFromDateShouldFail() {
        //Given
        BookingDto bookingDto = BookingDto.builder().fromDate(LocalDate.now().plusDays(2)).toDate(LocalDate.now()).build();
        BookingDatesValidator bdv = new BookingDatesValidator();
        //When
        Optional<String> errorMessage = bdv.valid(bookingDto);
        //Then
        assertTrue(bdv.valid(bookingDto).isPresent());
        assertEquals(ValidationErrorMessages.INVALID_BOOKING_DATES, errorMessage.get());
    }

}
