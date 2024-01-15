package com.bookingservice;


import com.bookingservice.api.BookingRequestBody;
import com.bookingservice.booking.Booking;
import com.netflix.discovery.converters.Auto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
public class RestControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user",password = "password")
    void testAddBooking() throws Exception {
    String url="/api/v1/add_booking/";

        BookingRequestBody body = new BookingRequestBody();
        String now = LocalDate.now().toString()+"T";
        String startHour = "20:40";
        String endHour = "21:30";

        body.setUserId(3);
        body.setOfficeName("officefJlK");
        body.setStartDate((now+startHour));
        body.setEndDate((now+endHour));
       System.out.println(now+startHour + " "+now+endHour);
        MvcResult result =this.mockMvc.perform(post(url,body)).
                andDo(print()).
                andExpect(status().
                        isOk()).andDo(print()).andReturn();


    }
}
