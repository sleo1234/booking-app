package com.bookingservice.booking;

import com.bookingservice.office.Office;
import com.bookingservice.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.*;
import java.util.Locale;
import java.util.Set;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Basic
  // @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startDate;


    @Basic
   //@Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime endDate;





    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id",nullable = false)
    private User user;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="office_id",nullable = false)
    private Office office;
    public Booking(LocalDateTime startDate, LocalDateTime endDate, User user, Office office) {

        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
        this.office = office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public Office getOffice() {
        return office;
    }



    public Booking(LocalDateTime startDate, LocalDateTime endDate, Set<Office> offices, User user) {
        this.startDate = startDate;
        this.endDate = endDate;

        this.user = user;
    }

    public Booking(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;

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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }



    public void setId(Integer id) {
        this.id = id;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", user=" + user +
                '}';
    }
}
