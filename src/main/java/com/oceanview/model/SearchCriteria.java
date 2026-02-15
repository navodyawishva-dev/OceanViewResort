package com.oceanview.model;

import java.util.Date;


public class SearchCriteria {


    private String reservationId;
    private String customerId;
    private String customerName;
    private String roomId;
    private String roomNumber;
    private Date   checkInDate;
    private Date   checkOutDate;
    private String reservationStatus;


    private String roomTypeId;
    private String roomStatus;
    private Integer floor;


    private String billId;
    private String paymentStatus;


    private String keyword;


    public SearchCriteria() {}


    public String  getReservationId()     { return reservationId; }
    public String  getCustomerId()        { return customerId; }
    public String  getCustomerName()      { return customerName; }
    public String  getRoomId()            { return roomId; }
    public String  getRoomNumber()        { return roomNumber; }
    public Date    getCheckInDate()       { return checkInDate; }
    public Date    getCheckOutDate()      { return checkOutDate; }
    public String  getReservationStatus() { return reservationStatus; }
    public String  getRoomTypeId()        { return roomTypeId; }
    public String  getRoomStatus()        { return roomStatus; }
    public Integer getFloor()             { return floor; }
    public String  getBillId()            { return billId; }
    public String  getPaymentStatus()     { return paymentStatus; }
    public String  getKeyword()           { return keyword; }


    public void setReservationId(String reservationId)         { this.reservationId = reservationId; }
    public void setCustomerId(String customerId)               { this.customerId = customerId; }
    public void setCustomerName(String customerName)           { this.customerName = customerName; }
    public void setRoomId(String roomId)                       { this.roomId = roomId; }
    public void setRoomNumber(String roomNumber)               { this.roomNumber = roomNumber; }
    public void setCheckInDate(Date checkInDate)               { this.checkInDate = checkInDate; }
    public void setCheckOutDate(Date checkOutDate)             { this.checkOutDate = checkOutDate; }
    public void setReservationStatus(String reservationStatus) { this.reservationStatus = reservationStatus; }
    public void setRoomTypeId(String roomTypeId)               { this.roomTypeId = roomTypeId; }
    public void setRoomStatus(String roomStatus)               { this.roomStatus = roomStatus; }
    public void setFloor(Integer floor)                        { this.floor = floor; }
    public void setBillId(String billId)                       { this.billId = billId; }
    public void setPaymentStatus(String paymentStatus)         { this.paymentStatus = paymentStatus; }
    public void setKeyword(String keyword)                     { this.keyword = keyword; }

    @Override
    public String toString() {
        return "SearchCriteria{keyword='" + keyword +
                "', status='" + reservationStatus +
                "', roomStatus='" + roomStatus + "'}";
    }
}