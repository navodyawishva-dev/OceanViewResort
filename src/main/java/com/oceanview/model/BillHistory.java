package com.oceanview.model;

import java.util.Date;


public class BillHistory {

    private String historyId;    // H001, H002...
    private String billId;       // which bill this action relates to
    private String action;       // e.g. "Bill Generated", "Payment Received"
    private Date   actionDate;
    private String performedBy;  // staff_id who performed the action


    public static final String ACTION_GENERATED   = "Bill Generated";
    public static final String ACTION_PAYMENT     = "Payment Received";
    public static final String ACTION_CANCELLED   = "Bill Cancelled";
    public static final String ACTION_CHECKOUT    = "Checkout Processed";


    public BillHistory() {}


    public BillHistory(String historyId, String billId, String action,
                       Date actionDate, String performedBy) {
        this.historyId   = historyId;
        this.billId      = billId;
        this.action      = action;
        this.actionDate  = actionDate;
        this.performedBy = performedBy;
    }


    public String getHistoryId()   { return historyId; }
    public String getBillId()      { return billId; }
    public String getAction()      { return action; }
    public Date   getActionDate()  { return actionDate; }
    public String getPerformedBy() { return performedBy; }


    public void setHistoryId(String historyId)     { this.historyId = historyId; }
    public void setBillId(String billId)           { this.billId = billId; }
    public void setAction(String action)           { this.action = action; }
    public void setActionDate(Date actionDate)     { this.actionDate = actionDate; }
    public void setPerformedBy(String performedBy) { this.performedBy = performedBy; }

    @Override
    public String toString() {
        return "BillHistory{id='" + historyId +
                "', billId='" + billId +
                "', action='" + action +
                "', by='" + performedBy + "'}";
    }
}
