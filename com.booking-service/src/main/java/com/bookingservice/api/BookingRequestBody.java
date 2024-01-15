package com.bookingservice.api;

import java.time.LocalDateTime;

public class BookingRequestBody {

    private Integer userId;
    private String startDate;
    private String endDate;
    private String officeName;

    public BookingRequestBody() {
    }

    public BookingRequestBody(Integer userId, String startDate, String endDate, String officeName) {
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.officeName = officeName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }
}
