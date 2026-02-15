package com.oceanview.model;

import java.util.Date;


public class Bill {

    private String      billId;          // B001, B002...
    private Reservation reservation;     // full Reservation object
    private int         numNights;       // number of nights stayed
    private double      roomPrice;       // price per night at time of billing
    private double      subtotal;        // numNights × roomPrice
    private double      taxAmount;       // tax on top (can be 0)
    private double      totalAmount;     // subtotal + taxAmount
    private String      paymentStatus;   // Paid, Unpaid
    private Date        generatedAt;


    public static final String PAYMENT_PAID   = "Paid";
    public static final String PAYMENT_UNPAID = "Unpaid";


    public Bill() {}


    public Bill(String billId, Reservation reservation, int numNights,
                double roomPrice, double subtotal, double taxAmount,
                double totalAmount, String paymentStatus, Date generatedAt) {
        this.billId        = billId;
        this.reservation   = reservation;
        this.numNights     = numNights;
        this.roomPrice     = roomPrice;
        this.subtotal      = subtotal;
        this.taxAmount     = taxAmount;
        this.totalAmount   = totalAmount;
        this.paymentStatus = paymentStatus;
        this.generatedAt   = generatedAt;
    }


    public String      getBillId()        { return billId; }
    public Reservation getReservation()   { return reservation; }
    public int         getNumNights()     { return numNights; }
    public double      getRoomPrice()     { return roomPrice; }
    public double      getSubtotal()      { return subtotal; }
    public double      getTaxAmount()     { return taxAmount; }
    public double      getTotalAmount()   { return totalAmount; }
    public String      getPaymentStatus() { return paymentStatus; }
    public Date        getGeneratedAt()   { return generatedAt; }


    public void setBillId(String billId)               { this.billId = billId; }
    public void setReservation(Reservation reservation){ this.reservation = reservation; }
    public void setNumNights(int numNights)            { this.numNights = numNights; }
    public void setRoomPrice(double roomPrice)         { this.roomPrice = roomPrice; }
    public void setSubtotal(double subtotal)           { this.subtotal = subtotal; }
    public void setTaxAmount(double taxAmount)         { this.taxAmount = taxAmount; }
    public void setTotalAmount(double totalAmount)     { this.totalAmount = totalAmount; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public void setGeneratedAt(Date generatedAt)       { this.generatedAt = generatedAt; }


    public boolean isPaid() {
        return PAYMENT_PAID.equals(this.paymentStatus);
    }

    @Override
    public String toString() {
        return "Bill{id='" + billId +
                "', total=" + totalAmount +
                "', status='" + paymentStatus + "'}";
    }
}
