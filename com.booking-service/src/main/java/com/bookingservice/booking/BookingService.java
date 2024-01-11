package com.bookingservice.booking;


import com.bookingservice.office.Office;
import com.bookingservice.office.OfficeRepository;
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



    public LinkedHashMap<LocalDateTime,LocalDateTime> addBookingDates(Set<Booking> existingBookings) {
        LinkedHashMap<LocalDateTime,LocalDateTime> bookingDates= new LinkedHashMap<>();
        for (Booking booking : existingBookings){
            bookingDates.put(booking.getStartDate(),booking.getEndDate());
        }
        return bookingDates;
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
}
