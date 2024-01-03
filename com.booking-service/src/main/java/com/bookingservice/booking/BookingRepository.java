package com.bookingservice.booking;

import com.bookingservice.office.Office;
import com.bookingservice.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Set;

public interface BookingRepository extends JpaRepository<Booking,Integer> {

    Set <Booking> findBookingByUser(User user);


}
