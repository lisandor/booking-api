package com.lisandor.volcano.bookingapi.controller;

import com.lisandor.volcano.bookingapi.domain.dto.BookingDto;
import com.lisandor.volcano.bookingapi.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Rest Controller for bookings. It allows to create, read, update and delete bookings.
 */
@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    /**
     * Create a new booking with the given information
     *
     * @param bookingDto booking information
     * @return {@link UUID} booking identifier
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    String create(@RequestBody BookingDto bookingDto) {
        return bookingService.create(bookingDto).toString();
    }

    /**
     * Gets a booking by its Id
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    BookingDto get(@PathVariable UUID id) {
        return bookingService.get(id);
    }

    /**
     * Updates an existing booking, identified by Id parameter, with given new data
     *
     * @param id         booking identifier
     * @param bookingDto updated info
     * @return
     */
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    BookingDto update(@PathVariable UUID id, @RequestBody BookingDto bookingDto) {
        return bookingService.update(id, bookingDto);
    }

    /**
     * Deletes a booking with the given Id
     *
     * @param id
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable UUID id) {
        bookingService.delete(id);
    }

    /**
     * Handles Illegal Argument exceptions by returning http status 400 and the error message.
     *
     * @param e raised {@link IllegalArgumentException}
     * @return Error message as content
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody
    String handleException(IllegalArgumentException e) {
        return e.getMessage();
    }


}
