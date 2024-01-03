package com.bookingservice.user;

import com.bookingservice.booking.Booking;
import com.bookingservice.office.Office;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.util.Set;

@Entity
@Table(name = "users")
public class User {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;


    @Column(unique = true)
    private String email;

    @Column(name = "user_password")
    private String password;



    @OneToMany(mappedBy = "user")
    private Set<Booking> bookings;


    @ManyToMany
    @JoinTable(name = "user_office",
            joinColumns = @JoinColumn(name="user_id"),inverseJoinColumns = @JoinColumn(name="office_id"))
    @Column(name="offices")
    private Set<Office> offices;


    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public User(String firstName, String lastName, String email, String password, Set<Booking> bookings) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.bookings = bookings;
    }

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public User(String firstName, String lastName, String email, String password, Set<Booking> bookings, Set<Office> offices) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.bookings = bookings;
        this.offices = offices;
    }

    public void setOffices(Set<Office> offices) {
        this.offices = offices;
    }

    public Set<Office> getOffices() {
        return offices;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }


}
