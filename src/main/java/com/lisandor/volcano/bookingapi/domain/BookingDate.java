package com.lisandor.volcano.bookingapi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * JPA Entity for BookingDate. A booking date is a date in the calendar.¡, for instance: 2020-01-01. It has an associated
 * status.
 */
@Table(name = "booking_date")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class BookingDate {

    @Transient
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    @Id
    @GeneratedValue(generator = "booking_date_generator")
    private Long id;
    @Column(name = "calendar_date", columnDefinition = "DATE")
    private LocalDate calendarDate;
    @Column
    private String status;

    public BookingDate(LocalDate calendarDate) {
        this.id = calendarDate.toEpochDay();
        this.calendarDate = calendarDate;
        this.status = "AVAILABLE";
    }

    public void lockObject() {
        System.out.println(String.format("%s %s - Locked", this, this.calendarDate));
        this.lock.writeLock().lock();
    }

    public void unlockObject() {
        try {
            this.lock.writeLock().unlock();
            System.out.println(String.format("%s - Locked", this.calendarDate));
        } catch (IllegalMonitorStateException imse) {
            //do nothing
        }
    }

    public boolean isEqualOrAfter(LocalDate otherDate) {
        return this.calendarDate.isEqual(otherDate) || this.calendarDate.isAfter(otherDate);
    }

    public boolean isEqualOrBefore(LocalDate otherDate) {
        return this.calendarDate.isEqual(otherDate) || this.calendarDate.isBefore(otherDate);
    }
}
