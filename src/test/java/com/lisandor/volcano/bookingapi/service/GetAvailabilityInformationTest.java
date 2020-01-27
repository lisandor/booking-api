package com.lisandor.volcano.bookingapi.service;

import com.lisandor.volcano.bookingapi.domain.dto.CheckAvailabilityDto;
import com.lisandor.volcano.bookingapi.domain.dto.DateInfoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class GetAvailabilityInformationTest {

    @Autowired
    CheckAvailabilityService checkAvailabilityService;

    @Test
    public void getAvailabilityWithNoSpecificDateShouldReturnFollowingThirtyDaysInfo() {
        //given
        CheckAvailabilityDto checkAvailabilityDto = new CheckAvailabilityDto();
        //when
        List<DateInfoDto> availabilityInfo = checkAvailabilityService.getInformation(checkAvailabilityDto);
        //then
        assertEquals(LocalDate.now().lengthOfMonth(), availabilityInfo.size());
    }

    @Test
    public void getAvailabilityForTheNextWeekShouldReturnSevenDaysInfo() {
        //given
        LocalDate fromDate = LocalDate.now().plusDays(1);
        LocalDate toDate = fromDate.plusDays(6);
        CheckAvailabilityDto checkAvailabilityDto = new CheckAvailabilityDto(fromDate, toDate);
        //when
        List<DateInfoDto> availabilityInfo = checkAvailabilityService.getInformation(checkAvailabilityDto);
        //then
        assertEquals(7, availabilityInfo.size());
    }
}
