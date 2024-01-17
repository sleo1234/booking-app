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
import java.nio.file.LinkOption;
import java.time.Instant;
import java.time.LocalDate;
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

//    @Autowired
//    OfficeService officeService;

    @Autowired
    EntityManager entityManager;

    @Test
    public void userRepositoryTest(){


        User newUser = new User("Safta01","Leonard201","safta3.leonard@yahoo.com","abce123");
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

       Office office = officeRepository.findByOfficeName(officeName);
       officeRepository.updateStatus(OfficeStatus.BOOKED,officeName);
       System.out.println("------------- status updated: "+officeRepository.findByOfficeName(officeName).getStatus());


       Booking newBooking = new Booking(sDate,eDate,bookingUser,office);

       Booking savedBooking = bookingRepo.save(newBooking);
       return savedBooking;
   }

   public  Set<Booking> getBookingByOfficeName(String officeName) {
       Office office = officeRepository.findByOfficeName(officeName);


      Set<Booking> bookings = bookingRepo.findBookingsByOfficeId(office.getId());

      return  bookings;
   }

   @Test
    public void testAddBooking(){
       LocalDate now = LocalDate.now();
       String startDate =now.toString()+"T13:20";
       String endDate = now.toString()+"T18:40";

        Booking booking = addBooking(1,"officejRtK",startDate,endDate);
   }


   @Test
   public void testDatesOverlap(){
       LocalDateTime startDate = LocalDateTime.parse("2024-01-11T08:30");
       LocalDateTime endDate = LocalDateTime.parse("2024-01-11T11:30");
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

           if (endDate.isAfter(startDates[i]) && endDate.isBefore(startDates[i])) System.out.println("Overlaping");
          else{
               System.out.println("Not Overlaping");
           }
       }
     // boolean response = checkDatesOverlap(startDate,endDate,bookingDates);
       //System.out.println("------------------------------- "+response);
   }
   @Test
   public void testGetBookingByOfficeName(){

       getBookingByOfficeName("officejfLm");
   }

   @Test
   public void testDeleteBookingById(){

        bookingRepo.deleteBookingById(4);
   }



   public static boolean overlap(LocalDateTime startDateA, LocalDateTime endDateA, LocalDateTime startDateB, LocalDateTime endDateB){

            if (startDateA.isAfter(endDateA)) System.out.println("A start date can not be after end date");
       if (startDateB.isAfter(endDateB)) System.out.println("A start date can not be after end date");


            return !((endDateA.isBefore(startDateB) && startDateA.isBefore(startDateB)) ||
                    (endDateB.isBefore(startDateA) && startDateB.isBefore(startDateA)));
   }



   @Test
   public void testTwoDatesOverlap(){
       LocalDateTime startDate = LocalDateTime.parse("2024-01-11T08:30");
       LocalDateTime endDate = LocalDateTime.parse("2024-01-11T11:30");
       LocalDateTime testStartDate = LocalDateTime.parse("2024-01-11T11:31");
       LocalDateTime testEndDate = LocalDateTime.parse("2024-01-11T15:40");

       boolean response = overlap(startDate,endDate,testStartDate,testEndDate);
       System.out.println(response);

   }


    public static boolean checkIfBookingDateOverlap(LocalDateTime startDate, LocalDateTime endDate, LinkedHashMap<LocalDateTime,LocalDateTime> bookingDates){


        for (Map.Entry<LocalDateTime,LocalDateTime> e : bookingDates.entrySet()){
            if (overlap(startDate,endDate,e.getKey(),e.getValue())) return true;


        }
        return false;
    }

    @Test
    public void testIfBookingDatesOverlap(){

        LinkedHashMap<LocalDateTime,LocalDateTime> bookingDates= new LinkedHashMap<>();
        JpaTests tests = new JpaTests();
        int index=0;

        Set<Booking> existingBookings = getBookingByOfficeName("officefJlK");

        for (Booking booking : existingBookings){
            bookingDates.put(booking.getStartDate(),booking.getEndDate());
            index++;
        }

        LocalDateTime startDate = LocalDateTime.parse("2023-12-29T16:35");
        LocalDateTime endDate = LocalDateTime.parse("2023-12-29T20:00");

       boolean response = checkIfBookingDateOverlap(startDate,endDate,bookingDates);

       System.out.println(response);


    }
    @Test
    public void testDeleteBookingByUserId(){

        bookingRepo.deleteBookingByUserId(1);
    }

    @Test
    public void testCancelBooking(){
        LocalDateTime startDate = LocalDateTime.parse("2024-01-17T06:40:00");
        Integer userId=3;


        Booking bookingToBeCanceled = bookingRepo.findBookingByStartDateAndUserId(startDate,userId);
        Office bookedOffice = bookingToBeCanceled.getOffice();

        bookingRepo.deleteById(bookingToBeCanceled.getId());

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
