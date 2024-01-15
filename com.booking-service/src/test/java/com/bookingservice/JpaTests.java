package com.bookingservice;


import com.bookingservice.booking.Booking;
import com.bookingservice.booking.BookingRepository;
import com.bookingservice.office.Office;
import com.bookingservice.office.OfficeRepository;
import com.bookingservice.office.OfficeService;
import com.bookingservice.office.OfficeStatus;
import jakarta.persistence.EntityManager;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import com.bookingservice.user.User;
import com.bookingservice.user.UserRepository;

import javax.sound.sampled.Line;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = true)

public class JpaTests {

    @Autowired UserRepository userRepository;
    @Autowired OfficeRepository officeRepository;

    @Autowired
    BookingRepository bookingRepo;

//    @Autowired
//    OfficeService officeService;

    @Autowired
    EntityManager entityManager;

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
       LocalDate now = LocalDate.now();
        Booking booking = addBooking(3,"officefJlK",now.toString()+"T17:40",now.toString()+"T18:40");
   }


   @Test
   public void testDatesOverlap(){
       LocalDateTime startDate = LocalDateTime.parse("2024-01-13T05:30");
       LocalDateTime endDate = LocalDateTime.parse("2024-01-13T08:30");
       LocalDateTime testDate = LocalDateTime.parse("2024-01-13T20:40");
       LinkedHashMap <LocalDateTime,LocalDateTime> bookingDates= new LinkedHashMap<>();
       Set<Booking> existingBookings = getBookingByOfficeName("officefJlK");
       System.out.println("test: "+endDate.isBefore(testDate));
       for (Booking booking : existingBookings){
           bookingDates.put(booking.getStartDate(),booking.getEndDate());
       }
       int index=0;
       LocalDateTime[]  startDates = new LocalDateTime[bookingDates.size()];

       LinkedHashMap<LocalDateTime,LocalDateTime> sortedDates=sortDates(bookingDates);

       for (Map.Entry<LocalDateTime,LocalDateTime> e : sortedDates.entrySet()){
           startDates[index]=e.getKey();
           System.out.println("0000 "+ startDates[index]);
           index++;
       }


       for (int i=0; i < startDates.length; i++){

           if (endDate.isAfter(startDates[i])) System.out.println("Overlaping");
          else{
               System.out.println("Not Overlaping");
           }
       }
     // boolean response = checkDatesOverlap(startDate,endDate,bookingDates);
       //System.out.println("------------------------------- "+response);
   }
   @Test
   public void testGetBookingByOfficeName(){

       getBookingByOfficeName("officetQvP");
   }

   @Test
   public void testDeleteBookingById(){

        bookingRepo.deleteBookingById(4);
   }

    @Test
    public void testDeleteBookingByUserId(){

        bookingRepo.deleteBookingByUserId(1);
    }

//    @Test
//    public void testCancelBooking(){
//        LocalDateTime date = LocalDateTime.parse("2023-12-29T16:30:00");
//        Integer userId=5;
//
//
//        Office bookedOffice = officeRepository.findByUsersId(userId);
//        String officeName = bookedOffice.getOfficeName();
//        officeRepository.updateStatus(OfficeStatus.FREE,officeName);
//        officeService.deleteUserOfficeRecords(userId);
//        System.out.println("====== " +officeRepository.findAll());
//        bookingRepo.deleteByUserIdAndStartDateLessThan(userId,date);
//    }


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

       // unsortedDates.put(startDate,endDate);

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

            if (endDate.isAfter(startDates[i])) return true;
            return false;
        }

        return false;
    }



    @Test
    public void testSortDates(){


        LocalDateTime startDate = LocalDateTime.parse("2024-01-13T08:23");
        LocalDateTime endDate = LocalDateTime.parse("2024-01-13T10:23");

        LinkedHashMap<LocalDateTime,LocalDateTime> unsortedDates = new LinkedHashMap<>();
        LinkedHashMap<LocalDateTime,LocalDateTime> sortedDates = new LinkedHashMap<>();

        unsortedDates.put(LocalDateTime.parse("2024-01-13T20:23"),LocalDateTime.parse("2024-01-13T23:23"));
        unsortedDates.put(LocalDateTime.parse("2024-01-13T15:23"),LocalDateTime.parse("2024-01-13T19:23"));
        unsortedDates.put(LocalDateTime.parse("2024-01-13T21:23"),LocalDateTime.parse("2024-01-13T18:23"));
        unsortedDates.put(LocalDateTime.parse("2024-01-13T11:23"),LocalDateTime.parse("2024-01-13T13:23"));

       sortedDates = sortDates(unsortedDates);
       System.out.println(sortedDates);
      boolean response = checkDatesOverlap(startDate,endDate,unsortedDates);
      System.out.println("================================================ "+response);
    }


    public LinkedHashMap<LocalDateTime,LocalDateTime> sortDates(LinkedHashMap<LocalDateTime,LocalDateTime> unsortedDates){

        LocalDateTime[] startDates = new LocalDateTime[unsortedDates.size()];
        LocalDateTime[] endDates = new LocalDateTime[unsortedDates.size()];

        LinkedHashMap<LocalDateTime,LocalDateTime> sortedDates = new LinkedHashMap<>();

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
            sortedDates.put(startDates[i],endDates[i]);
        }

        return sortedDates;
    }
}
