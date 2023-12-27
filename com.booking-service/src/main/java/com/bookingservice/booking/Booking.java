package com.bookingservice.booking;

import com.bookingservice.office.Office;
import com.bookingservice.user.User;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Locale;
import java.util.Set;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Basic
    @Temporal(TemporalType.TIME)
    private Date startDate;


    @Basic
    @Temporal(TemporalType.TIME)
    private Date endDate;



    @ManyToMany
    @JoinTable(name = "booking_office",
            joinColumns = @JoinColumn(name="booking_id"),inverseJoinColumns = @JoinColumn(name="office_id"))
    Set<Office> offices;



    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    public Booking(Date startDate, Date endDate, Set<Office> offices, User user) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.offices = offices;
        this.user = user;
    }

    public Booking(Date startDate, Date endDate, Set<Office> offices) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.offices = offices;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Booking() {
    }

    public Integer getId() {
        return id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Set<Office> getOffices() {
        return offices;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setOffices(Set<Office> offices) {
        this.offices = offices;
    }
}
