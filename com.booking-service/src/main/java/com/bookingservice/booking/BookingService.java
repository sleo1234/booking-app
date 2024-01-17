package com.bookingservice.booking;


import com.bookingservice.office.Office;
import com.bookingservice.office.OfficeRepository;
import com.bookingservice.office.OfficeService;
import com.bookingservice.office.OfficeStatus;
import com.bookingservice.user.User;
import com.bookingservice.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Service
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

}
