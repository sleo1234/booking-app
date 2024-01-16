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




    public void cancelBooking(LocalDateTime startDate,Integer userId){

        Office bookedOffice = officeRepository.findByUsersId(userId);
        String officeName = bookedOffice.getOfficeName();
        officeRepository.updateStatus(OfficeStatus.FREE,officeName);
        officeService.deleteUserOfficeRecords(userId);
        System.out.println("====== " +officeRepository.findAll());
        bookingRepo.deleteByUserIdAndStartDateLessThan(userId,startDate);

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
        Set<User> users = userRepository.findUserByOffices(office);
        users.forEach(System.out :: println);
        Set<Set<Booking>> bookings = new HashSet<Set<Booking>>();
        Set<Booking> flattenedSet = new HashSet<>();
        for (User user : users) {
            bookings.add(bookingRepo.findBookingByUser(user));
        }
        bookings.forEach(flattenedSet::addAll);
        return flattenedSet;
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
