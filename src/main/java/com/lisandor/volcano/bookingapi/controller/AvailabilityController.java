package com.lisandor.volcano.bookingapi.controller;

import com.lisandor.volcano.bookingapi.domain.dto.CheckAvailabilityDto;
import com.lisandor.volcano.bookingapi.domain.dto.DateInfoDto;
import com.lisandor.volcano.bookingapi.service.CheckAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @{link AvailabilityController} expose an endpoint for dates search.
 */
@RestController
@RequestMapping("/dates")
public class AvailabilityController {
    @Autowired
    private CheckAvailabilityService checkAvailabilityService;

    /**
     * Given a {@link CheckAvailabilityDto} with boundary dates, returns the list of dates inside that parameters,
     * each one  with its associated status: Availabe or Unavailable
     *
     * @param checkAvailabilityDto
     * @return a List {@link DateInfoDto}
     */
    @PostMapping("/search")
    public @ResponseBody
    List<DateInfoDto> checkAvailability(@RequestBody(required = false) CheckAvailabilityDto checkAvailabilityDto) {
        return checkAvailabilityService.getInformation(checkAvailabilityDto);
    }
}
