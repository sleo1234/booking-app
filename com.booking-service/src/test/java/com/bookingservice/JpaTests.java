package com.bookingservice;


import com.bookingservice.office.Office;
import com.bookingservice.office.OfficeRepository;
import com.bookingservice.office.OfficeStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import com.bookingservice.user.User;
import com.bookingservice.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)

public class JpaTests {

    @Autowired UserRepository userRepository;
    @Autowired OfficeRepository officeRepository;

    @Test
    public void userRepositoryTest(){


        User newUser = new User("Safta","Leonard","safta.leonard@yahoo.com","abce123");
        userRepository.save(newUser);
    }


    @Test
    public void createOffices(){
        List<Office> officeList = new ArrayList<>();

        for (int i=0; i<51; i++){
           officeList.add(new Office(i,OfficeStatus.FREE,"office"));
        }

        officeList.forEach(System.out :: println);
       officeRepository.saveAll(officeList);
    }



}
