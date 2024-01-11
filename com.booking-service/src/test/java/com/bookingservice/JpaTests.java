package com.bookingservice;


import com.bookingservice.booking.Booking;
import com.bookingservice.booking.BookingRepository;
import com.bookingservice.office.Office;
import com.bookingservice.office.OfficeRepository;
import com.bookingservice.office.OfficeStatus;
import net.bytebuddy.asm.Advice;
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


        User newUser = new User("Safta3","Leonard23","safta4.leonard@yahoo.com","abce123");
        userRepository.save(newUser);
    }

    @Test
    void testDeleteBookingByUserIdAndStartDate(){

        LocalDateTime beforeDate = LocalDateTime.parse("2023-12-29T09:45");

     bookingRepo.deleteByUserIdAndStartDateLessThan(1,beforeDate);
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
       System.out.println("------------- status updated: "+officeRepository.findByOfficeName(officeName).getStatus());
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
       LinkedHashMap <LocalDateTime,LocalDateTime> bookingDates= new LinkedHashMap<>();

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
           for (Booking booking : flattenedSet){
               bookingDates.put(booking.getStartDate(),booking.getEndDate());
           }

       LocalDateTime startDate = LocalDateTime.parse("2023-12-29T16:25:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-12-29T23:30:00");

     flattenedSet.forEach(System.out :: println);
       return flattenedSet;
   }

   @Test
    public void testAddBooking(){

        Booking booking = addBooking(5,"officeTWfR","2023-12-29T16:30:00","2023-12-29T18:30:00");
   }


   @Test
   public void testDatesOverlap(){
       LocalDateTime startDate = LocalDateTime.parse("2023-12-29T16:25:00");
       LocalDateTime endDate = LocalDateTime.parse("2023-12-29T23:30:00");
       LinkedHashMap <LocalDateTime,LocalDateTime> bookingDates= new LinkedHashMap<>();
       Set<Booking> existingBookings = getBookingByOfficeName("officeHKgY");
       for (Booking booking : existingBookings){
           bookingDates.put(booking.getStartDate(),booking.getEndDate());
       }

      boolean response = checkDatesOverlap(startDate,endDate,bookingDates);
       System.out.println(bookingDates);
   }
   @Test
   public void testGetBookingByOfficeName(){

       getBookingByOfficeName("officeHKgY");
   }

   @Test
   public void testDeleteBookingById(){

        bookingRepo.deleteBookingById(4);
   }

    @Test
    public void testDeleteBookingByUserId(){

        bookingRepo.deleteBookingByUserId(1);
    }

    @Test
    public void testCancelBooking(){
        LocalDateTime date = LocalDateTime.parse("2023-12-29T16:30:00");
        Integer userId=5;


        Office bookedOffice = officeRepository.findByUsersId(userId);
        String officeName = bookedOffice.getOfficeName();
        officeRepository.updateStatus(OfficeStatus.FREE,officeName);
        officeRepository.deleteByUsersId(userId);
        System.out.println("====== " +officeRepository.findAll());
        bookingRepo.deleteByUserIdAndStartDateLessThan(userId,date);
    }


    @Test
    void testFindOfficeByUserId(){

        Office office = officeRepository.findByUsersId(2);
        System.out.println(office);
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



    public static boolean checkDatesOverlap(LocalDateTime startDate,LocalDateTime endDate, LinkedHashMap<LocalDateTime,LocalDateTime> unsortedDates){

        unsortedDates.put(startDate,endDate);

        LocalDateTime[] startDates = new LocalDateTime[unsortedDates.size()];
        LocalDateTime[] endDates = new LocalDateTime[unsortedDates.size()];
        int dateSize=unsortedDates.size();
        int index=0;

        boolean swapped;
        LocalDateTime temp;

        for (Map.Entry<LocalDateTime,LocalDateTime> e : unsortedDates.entrySet()){
            startDates[index]=e.getKey();
            index++;
        }




        for (int i=0; i<dateSize-1; i++){
            swapped=false;
            for (int j=0; j<dateSize-i-1; j++){
                if(startDates[j].isAfter(startDates[j+1])){
                    temp=startDates[j];
                    startDates[j]=startDates[j+1];
                    startDates[j+1]=temp;
                    swapped=true;
                }

            }

            if (!swapped) break;

        }

        for (int i=0; i<dateSize; i++){
            endDates[i]=unsortedDates.get(startDates[i]);
        }


        for (int i=1; i < dateSize; i++){

            if (endDates[i-1].isAfter(startDates[i])) return true;
            return false;
        }

        return false;
    }

}
