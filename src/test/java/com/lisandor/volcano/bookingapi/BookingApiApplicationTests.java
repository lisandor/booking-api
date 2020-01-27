package com.lisandor.volcano.bookingapi;

import com.lisandor.volcano.bookingapi.repository.BookingDateJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
//@Sql({"/schema.sql", "/data.sql"})
class BookingApiApplicationTests {

    @Autowired
    private BookingDateJpaRepository bookingDateJpaRepository;

    @Test
    public void testLoadDataForTestClass() {
        System.out.println(bookingDateJpaRepository.findAll().get(88).getCalendarDate());
        assertEquals(89, bookingDateJpaRepository.findAll().size());
    }
}
