package com.lisandor.volcano.bookingapi.repository;

import com.lisandor.volcano.bookingapi.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookingJpaRepository extends JpaRepository<Booking, UUID> {
}
