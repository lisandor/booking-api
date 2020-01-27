package com.lisandor.volcano.bookingapi.controller;

import com.lisandor.volcano.bookingapi.service.BookingService;
import com.lisandor.volcano.bookingapi.service.CheckAvailabilityService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
public abstract class AbstractControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CheckAvailabilityService checkAvailabilityService;

    @MockBean
    BookingService bookingService;

    @BeforeEach
    public void setUp() {
        Mockito.reset(checkAvailabilityService, bookingService);
    }

}