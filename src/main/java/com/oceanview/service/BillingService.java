package com.oceanview.service;

import com.oceanview.dao.BillDAO;
import com.oceanview.dao.BillHistoryDAO;
import com.oceanview.dao.PaymentDAO;
import com.oceanview.dao.ReservationDAO;
import com.oceanview.exception.PaymentException;
import com.oceanview.model.*;
import com.oceanview.util.IdGenerator;
import com.oceanview.util.ValidationUtil;

import java.util.List;


public class BillingService {

    private final BillDAO        billDAO        = new BillDAO();
    private final PaymentDAO     paymentDAO     = new PaymentDAO();
    private final BillHistoryDAO historyDAO     = new BillHistoryDAO();
    private final ReservationDAO reservationDAO = new ReservationDAO();


    public List<Bill> getAllBills() {
        return billDAO.findAll();
    }


    public Bill getBillById(String billId) {
        return billDAO.findById(billId);
    }


    public Bill getBillByReservationId(String reservationId) {
        return billDAO.findByReservationId(reservationId);
    }


    public List<Bill> getBillsByPaymentStatus(String paymentStatus) {
        return billDAO.findByPaymentStatus(paymentStatus);
    }


    public Bill generateBill(String reservationId, String staffId) throws Exception {


        Reservation reservation = reservationDAO.findById(reservationId);
        if (reservation == null) {
            throw new PaymentException("Reservation not found: " + reservationId);
        }


        Bill existingBill = billDAO.findByReservationId(reservationId);
        if (existingBill != null) {
            return existingBill;
        }


        if (Reservation.STATUS_CANCELLED.equals(reservation.getStatus())) {
            throw new PaymentException("Cannot generate bill for a cancelled reservation");
        }


        int    numNights   = reservation.getNumNights();
        double roomPrice   = reservation.getRoom().getPricePerNight();
        double subtotal    = numNights * roomPrice;
        double taxAmount   = 0.0;
        double totalAmount = subtotal + taxAmount;

        if (numNights <= 0) {
            throw new PaymentException(
                    "Invalid stay duration. Check-out must be after check-in.");
        }


        Bill bill = new Bill();
        bill.setBillId(IdGenerator.generateBillId());
        bill.setReservation(reservation);
        bill.setNumNights(numNights);
        bill.setRoomPrice(roomPrice);
        bill.setSubtotal(subtotal);
        bill.setTaxAmount(taxAmount);
        bill.setTotalAmount(totalAmount);
        bill.setPaymentStatus(Bill.PAYMENT_UNPAID);


        if (!billDAO.insert(bill)) {
            throw new PaymentException("Failed to generate bill. Please try again.");
        }


        logBillAction(bill.getBillId(), BillHistory.ACTION_GENERATED, staffId);

        return bill;
    }




    public List<Payment> getPaymentsForBill(String billId) {
        return paymentDAO.findByBillId(billId);
    }


    public Payment processPayment(String billId, double amountPaid,
                                  String paymentMethod, String staffId) throws Exception {


        Bill bill = billDAO.findById(billId);
        if (bill == null) {
            throw new PaymentException("Bill not found: " + billId);
        }


        if (Bill.PAYMENT_PAID.equals(bill.getPaymentStatus())) {
            throw new PaymentException("Bill " + billId + " is already paid");
        }


        if (!ValidationUtil.isPositive(amountPaid)) {
            throw new PaymentException("Payment amount must be greater than zero");
        }


        if (!Payment.METHOD_CASH.equals(paymentMethod) &&
                !Payment.METHOD_CARD.equals(paymentMethod) &&
                !Payment.METHOD_ONLINE.equals(paymentMethod)) {
            throw new PaymentException(
                    "Invalid payment method. Use: Cash, Card, or Online");
        }


        double totalPaidSoFar = paymentDAO.getTotalPaidForBill(billId);
        double remaining      = bill.getTotalAmount() - totalPaidSoFar;

        if (amountPaid > remaining + 0.01) {
            throw new PaymentException(
                    "Payment amount (LKR " + amountPaid +
                            ") exceeds remaining balance (LKR " + remaining + ")");
        }


        Payment payment = new Payment();
        payment.setPaymentId(IdGenerator.generatePaymentId());
        payment.setBill(bill);
        payment.setAmountPaid(amountPaid);
        payment.setPaymentMethod(paymentMethod);
        payment.setProcessedBy(staffId);


        if (!paymentDAO.insert(payment)) {
            throw new PaymentException("Failed to process payment. Please try again.");
        }


        double newTotalPaid = totalPaidSoFar + amountPaid;
        if (newTotalPaid >= bill.getTotalAmount() - 0.01) {
            billDAO.updatePaymentStatus(billId, Bill.PAYMENT_PAID);
            logBillAction(billId, BillHistory.ACTION_PAYMENT, staffId);
        }

        return payment;
    }



    public Bill processCheckout(String reservationId, String paymentMethod,
                                String staffId) throws Exception {


        Bill bill = getBillByReservationId(reservationId);
        if (bill == null) {
            bill = generateBill(reservationId, staffId);
        }


        if (!Bill.PAYMENT_PAID.equals(bill.getPaymentStatus())) {
            double totalPaid  = paymentDAO.getTotalPaidForBill(bill.getBillId());
            double remaining  = bill.getTotalAmount() - totalPaid;
            if (remaining > 0) {
                processPayment(bill.getBillId(), remaining, paymentMethod, staffId);
            }
        }


        logBillAction(bill.getBillId(), BillHistory.ACTION_CHECKOUT, staffId);


        return billDAO.findById(bill.getBillId());
    }


    public List<BillHistory> getBillHistory(String billId) {
        return historyDAO.findByBillId(billId);
    }


    public double getTotalRevenue() {
        return billDAO.getTotalRevenue();
    }


    public int[] getBillCounts() {
        int paid   = billDAO.countByPaymentStatus(Bill.PAYMENT_PAID);
        int unpaid = billDAO.countByPaymentStatus(Bill.PAYMENT_UNPAID);
        return new int[]{paid, unpaid};
    }




    private void logBillAction(String billId, String action, String performedBy) {
        try {
            BillHistory history = new BillHistory();
            history.setHistoryId(IdGenerator.generateHistoryId());
            history.setBillId(billId);
            history.setAction(action);
            history.setPerformedBy(performedBy);
            historyDAO.insert(history);
        } catch (Exception e) {

            System.out.println("Warning: Failed to log bill action: " + e.getMessage());
        }
    }
};
