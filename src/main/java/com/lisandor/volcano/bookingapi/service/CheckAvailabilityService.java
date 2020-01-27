package com.lisandor.volcano.bookingapi.service;

import com.lisandor.volcano.bookingapi.domain.dto.CheckAvailabilityDto;
import com.lisandor.volcano.bookingapi.domain.dto.DateInfoDto;
import com.lisandor.volcano.bookingapi.repository.BookingDateJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CheckAvailabilityService {

    @Autowired
    BookingDateJpaRepository bookingDateJpaRepository;

    /**
     * Retrieves a List of {@link DateInfoDto} that matches date range given.
     * If no date range is given, default range is: following month.
     *
     * @param checkAvailabilityDto
     * @return
     */
    public List<DateInfoDto> getInformation(CheckAvailabilityDto checkAvailabilityDto) {
        LocalDate from, to;
        if (checkAvailabilityDto == null || (checkAvailabilityDto.getFromDate() == null && checkAvailabilityDto.getToDate() == null)) {
            from = LocalDate.now().plusDays(1);
            to = LocalDate.now().plusMonths(1);
        } else {
            from = checkAvailabilityDto.getFromDate();
            to = checkAvailabilityDto.getToDate();
        }
        return bookingDateJpaRepository.findAllByCalendarDateBetween(from, to)
                .stream()
                .map(date -> new DateInfoDto(date.getCalendarDate(), date.getStatus()))
                .sorted(Comparator.comparing(DateInfoDto::getCalendarDate))
                .collect(Collectors.toList());
    }
}
