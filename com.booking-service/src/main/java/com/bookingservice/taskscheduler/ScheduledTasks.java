package com.bookingservice.taskscheduler;


import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;


public class ScheduledTasks {



    @Scheduled(fixedRate = 5000)
    public void performTask() {
        System.out.println("Task performed at {} at "+ LocalDateTime.now());
    }
}
