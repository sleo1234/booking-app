package com.bookingservice.office;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OfficeService {


    @Autowired
    private EntityManager entityManager;

    public void deleteUserOfficeRecords(Integer userId) {
        Query query = entityManager.createNativeQuery(
                "DELETE FROM user_office WHERE user_id = :userId");
        query.setParameter("userId", userId);

        query.executeUpdate();
    }
}
