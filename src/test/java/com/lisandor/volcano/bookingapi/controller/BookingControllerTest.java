package com.lisandor.volcano.bookingapi.controller;


import com.lisandor.volcano.bookingapi.validations.ValidationErrorMessages;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.http.MediaType;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class BookingControllerTest extends AbstractControllerTest {

    @Test
    public void missingDatesShouldReturnBadRequest() {
        // given
        String postBody = "{\"email\":\"user@test.com\", \"fullName\":\"User Test\"}";

        // when
        when(bookingService.create(ArgumentMatchers.any())).thenCallRealMethod();

        // then
        try {
            mockMvc.perform(post("/booking").content(postBody).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(ValidationErrorMessages.BOOKING_DATE_MISSING));
        } catch (Exception e) {
            fail("An exception shouldn't be raised");
        }
    }

    @Test
    public void invalidTimeRangeShouldReturnBadRequest() {
        // given
        String postBody = "{\"email\":\"user@test.com\", " +
                "\"fullName\":\"User Test\", " +
                "\"fromDate\":\"" + LocalDate.now().plusDays(5).toString() + "\", " +
                "\"toDate\":\"" + LocalDate.now().plusDays(4).toString() + "\"} ";

        // when
        when(bookingService.create(ArgumentMatchers.any())).thenCallRealMethod();

        // then
        try {
            mockMvc.perform(post("/booking").content(postBody).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(ValidationErrorMessages.INVALID_BOOKING_DATES));
        } catch (Exception e) {
            fail("An exception shouldn't be raised");
        }
    }


    @Test
    public void invalidStartDateShouldReturnBadRequest() {
        // given
        String postBody = "{\"email\":\"user@test.com\", " +
                "\"fullName\":\"User Test\", " +
                "\"fromDate\":\"" + LocalDate.now().minusDays(1).toString() + "\", " +
                "\"toDate\":\"" + LocalDate.now().plusDays(1).toString() + "\"} ";

        // when
        when(bookingService.create(ArgumentMatchers.any())).thenCallRealMethod();

        // then
        try {
            mockMvc.perform(post("/booking").content(postBody).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(ValidationErrorMessages.BOOKING_ANTICIPATION_ERROR));
        } catch (Exception e) {
            fail("An exception shouldn't be raised");
        }
    }

    @Test
    public void startDateAfterOneMonthShouldReturnBadRequest() {
        // given
        String postBody = "{\"email\":\"user@test.com\", " +
                "\"fullName\":\"User Test\", " +
                "\"fromDate\":\"" + LocalDate.now().plusDays(41).toString() + "\", " +
                "\"toDate\":\"" + LocalDate.now().plusDays(43).toString() + "\"} ";

        // when
        when(bookingService.create(ArgumentMatchers.any())).thenCallRealMethod();

        // then
        try {
            mockMvc.perform(post("/booking").content(postBody).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(ValidationErrorMessages.BOOKING_ANTICIPATION_ERROR));
        } catch (Exception e) {
            fail("An exception shouldn't be raised");
        }
    }

    @Test
    public void bookingForTooLongShouldReturnBadRequest() {
        // given
        String postBody = "{\"email\":\"user@test.com\", " +
                "\"fullName\":\"User Test\", " +
                "\"fromDate\":\"" + LocalDate.now().plusDays(1).toString() + "\", " +
                "\"toDate\":\"" + LocalDate.now().plusDays(10).toString() + "\"} ";

        // when
        when(bookingService.create(ArgumentMatchers.any())).thenCallRealMethod();

        // then
        try {
            mockMvc.perform(post("/booking").content(postBody).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(ValidationErrorMessages.BOOKING_TIME_RANGE_ERROR));
        } catch (Exception e) {
            fail("An exception shouldn't be raised");
        }
    }

    @Test
    public void bookingForOneDayShouldReturnBadRequest() {
        // given
        String postBody = "{\"email\":\"user@test.com\", " +
                "\"fullName\":\"User Test\", " +
                "\"fromDate\":\"" + LocalDate.now().plusDays(1).toString() + "\", " +
                "\"toDate\":\"" + LocalDate.now().plusDays(1).toString() + "\"} ";

        // when
        when(bookingService.create(ArgumentMatchers.any())).thenCallRealMethod();

        // then
        try {
            mockMvc.perform(post("/booking").content(postBody).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(ValidationErrorMessages.BOOKING_TIME_RANGE_ERROR));
        } catch (Exception e) {
            fail("An exception shouldn't be raised");
        }
    }
}
