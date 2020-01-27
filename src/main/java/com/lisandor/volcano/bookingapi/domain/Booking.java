package com.lisandor.volcano.bookingapi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * JPA Entity for Bookings. A Booking has a list of {@link BookingDate} instead of define a Date Range. This allows to
 * manage in a better way the calendar dates.
 */
@Table(name = "booking")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Booking {

    @Id
    private UUID id;
    @Column
    private String email;
    @Column(name = "full_name")
    private String fullName;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "booking_booking_dates",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "booking_date_id")
    )
    private List<BookingDate> dates;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "modified_at")
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    public Booking() {
    }
}
