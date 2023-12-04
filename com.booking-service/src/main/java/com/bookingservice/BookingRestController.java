package com.bookingservice;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookingRestController {


    @GetMapping("/booking-service/")
    public String getBookingService(){

        return "Booking service";
    }


    @GetMapping("/bookings")

    public String getHome(){

        return "Hi.";
    }
}
