package com.oceanview.model;

import java.util.Date;


public class Payment {

    private String paymentId;
    private Bill   bill;
    private double amountPaid;
    private String paymentMethod;
    private Date   paymentDate;
    private String processedBy;


    public static final String METHOD_CASH   = "Cash";
    public static final String METHOD_CARD   = "Card";
    public static final String METHOD_ONLINE = "Online";


    public Payment() {}


    public Payment(String paymentId, Bill bill, double amountPaid,
                   String paymentMethod, Date paymentDate, String processedBy) {
        this.paymentId     = paymentId;
        this.bill          = bill;
        this.amountPaid    = amountPaid;
        this.paymentMethod = paymentMethod;
        this.paymentDate   = paymentDate;
        this.processedBy   = processedBy;
    }


    public String getPaymentId()     { return paymentId; }
    public Bill   getBill()          { return bill; }
    public double getAmountPaid()    { return amountPaid; }
    public String getPaymentMethod() { return paymentMethod; }
    public Date   getPaymentDate()   { return paymentDate; }
    public String getProcessedBy()   { return processedBy; }


    public void setPaymentId(String paymentId)         { this.paymentId = paymentId; }
    public void setBill(Bill bill)                     { this.bill = bill; }
    public void setAmountPaid(double amountPaid)       { this.amountPaid = amountPaid; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setPaymentDate(Date paymentDate)       { this.paymentDate = paymentDate; }
    public void setProcessedBy(String processedBy)     { this.processedBy = processedBy; }

    @Override
    public String toString() {
        return "Payment{id='" + paymentId +
                "', amount=" + amountPaid +
                "', method='" + paymentMethod + "'}";
    }
}
