package com.bookingservice.office;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface OfficeRepository extends JpaRepository<Office,Integer> {


    //@Query("SELECT o FROM Office o WHERE o.officeName = 1")
     Office findByOfficeName(@Param("oficeName") String oficeName);

    @Modifying
    @Query("UPDATE Office o SET o.status=?1 WHERE o.officeName=?2")
    public void updateStatus(OfficeStatus status,String name);

}
