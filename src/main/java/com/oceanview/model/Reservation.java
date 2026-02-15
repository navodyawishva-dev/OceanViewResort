package com.oceanview.model;

import java.util.Date;


public class Reservation {

    private String   reservationId;
    private Customer customer;
    private Room     room;
    private Date     checkInDate;
    private Date     checkOutDate;
    private int      numGuests;
    private String   status;
    private String   specialRequests;
    private String   createdBy;
    private Date     createdAt;


    public static final String STATUS_CONFIRMED  = "Confirmed";
    public static final String STATUS_CHECKEDIN  = "Checked-In";
    public static final String STATUS_COMPLETED  = "Completed";
    public static final String STATUS_CANCELLED  = "Cancelled";


    public Reservation() {}


    public Reservation(String reservationId, Customer customer, Room room,
                       Date checkInDate, Date checkOutDate, int numGuests,
                       String status, String specialRequests,
                       String createdBy, Date createdAt) {
        this.reservationId  = reservationId;
        this.customer       = customer;
        this.room           = room;
        this.checkInDate    = checkInDate;
        this.checkOutDate   = checkOutDate;
        this.numGuests      = numGuests;
        this.status         = status;
        this.specialRequests = specialRequests;
        this.createdBy      = createdBy;
        this.createdAt      = createdAt;
    }


    public String   getReservationId()   { return reservationId; }
    public Customer getCustomer()        { return customer; }
    public Room     getRoom()            { return room; }
    public Date     getCheckInDate()     { return checkInDate; }
    public Date     getCheckOutDate()    { return checkOutDate; }
    public int      getNumGuests()       { return numGuests; }
    public String   getStatus()          { return status; }
    public String   getSpecialRequests() { return specialRequests; }
    public String   getCreatedBy()       { return createdBy; }
    public Date     getCreatedAt()       { return createdAt; }


    public void setReservationId(String reservationId)     { this.reservationId = reservationId; }
    public void setCustomer(Customer customer)             { this.customer = customer; }
    public void setRoom(Room room)                         { this.room = room; }
    public void setCheckInDate(Date checkInDate)           { this.checkInDate = checkInDate; }
    public void setCheckOutDate(Date checkOutDate)         { this.checkOutDate = checkOutDate; }
    public void setNumGuests(int numGuests)                { this.numGuests = numGuests; }
    public void setStatus(String status)                   { this.status = status; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
    public void setCreatedBy(String createdBy)             { this.createdBy = createdBy; }
    public void setCreatedAt(Date createdAt)               { this.createdAt = createdAt; }


    public int getNumNights() {
        if (checkInDate == null || checkOutDate == null) return 0;
        long diff = checkOutDate.getTime() - checkInDate.getTime();
        return (int) (diff / (1000 * 60 * 60 * 24)); // convert ms to days
    }


    public double getTotalCost() {
        if (room == null) return 0.0;
        return getNumNights() * room.getPricePerNight();
    }

    @Override
    public String toString() {
        return "Reservation{id='" + reservationId +
                "', customer='" + (customer != null ? customer.getFullName() : "N/A") +
                "', room='" + (room != null ? room.getRoomNumber() : "N/A") +
                "', status='" + status + "'}";
    }
}