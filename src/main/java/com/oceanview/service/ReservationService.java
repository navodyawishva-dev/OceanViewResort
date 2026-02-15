package com.oceanview.service;

import com.oceanview.dao.CustomerDAO;
import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.RoomDAO;
import com.oceanview.exception.InvalidReservationException;
import com.oceanview.model.Customer;
import com.oceanview.model.Reservation;
import com.oceanview.model.Room;
import com.oceanview.util.DateUtil;
import com.oceanview.util.IdGenerator;
import com.oceanview.util.ValidationUtil;

import java.util.Date;
import java.util.List;


public class ReservationService {

    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final RoomDAO        roomDAO        = new RoomDAO();
    private final CustomerDAO    customerDAO    = new CustomerDAO();


    public List<Reservation> getAllReservations() {
        return reservationDAO.findAll();
    }


    public Reservation getReservationById(String reservationId) {
        return reservationDAO.findById(reservationId);
    }


    public List<Reservation> getReservationsByCustomer(String customerId) {
        return reservationDAO.findByCustomerId(customerId);
    }


    public List<Reservation> getReservationsByStatus(String status) {
        return reservationDAO.findByStatus(status);
    }


    public List<Reservation> searchReservations(String keyword) {
        if (!ValidationUtil.isNotEmpty(keyword)) {
            return reservationDAO.findAll();
        }
        return reservationDAO.search(keyword.trim());
    }


    public Reservation createReservation(String customerId, String roomId,
                                         String checkInDate, String checkOutDate,
                                         int numGuests, String specialRequests,
                                         String createdBy) throws Exception {


        Date checkIn  = DateUtil.parseDate(checkInDate);
        Date checkOut = DateUtil.parseDate(checkOutDate);

        if (checkIn == null || checkOut == null) {
            throw new InvalidReservationException("Valid check-in and check-out dates are required");
        }


        if (!ValidationUtil.isValidDateRange(checkIn, checkOut)) {
            throw new InvalidReservationException(
                    "Check-out date must be after check-in date");
        }


        if (!ValidationUtil.isPositive(numGuests)) {
            throw new InvalidReservationException("Number of guests must be at least 1");
        }


        Customer customer = customerDAO.findById(customerId);
        if (customer == null) {
            throw new InvalidReservationException("Customer not found: " + customerId);
        }


        Room room = roomDAO.findById(roomId);
        if (room == null) {
            throw new InvalidReservationException("Room not found: " + roomId);
        }


        boolean available = roomDAO.isRoomAvailable(roomId, checkIn, checkOut, null);
        if (!available) {
            throw new InvalidReservationException(
                    "Room " + room.getRoomNumber() +
                            " is not available for the selected dates. " +
                            "Please choose different dates or another room.");
        }


        if (numGuests > room.getRoomType().getMaxOccupancy()) {
            throw new InvalidReservationException(
                    "Number of guests (" + numGuests + ") exceeds room capacity (" +
                            room.getRoomType().getMaxOccupancy() + ")");
        }


        Reservation reservation = new Reservation();
        reservation.setReservationId(IdGenerator.generateReservationId());
        reservation.setCustomer(customer);
        reservation.setRoom(room);
        reservation.setCheckInDate(checkIn);
        reservation.setCheckOutDate(checkOut);
        reservation.setNumGuests(numGuests);
        reservation.setStatus(Reservation.STATUS_CONFIRMED);
        reservation.setSpecialRequests(
                specialRequests != null ? specialRequests.trim() : "");
        reservation.setCreatedBy(createdBy);


        if (!reservationDAO.insert(reservation)) {
            throw new InvalidReservationException(
                    "Failed to create reservation. Please try again.");
        }


        roomDAO.updateStatus(roomId, Room.STATUS_RESERVED);

        return reservation;
    }


    public void updateReservation(String reservationId, String roomId,
                                  String checkInDate, String checkOutDate,
                                  int numGuests, String specialRequests) throws Exception {


        Reservation existing = reservationDAO.findById(reservationId);
        if (existing == null) {
            throw new InvalidReservationException(
                    "Reservation not found: " + reservationId);
        }


        if (Reservation.STATUS_COMPLETED.equals(existing.getStatus()) ||
                Reservation.STATUS_CANCELLED.equals(existing.getStatus())) {
            throw new InvalidReservationException(
                    "Cannot edit a " + existing.getStatus() + " reservation");
        }


        Date checkIn  = DateUtil.parseDate(checkInDate);
        Date checkOut = DateUtil.parseDate(checkOutDate);

        if (checkIn == null || checkOut == null) {
            throw new InvalidReservationException(
                    "Valid check-in and check-out dates are required");
        }
        if (!ValidationUtil.isValidDateRange(checkIn, checkOut)) {
            throw new InvalidReservationException(
                    "Check-out date must be after check-in date");
        }


        Room room = roomDAO.findById(roomId);
        if (room == null) {
            throw new InvalidReservationException("Room not found: " + roomId);
        }


        boolean available = roomDAO.isRoomAvailable(
                roomId, checkIn, checkOut, reservationId);
        if (!available) {
            throw new InvalidReservationException(
                    "Room " + room.getRoomNumber() +
                            " is not available for the selected dates.");
        }


        String oldRoomId = existing.getRoom().getRoomId();
        if (!oldRoomId.equals(roomId)) {
            roomDAO.updateStatus(oldRoomId, Room.STATUS_AVAILABLE);
            roomDAO.updateStatus(roomId, Room.STATUS_RESERVED);
        }


        existing.setRoom(room);
        existing.setCheckInDate(checkIn);
        existing.setCheckOutDate(checkOut);
        existing.setNumGuests(numGuests);
        existing.setSpecialRequests(
                specialRequests != null ? specialRequests.trim() : "");

        if (!reservationDAO.update(existing)) {
            throw new InvalidReservationException(
                    "Failed to update reservation. Please try again.");
        }
    }


    public void cancelReservation(String reservationId) throws Exception {
        Reservation existing = reservationDAO.findById(reservationId);
        if (existing == null) {
            throw new InvalidReservationException(
                    "Reservation not found: " + reservationId);
        }


        if (Reservation.STATUS_COMPLETED.equals(existing.getStatus())) {
            throw new InvalidReservationException(
                    "Cannot cancel a completed reservation");
        }
        if (Reservation.STATUS_CANCELLED.equals(existing.getStatus())) {
            throw new InvalidReservationException(
                    "Reservation is already cancelled");
        }


        reservationDAO.updateStatus(reservationId, Reservation.STATUS_CANCELLED);


        roomDAO.updateStatus(
                existing.getRoom().getRoomId(), Room.STATUS_AVAILABLE);
    }


    public void checkIn(String reservationId) throws Exception {
        Reservation existing = reservationDAO.findById(reservationId);
        if (existing == null) {
            throw new InvalidReservationException(
                    "Reservation not found: " + reservationId);
        }


        if (!Reservation.STATUS_CONFIRMED.equals(existing.getStatus())) {
            throw new InvalidReservationException(
                    "Only confirmed reservations can be checked in. " +
                            "Current status: " + existing.getStatus());
        }


        reservationDAO.updateStatus(reservationId, Reservation.STATUS_CHECKEDIN);
        roomDAO.updateStatus(
                existing.getRoom().getRoomId(), Room.STATUS_OCCUPIED);
    }


    public void checkOut(String reservationId) throws Exception {
        Reservation existing = reservationDAO.findById(reservationId);
        if (existing == null) {
            throw new InvalidReservationException(
                    "Reservation not found: " + reservationId);
        }


        if (!Reservation.STATUS_CHECKEDIN.equals(existing.getStatus())) {
            throw new InvalidReservationException(
                    "Only checked-in reservations can be checked out. " +
                            "Current status: " + existing.getStatus());
        }


        reservationDAO.updateStatus(reservationId, Reservation.STATUS_COMPLETED);
        roomDAO.updateStatus(
                existing.getRoom().getRoomId(), Room.STATUS_AVAILABLE);
    }


    public void deleteReservation(String reservationId) throws Exception {
        Reservation existing = reservationDAO.findById(reservationId);
        if (existing == null) {
            throw new InvalidReservationException(
                    "Reservation not found: " + reservationId);
        }
        if (!Reservation.STATUS_CANCELLED.equals(existing.getStatus())) {
            throw new InvalidReservationException(
                    "Only cancelled reservations can be deleted");
        }
        if (!reservationDAO.delete(reservationId)) {
            throw new InvalidReservationException(
                    "Failed to delete reservation. Please try again.");
        }
    }


    public int[] getReservationCounts() {
        int confirmed = reservationDAO.countByStatus(Reservation.STATUS_CONFIRMED);
        int checkedIn = reservationDAO.countByStatus(Reservation.STATUS_CHECKEDIN);
        int completed = reservationDAO.countByStatus(Reservation.STATUS_COMPLETED);
        int cancelled = reservationDAO.countByStatus(Reservation.STATUS_CANCELLED);
        return new int[]{confirmed, checkedIn, completed, cancelled};
    }
}
