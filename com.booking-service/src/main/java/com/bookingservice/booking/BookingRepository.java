package com.bookingservice.booking;

import com.bookingservice.office.Office;
import com.bookingservice.user.User;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Integer> {

    Set <Booking> findBookingByUser(User user);

    void deleteBookingById(Integer id);

    void deleteBookingByUserId(Integer id);

    void deleteByUserIdAndStartDateLessThan(Integer id, LocalDateTime startDate);


    void deleteByUserIdAndStartDate(Integer id, LocalDateTime startDate);


    Set<Booking> findBookingsByOfficeId(Integer id);

    Booking findBookingByStartDateAndUserId(LocalDateTime startDate, Integer id);

    void deleteAllBookingsByUserId(Integer userId);

}
