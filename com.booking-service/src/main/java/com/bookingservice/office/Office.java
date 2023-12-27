package com.bookingservice.office;


import com.bookingservice.booking.Booking;
import com.bookingservice.user.User;
import io.lettuce.core.dynamic.annotation.CommandNaming;
import jakarta.persistence.*;
import org.apache.commons.lang.RandomStringUtils;

import java.util.Set;

@Entity
@Table(name = "office")

public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="office_name",nullable = false,length = 64)
    private String officeName;

    @Enumerated(EnumType.STRING)
    @Column(length=30,nullable = false)
    private OfficeStatus status;


    @ManyToMany(mappedBy = "offices")
    Set<Booking> bookings;

    public Office(String officeName, OfficeStatus status, Set<Booking> bookings) {
        this.officeName = officeName;
        this.status = status;
        this.bookings = bookings;
    }

    public Office(Integer id, String officeName, OfficeStatus status) {
        this.id = id;
        this.officeName = officeName;
        this.status = status;
    }

    public Office() {
    }

    public Integer getId() {
        return id;
    }

    public String getOfficeName() {
        return officeName;
    }

    public OfficeStatus getStatus() {
        return status;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public void setStatus(OfficeStatus status) {
        this.status = status;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

}
