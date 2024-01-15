package com.bookingservice.api;


import com.bookingservice.booking.Booking;
import com.bookingservice.booking.BookingService;
import com.bookingservice.office.Office;
import com.bookingservice.office.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
public class BookingRestController {

    @Autowired private BookingService bookingService;


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

    @PostMapping("/add_booking")

    public String addBooking(@RequestBody BookingRequestBody requestBody){

        Integer userId = requestBody.getUserId();
        String startDate = requestBody.getStartDate();
        String endDate = requestBody.getEndDate();
        String officeName = requestBody.getOfficeName();
       Set<Booking> bookings = bookingService.getBookingByOfficeName(officeName);
       System.out.println("-----0000000000000000000000000 "+ bookings);
        LinkedHashMap<LocalDateTime,LocalDateTime> bookingDates = bookingService.addBookingDates(bookings);
         System.out.println("----------- "+bookingDates);
         if (bookingService.checkDatesOverlap(LocalDateTime.parse(startDate),LocalDateTime.parse(endDate),bookingDates)== false){
             bookingService.addBooking(userId,officeName,startDate,endDate);
             return "You succsesfully booked office: " + officeName+" from "+LocalDateTime.parse(startDate)+" to "+LocalDateTime.parse(endDate);
         }
         else{
             return "This office is booked for this period for the following periods: "+bookingDates.toString();
         }
    }

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
