package com.bookingservice.taskscheduler;

import com.bookingservice.booking.Booking;
import com.bookingservice.booking.BookingRepository;
import com.bookingservice.booking.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@Component
public class ProgramaticallyScheduledTasks {
    private final TaskScheduler taskScheduler;


   /// @Autowired BookingService bookingService;




    @Autowired
    public ProgramaticallyScheduledTasks(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
        //scheduleTask();
    }




    public void scheduleTask() {
            //List<Booking> bookings= bookingService.findAllBookings();
            Runnable task = () -> perform();
            taskScheduler.scheduleWithFixedDelay(task, Duration.ofSeconds(5));

        }

    public void perform(){

      //  findClosestBookingDate();
    }



}