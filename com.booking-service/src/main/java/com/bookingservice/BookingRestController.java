package com.bookingservice;


import com.bookingservice.office.Office;
import com.bookingservice.office.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class BookingRestController {


    @GetMapping("/booking-service")
    public String getBookingService(){

        return "Booking service";
    }

    @GetMapping("/booking-admin")
    public String getAdmin(){

        return "Hi admin";
    }

    @Autowired
    OfficeService service;


    @DeleteMapping("/delete/{id}")
    //test delete from user_office
    public String delete(@PathVariable("id") Integer userId){
    service.deleteUserOfficeRecords(userId);
    return "deleted from user office entry with id: "+userId.toString();
    }


    @GetMapping("/bookings")

    public String getHome(){

        return "Hi.";
    }
}
