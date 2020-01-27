package com.lisandor.volcano.bookingapi.controller;

import com.lisandor.volcano.bookingapi.domain.dto.BookingDto;
import com.lisandor.volcano.bookingapi.service.BookingService;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookingControllerIntegrationTest {

    @Autowired
    BookingService bookingService;
    @Autowired
    private TestRestTemplate restTemplate;
    private RestTemplate patchRestTemplate;

    @BeforeEach
    public void setup() {

        this.patchRestTemplate = restTemplate.getRestTemplate();
        HttpClient httpClient = HttpClientBuilder.create().build();
        this.patchRestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
        bookingService.findAll().forEach(b -> bookingService.delete(b.getId()));
    }

    @Test
    public void concurrentBookingForOverlappedDatesShouldCreateOnlyTheFirst() throws InterruptedException {
        final int threadCount = 2;
        final ExecutorService service = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            final LocalDate startDate = LocalDate.now().plusDays(1 + i);
            service.execute(() -> this.createBooking(startDate));
        }
        Thread.sleep(2000);
        assertEquals(1, bookingService.findAll().size());

        service.shutdown();

    }

    @Test
    public void concurrentUpdateForOverlappedDatesShouldCreateOnlyTheFirst() throws InterruptedException {
        final int threadCount = 2;
        String id1 = this.createBooking(LocalDate.now().plusDays(3));
        String id2 = this.createBooking(LocalDate.now().plusDays(10));
        List<String> ids = Arrays.asList(id1, id2);
        final ExecutorService service = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            final String id = ids.get(i);
            final LocalDate newStartDate = LocalDate.now().plusDays(20);
            service.execute(() -> this.updateBooking(id, newStartDate));
        }
        Thread.sleep(2000);
        List<BookingDto> bookings = bookingService.findAll();
        LocalDate checkDate = LocalDate.now().plusDays(20);
        System.out.println(bookings.get(0).getFromDate());
        System.out.println(bookings.get(1).getFromDate());
        System.out.println(checkDate.toString());

        assertTrue(checkDate.isEqual(bookings.get(0).getFromDate())
                ^ checkDate.isEqual(bookings.get(1).getFromDate()));
        service.shutdown();

    }

    @Test
    public void concurrentBookingForDifferentDatesShouldCreateTwo() throws InterruptedException {
        final int threadCount = 2;
        final ExecutorService service = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            final LocalDate startDate = LocalDate.now().plusDays((30 - (5 * (i + 1))));
            service.execute(() -> this.createBooking(startDate));
        }
        Thread.sleep(2000);
        assertEquals(2, bookingService.findAll().size());


        service.shutdown();

    }


    private String createBooking(LocalDate startDate) {
        try {
            System.out.println(String.format("%s - %s: %s - %s", Thread.currentThread().getName(), "trying to create booking for ", startDate, startDate.plusDays(3)));
            BookingDto newBooking = BookingDto.builder().email("user@test.com").fullName("Anonymous User").fromDate(startDate).toDate(
                    startDate.plusDays(3)).build();
            String responseEntity = restTemplate.postForObject("/booking", newBooking, String.class);
            System.out.println(String.format("%s - %s", Thread.currentThread().getName(), responseEntity));
            return responseEntity;
        } catch (Exception e) {
            System.out.println(String.format("%s - %s", Thread.currentThread().getName(), e));
            //do nothing
        }
        return null;
    }

    private String updateBooking(String id, LocalDate startDate) {
        try {
            System.out.println(String.format("%s - %s", Thread.currentThread().getName(), "trying to update booking"));
            BookingDto newBooking = BookingDto.builder().email("user@test.com").fullName("Anonymous User").fromDate(startDate).toDate(
                    startDate.plusDays(3)).build();
            String responseEntity = patchRestTemplate.patchForObject("/booking/" + id, newBooking, String.class);
            return responseEntity;
        } catch (Exception e) {
            System.out.println(String.format("%s - %s", Thread.currentThread().getName(), e));
            //do nothing
        }
        return null;
    }

}

