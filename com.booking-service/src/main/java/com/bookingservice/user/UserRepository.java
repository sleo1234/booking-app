package com.bookingservice.user;


import com.bookingservice.office.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {


    Set<User> findUserByOffices(Office office);
}
