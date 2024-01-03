package com.bookingservice;


import com.bookingservice.booking.Booking;
import com.bookingservice.booking.BookingRepository;
import com.bookingservice.office.Office;
import com.bookingservice.office.OfficeRepository;
import com.bookingservice.office.OfficeStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import com.bookingservice.user.User;
import com.bookingservice.user.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)

public class JpaTests {

    @Autowired UserRepository userRepository;
    @Autowired OfficeRepository officeRepository;

    @Autowired
    BookingRepository bookingRepo;

    @Test
    public void userRepositoryTest(){


        User newUser = new User("Safta3","Leonard23","safta3.leonard@yahoo.com","abce123");
        userRepository.save(newUser);
    }


    @Test
    public void createOffices(){
        List<Office> officeList = new ArrayList<>();

        for (int i=0; i<51; i++){
           officeList.add(new Office(i,"office",OfficeStatus.FREE));
        }

        officeList.forEach(System.out :: println);
       officeRepository.saveAll(officeList);
    }



    public Date addHoursToJavaUtilDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }


   public Booking addBooking(Integer userId,String officeName, String startDate, String endDate){

       LocalDateTime sDate = LocalDateTime.parse(startDate);
       LocalDateTime eDate = LocalDateTime.parse(endDate);
        User bookingUser = userRepository.findById(userId).get();
        Set<User> users = new HashSet<>();
        users.add(bookingUser);
       Office office = officeRepository.findByOfficeName(officeName);
       office.setUsers(users);
       officeRepository.updateStatus(OfficeStatus.BOOKED,officeName);
       officeRepository.save(office);
       Set offices = new HashSet();
       Set bookings = new HashSet();
       offices.add(office);
       Booking newBooking = new Booking(sDate,eDate,offices,bookingUser);
       bookings.add(newBooking);
       bookingUser.setBookings(bookings);
       bookingUser.setOffices(offices);
       userRepository.save(bookingUser);
       Booking savedBooking = bookingRepo.save(newBooking);
       return savedBooking;
   }

   public Set<Booking> getBookingByOfficeName(String officeName) {
       Office office = officeRepository.findByOfficeName(officeName);
       Set<User> users = userRepository.findUserByOffices(office);
       users.forEach(System.out :: println);

       int size = users.size();
       int i = 0;
       Set<Set<Booking>> bookings = new HashSet<Set<Booking>>();
       Set<Booking> flattenedSet = new HashSet<>();


           for (User user : users) {

                  bookings.add(bookingRepo.findBookingByUser(user));
                  //System.out.println(bookingRepo.findBookingByUser(user));

           }

           bookings.forEach(flattenedSet::addAll);



     flattenedSet.forEach(System.out :: println);
       return flattenedSet;
   }

   @Test
    public void testAddBooking(){

        Booking booking = addBooking(3,"officeaFaP","2023-12-29T09:30:00","2023-12-29T16:30:00");
   }

   @Test
   public void testGetBookingByOfficeName(){

       getBookingByOfficeName("officeHKgY");
   }



   @Test
   public void testUpdateOfficeStatus(){
        officeRepository.updateStatus(OfficeStatus.BOOKED,"officeTvmf");
   }

   @Test
    public void findAllBookings(){

        List<Booking> bookings = bookingRepo.findAll();
        bookings.forEach(System.out :: println);
   }
}
