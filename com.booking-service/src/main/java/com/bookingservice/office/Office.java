package com.bookingservice.office;


import com.bookingservice.booking.Booking;
import com.bookingservice.user.User;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import io.lettuce.core.dynamic.annotation.CommandNaming;
import jakarta.persistence.*;
import org.apache.commons.lang.RandomStringUtils;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

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

   private Set<User> users;



    public Office(Integer id, String officeName, OfficeStatus status) {
        this.id = id;
        this.officeName = officeName+RandomStringUtils.randomAlphabetic(4);
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

    public Set<User> getUsers() {
        return users;
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

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Office(String officeName, OfficeStatus status, Set<User> users) {
        this.officeName = officeName+RandomStringUtils.randomAlphabetic(4);;
        this.status = status;
        this.users = users;
    }
}
