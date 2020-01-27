package com.lisandor.volcano.bookingapi.service;

import com.lisandor.volcano.bookingapi.domain.Booking;
import com.lisandor.volcano.bookingapi.domain.BookingDate;
import com.lisandor.volcano.bookingapi.domain.dto.BookingDto;
import com.lisandor.volcano.bookingapi.repository.BookingDateJpaRepository;
import com.lisandor.volcano.bookingapi.repository.BookingJpaRepository;
import com.lisandor.volcano.bookingapi.validations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 *
 */
@Service
@Transactional
public class BookingService {

    static final ConcurrentHashMap<Long, BookingDate> cache = new ConcurrentHashMap<>();

    @Autowired
    private BookingJpaRepository bookingJpaRepository;
    @Autowired
    private BookingDateJpaRepository bookingDateJpaRepository;

    /**
     * Creates a new booking based on information given in BookingDto parameter
     *
     * @param bookingDto {@link BookingDto} Booking related information,
     * @return {@link UUID} representing generated booking identifier
     */
    public UUID create(BookingDto bookingDto) throws IllegalArgumentException {
        validateDto(bookingDto);
        List<BookingDate> dates = validateBookingDates(bookingDto);

        Booking booking = fromDto(bookingDto, dates);
        bookingJpaRepository.save(booking);
        return booking.getId();
    }

    /**
     * locks a booking date obtained from cache.
     *
     * @param bookingDate
     */
    private void lockObject(BookingDate bookingDate) {
        bookingDate.lockObject();

    }

    /**
     * Unlocks a Booking Date. I must update object in cache and in the database.
     *
     * @param bookingDate
     */
    private void unlockObject(BookingDate bookingDate) {
        bookingDate.unlockObject();
        cache.replace(bookingDate.getId(), bookingDate);
        bookingDateJpaRepository.save(bookingDate);
    }

    /**
     * Check if any of the selected dates are unavailable. This method is crucial in terms of concurrency.
     * I decided to synchronize at Date level in order to not to block bookings for different dates.
     * The first user to ask for a booking will lock the only selected dates till booking is created (or throws an error).
     * Bookings on different date ranges should not be executing sequentially.
     * If I synchronize at request level, I'd be creating a bottleneck as the load grows up on the endpoint.
     *
     * @param bookingDto
     * @return
     */
    private List<BookingDate> validateBookingDates(BookingDto bookingDto) {
        List<BookingDate> dates = getDatesFromCache(bookingDto.getFromDate(), bookingDto.getToDate());
        dates.forEach(this::lockObject);
        if (dates.stream().anyMatch(date -> "UNAVAILABLE".equals(date.getStatus()))) {
            System.out.println(String.format("%s unlocks", Thread.currentThread().getName()));
            dates.forEach(this::unlockObject);
            throw new IllegalArgumentException("Some of the dates you selected are not available");
        }
        return dates;
    }

    /**
     * Use a custom (and rudimentary) cache to get a better way to manage locks.
     *
     * @param from
     * @param to
     * @return
     */
    List<BookingDate> getDatesFromCache(LocalDate from, LocalDate to) {
        List<BookingDate> dates = bookingDateJpaRepository.findAllByCalendarDateBetween(from, to);

        return dates.stream().map(d -> {
            if (cache.containsKey(d.getId()))
                return cache.get(d.getId());
            else {
                cache.put(d.getId(), d);
                return d;
            }
        }).collect(Collectors.toList());
    }

    /**
     * Change status of given booking dates to <b>newStatus</b>
     *
     * @param dates     BookingDates to be updated
     * @param newStatus new BookingDate status
     * @return List of updated {@link BookingDate}
     */
    private List<BookingDate> updateBookingDateStatus(List<BookingDate> dates, final String newStatus) {
        if (newStatus.equals("AVAILABLE"))
            dates.forEach(this::lockObject);
        dates.stream().forEach(date -> {
            date.setStatus(newStatus);
            bookingDateJpaRepository.save(date);
            unlockObject(date);
        });
        return dates;
    }


    /**
     * Gets the booking identified by de given {@link UUID}
     *
     * @param id
     * @return {@link BookingDto} with booking information
     */
    public BookingDto get(UUID id) {
        Booking booking = bookingJpaRepository.getOne(id);
        return fromObject(booking);
    }

    /**
     * makes the date range of the booking available again an then deletes the booking.
     *
     * @param id
     */
    public void delete(UUID id) {
        // makes the date range available again and the
        updateBookingDateStatus(bookingJpaRepository.getOne(id).getDates(), "AVAILABLE");
        bookingJpaRepository.deleteById(id);
    }

    /**
     * Partially updates an existing Booking, identified by id parameter with new information.
     *
     * @param id
     * @param newBooking
     * @return
     */
    public BookingDto update(UUID id, BookingDto newBooking) {
        if (newBooking.getId() == null)
            newBooking.setId(id);
        // get the old booking information
        Booking oldBooking = bookingJpaRepository.getOne(id);
        // make available the old date range
        updateBookingDateStatus(oldBooking.getDates(), "AVAILABLE");

        //updates with the new information
        List<BookingDate> newDates = validateBookingDates(newBooking);
        Booking updatedBooking = fromDto(newBooking, newDates);
        bookingJpaRepository.save(updatedBooking);
        return fromObject(updatedBooking);
    }

    /**
     * @return list of {@link BookingDto} of all bookings registered
     */
    public List<BookingDto> findAll() {
        return bookingJpaRepository.findAll().stream().map(this::fromObject).collect(Collectors.toList());
    }

    /**
     * Creates a booking from dto
     *
     * @param dto
     * @param dates
     * @return
     */
    public Booking fromDto(BookingDto dto, List<BookingDate> dates) {
        return Booking.builder()
                .id(dto.getId() == null ? UUID.randomUUID() : dto.getId())
                .email(dto.getEmail())
                .fullName(dto.getFullName())
                .createdAt(dto.getBookingDate())
                .dates(this.updateBookingDateStatus(dates, "UNAVAILABLE"))
                .build();
    }

    private BookingDto fromObject(Booking booking) {
        BookingDto dto = BookingDto.builder()
                .id(booking.getId())
                .email(booking.getEmail())
                .fullName(booking.getFullName())
                .fromDate(booking.getDates().get(0).getCalendarDate())
                .toDate(booking.getDates().get(booking.getDates().size() - 1).getCalendarDate())
                .build();
        return dto;
    }

    /**
     * Instantiates all bookingDto related validations and throws an Exceptions if any of this validations fails.
     *
     * @param bookingDto
     * @throws IllegalArgumentException
     */
    private void validateDto(BookingDto bookingDto) throws IllegalArgumentException {
        BookingValidations validations = new BookingValidations(
                new BookingDatesValidator(),
                new BookingAnticipationValidator(ValidationParameters.MIN_ANTICIPATION_DAYS, ValidationParameters.MAX_ANTICIPATION_DAYS),
                new BookingTimeRangeExtensionValidator(ValidationParameters.MIN_BOOKING_TIME_RANGE_ALLOWED, ValidationParameters.MAX_BOOKING_TIME_RANGE_ALLOWED));
        Optional<String> message = validations.valid(bookingDto);
        message.ifPresent(m -> {
            throw new IllegalArgumentException(m);
        });
    }
}


