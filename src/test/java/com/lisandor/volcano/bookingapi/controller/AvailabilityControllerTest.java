package com.lisandor.volcano.bookingapi.controller;


import com.lisandor.volcano.bookingapi.domain.dto.CheckAvailabilityDto;
import com.lisandor.volcano.bookingapi.domain.dto.DateInfoDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AvailabilityControllerTest extends AbstractControllerTest {


    @Test
    public void checkAvailabilityWithNoParametersShouldReturnFollowingThirtyDays() {
        // given
        List<DateInfoDto> info = new ArrayList<>();
        IntStream.range(1, 31).forEach(
                x -> info.add(new DateInfoDto(LocalDate.now().plusDays(x), "AVAILABLE"))
        );
        // when
        when(checkAvailabilityService.getInformation(null)).thenReturn(info);

        // then
        try {
            mockMvc.perform(post("/dates/search").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(30)))
                    .andExpect(jsonPath("$[0].calendarDate", is(LocalDate.now().plusDays(1).toString())))
                    .andExpect(jsonPath("$[0].status", is("AVAILABLE")));
        } catch (Exception e) {
            fail("An exception shouldn't be raised");
        }
    }


    @Test
    public void checkAvailabilityWithParametersShouldReturnRequestedDays() {
        // given
        String postBody = "{\"fromDate\":\"" + LocalDate.now().plusDays(1) + "\",\"toDate\":\"" + LocalDate.now().plusDays(8) + "\"}";
        CheckAvailabilityDto dto = new CheckAvailabilityDto(LocalDate.now().plusDays(1), LocalDate.now().plusDays(8));
        List<DateInfoDto> info = new ArrayList<>();
        IntStream.range(1, (int) DAYS.between(dto.getFromDate(), dto.getToDate())).forEach(
                x -> info.add(new DateInfoDto(LocalDate.now().plusDays(x), "AVAILABLE"))
        );
        // when
        when(checkAvailabilityService.getInformation(any())).thenReturn(info);

        // then
        try {
            mockMvc.perform(post("/dates/search").content(postBody)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(6)))
                    .andExpect(jsonPath("$[0].calendarDate", is(LocalDate.now().plusDays(1).toString())))
                    .andExpect(jsonPath("$[0].status", is("AVAILABLE")));
        } catch (Exception e) {
            fail("An exception shouldn't be raised");
        }
    }
}
