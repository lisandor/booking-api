package com.lisandor.volcano.bookingapi.repository;

import com.lisandor.volcano.bookingapi.domain.BookingDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingDateJpaRepository extends JpaRepository<BookingDate, Long> {

    @Query("select bd from BookingDate bd where bd.calendarDate >= :date1 and bd.calendarDate <= :date2")
    List<BookingDate> findAllByCalendarDateBetween(@Param("date1") LocalDate date1, @Param("date2") LocalDate date2);
}
