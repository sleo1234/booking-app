package com.bookingservice.taskscheduler;

import com.bookingservice.booking.Booking;
import com.bookingservice.booking.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ProgramaticallyScheduledTasks {
    private final TaskScheduler taskScheduler;


    @Autowired private BookingService bookingService;


    @Autowired
    public ProgramaticallyScheduledTasks(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
        scheduleTask();
    }

    public void scheduleTask() {
        if (1==2) {
            Runnable task = () -> perform();
            taskScheduler.scheduleWithFixedDelay(task, Duration.ofSeconds(5));
        }
        }

    public void perform(){

        System.out.println("Hi im under the water");
    }
}