package com.lisandor.volcano.bookingapi.service;

import com.lisandor.volcano.bookingapi.domain.dto.BookingDto;
import com.lisandor.volcano.bookingapi.repository.BookingDateJpaRepository;
import com.lisandor.volcano.bookingapi.validations.ValidationErrorMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
public class CreateReservationServiceTest {

    @Autowired
    BookingService bookingService;

    @Autowired
    BookingDateJpaRepository bookingDateRepository;

    @BeforeEach
    public void setUp() {
        BookingService.cache.clear();
        bookingDateRepository.findAll().stream().forEach(bookingDate -> {
            bookingDate.setStatus("AVAILABLE");
            bookingDateRepository.save(bookingDate);
        });

    }

    @Test
    public void createdReservationShouldReturnABookingIdentifier() {
        //given
        BookingDto newBooking = BookingDto.builder().email("user@test.com")
                .fullName("Anonymous User")
                .fromDate(LocalDate.now().plusDays(1))
                .toDate(LocalDate.now().plusDays(4))
                .build();

        //when
        UUID bookingIdentifier = bookingService.create(newBooking);

        //then
        assertNotNull(bookingIdentifier);
    }

    @Test
    public void newReservationsShouldNotBeLongerThanTreeDays() throws Exception {
        //Given
        BookingDto newBooking = BookingDto.builder().email("user@test.com")
                .fullName("Anonymous User")
                .fromDate(LocalDate.now().plusDays(1))
                .toDate(LocalDate.now().plusDays(5))
                .build();

        try {
            //when
            bookingService.create(newBooking);
            throw new Exception("Test Should fail on validation error: Booking too long");
        } catch (IllegalArgumentException iae) {
            //Then
            assertEquals(ValidationErrorMessages.BOOKING_TIME_RANGE_ERROR, iae.getMessage());
        }

    }

    @Test
    public void newReservationsShouldStartsADayAheadOfReservationDate() throws Exception {
        //Given
        BookingDto newBooking = BookingDto.builder().email("user@test.com")
                .fullName("Anonymous User")
                .fromDate(LocalDate.now())
                .toDate(LocalDate.now().plusDays(2))
                .build();
        //when
        try {
            bookingService.create(newBooking);
            throw new Exception("Test Should fail on validation error: Invalid start date. ");
        } catch (IllegalArgumentException iae) {
            //then
            assertEquals(ValidationErrorMessages.BOOKING_ANTICIPATION_ERROR, iae.getMessage());
        }

    }

    @Test
    public void newReservationsShouldStartsUpToOneMonthInAdvanceOfReservationDate() throws Exception {
        //Given
        BookingDto newBooking = BookingDto.builder().email("user@test.com")
                .fullName("Anonymous User")
                .fromDate(LocalDate.now().plusDays(35))
                .toDate(LocalDate.now().plusDays(37))
                .build();

        //when
        try {
            bookingService.create(newBooking);
            throw new Exception("Test Should fail on validation error: Invalid start date. More than a month in advance");
        } catch (IllegalArgumentException iae) {
            assertEquals(ValidationErrorMessages.BOOKING_ANTICIPATION_ERROR, iae.getMessage());
        }
    }

    @Test
    public void reservationOnUnavailableDatesShouldThrowAnError() throws Exception {

        //given
        LocalDate bookingStartDate = LocalDate.now().plusDays(1);
        BookingDto firstBooking = BookingDto.builder().email("user@test.com")
                .fullName("Anonymous User")
                .fromDate(bookingStartDate)
                .toDate(bookingStartDate.plusDays(3))
                .build();

        LocalDate secondBookingStartDate = LocalDate.now().plusDays(2);
        BookingDto secondBooking = BookingDto.builder().email("user@test.com")
                .fullName("Anonymous User")
                .fromDate(secondBookingStartDate)
                .toDate(secondBookingStartDate.plusDays(3))
                .build();

        try {
            bookingService.create(firstBooking);
            bookingService.create(secondBooking);
            throw new Exception("This should fail on validation error. unavailable dates");
        } catch (IllegalArgumentException iae) {
            assertEquals("Some of the dates you selected are not available", iae.getMessage());
        }
    }


}
