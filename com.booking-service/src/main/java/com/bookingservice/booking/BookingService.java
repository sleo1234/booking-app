package com.bookingservice.booking;


import com.bookingservice.office.Office;
import com.bookingservice.office.OfficeRepository;
import com.bookingservice.office.OfficeService;
import com.bookingservice.office.OfficeStatus;
import com.bookingservice.user.User;
import com.bookingservice.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class BookingService {

    @Autowired
    private UserRepository  userRepository;
    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    OfficeService officeService = new OfficeService();

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



    public void cancelBookingByDate(Integer userId, String startDate){
        LocalDateTime sDate = LocalDateTime.parse(startDate);
        Booking bookingToBeCanceled = bookingRepo.findBookingByStartDateAndUserId(sDate,userId);
        Office bookedOffice = bookingToBeCanceled.getOffice();
        officeRepository.updateStatus( OfficeStatus.FREE,bookedOffice.getOfficeName());
        bookingRepo.deleteById(bookingToBeCanceled.getId());
    }




    public LinkedHashMap<LocalDateTime,LocalDateTime> addBookingDates(Set<Booking> existingBookings) {
        LinkedHashMap<LocalDateTime,LocalDateTime> bookingDates= new LinkedHashMap<>();
        for (Booking booking : existingBookings){
            bookingDates.put(booking.getStartDate(),booking.getEndDate());
        }
        return bookingDates;
    }

    


    public Set<Booking> getBookingByOfficeName(String officeName) {
        Office office = officeRepository.findByOfficeName(officeName);


        Set<Booking> bookings = bookingRepo.findBookingsByOfficeId(office.getId());
        return  bookings;
    }


    public static boolean checkIfBookingDateOverlap(LocalDateTime startDate, LocalDateTime endDate, LinkedHashMap<LocalDateTime,LocalDateTime> bookingDates){


        for (Map.Entry<LocalDateTime,LocalDateTime> e : bookingDates.entrySet()){
            if (overlap(startDate,endDate,e.getKey(),e.getValue())) return true;


        }
        return false;
    }

    public static boolean overlap(LocalDateTime startDateA, LocalDateTime endDateA, LocalDateTime startDateB, LocalDateTime endDateB){

        if (startDateA.isAfter(endDateA)) System.out.println("A start date can not be after end date");
        if (startDateB.isAfter(endDateB)) System.out.println("A start date can not be after end date");


        return !((endDateA.isBefore(startDateB) && startDateA.isBefore(startDateB)) ||
                (endDateB.isBefore(startDateA) && startDateB.isBefore(startDateA)));
    }


    public void cancellBookingsByUser(Integer userId){
        User user = userRepository.findById(userId).get();
        Set<Booking> bookings = bookingRepo.findBookingByUser(user);

        bookingRepo.deleteAllBookingsByUserId(userId);
        updateOfficesStatus("FREE",userId);

    }
    public void updateOfficesStatus(String status, Integer userId){

        User user = userRepository.findById(userId).get();
        Set<Booking> bookings = bookingRepo.findBookingByUser(user);

        for (Booking booking : bookings){
            officeRepository.updateStatus(Enum.valueOf(OfficeStatus.class,status),booking.getOffice().getOfficeName());
        }

    }

    List<LocalDateTime> findBookingDatesByUserId(Integer userId){

        List<LocalDateTime> bookingDates = new ArrayList<>();
        User user = userRepository.findById(userId).get();

        Set<Booking> bookings = bookingRepo.findBookingByUser(user);
        for (Booking booking : bookings){
            bookingDates.add(booking.getStartDate());
        }
        return bookingDates;
    }
    public List<Booking> findAllBookings(){

        return bookingRepo.findAll();
    }


    @Scheduled(fixedDelay = 30, timeUnit = TimeUnit.SECONDS)
    public void findClosestBookingDate(){

        LocalDateTime myDate = LocalDateTime.parse("2024-01-18T22:05");
        //List<LocalDateTime> bookingDates = findBookingDatesByUserId(1);

        List<LocalDateTime> bookingDates = findBookingDatesByUserId(1);



       for (int i=0; i<bookingDates.size(); i++) {
           //dates.put(booking.getStartDate(), booking.getEndDate());
           long minutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), bookingDates.get(i));

           System.out.println(minutes + " untill ....");
           if (minutes < 15 && minutes > 0) {

               //    System.out.println(booking.getStartDate());
               bookingRepo.deleteByUserIdAndStartDate(1, bookingDates.get(i));

           }

       }

    }

}
