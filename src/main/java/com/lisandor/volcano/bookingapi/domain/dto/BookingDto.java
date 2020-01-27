package com.lisandor.volcano.bookingapi.domain.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Dto class for booking information. It allows to store dates info as well as user info.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {

    private UUID id;
    private String email;
    private String fullName;
    private LocalDate fromDate;
    private LocalDate toDate;
    @Builder.Default
    private LocalDateTime bookingDate = LocalDateTime.now();
    private LocalDateTime modified_at;


}
