package com.oceanview.service;

import com.oceanview.dao.AdminDAO;
import com.oceanview.dao.BillDAO;
import com.oceanview.dao.CustomerDAO;
import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.RoomDAO;
import com.oceanview.dao.StaffDAO;
import com.oceanview.model.Admin;
import com.oceanview.model.Bill;
import com.oceanview.model.Reservation;
import com.oceanview.model.Room;
import com.oceanview.model.Staff;
import com.oceanview.util.PasswordUtil;
import com.oceanview.util.ValidationUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdminService {

    private final AdminDAO       adminDAO       = new AdminDAO();
    private final StaffDAO       staffDAO       = new StaffDAO();
    private final CustomerDAO    customerDAO    = new CustomerDAO();
    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final RoomDAO        roomDAO        = new RoomDAO();
    private final BillDAO        billDAO        = new BillDAO();


    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalStaff",    staffDAO.countByStatus("Active"));
        stats.put("pendingStaff",  staffDAO.countByStatus("Pending"));
        stats.put("inactiveStaff", staffDAO.countByStatus("Inactive"));

        stats.put("totalCustomers", customerDAO.countAll());

        stats.put("confirmedReservations",
                reservationDAO.countByStatus(Reservation.STATUS_CONFIRMED));
        stats.put("checkedInReservations",
                reservationDAO.countByStatus(Reservation.STATUS_CHECKEDIN));
        stats.put("completedReservations",
                reservationDAO.countByStatus(Reservation.STATUS_COMPLETED));
        stats.put("cancelledReservations",
                reservationDAO.countByStatus(Reservation.STATUS_CANCELLED));

        stats.put("totalRooms",
                roomDAO.countByStatus(Room.STATUS_AVAILABLE) +
                        roomDAO.countByStatus(Room.STATUS_OCCUPIED)  +
                        roomDAO.countByStatus(Room.STATUS_RESERVED)  +
                        roomDAO.countByStatus(Room.STATUS_MAINTENANCE));
        stats.put("availableRooms",
                roomDAO.countByStatus(Room.STATUS_AVAILABLE));
        stats.put("occupiedRooms",
                roomDAO.countByStatus(Room.STATUS_OCCUPIED));
        stats.put("maintenanceRooms",
                roomDAO.countByStatus(Room.STATUS_MAINTENANCE));

        stats.put("totalRevenue", billDAO.getTotalRevenue());
        stats.put("paidBills",
                billDAO.countByPaymentStatus(Bill.PAYMENT_PAID));
        stats.put("unpaidBills",
                billDAO.countByPaymentStatus(Bill.PAYMENT_UNPAID));

        return stats;
    }



    public List<Staff> getAllStaff() {
        return staffDAO.findAll();
    }

    public List<Staff> getPendingStaff() {
        return staffDAO.findByStatus("Pending");
    }

    public List<Staff> getActiveStaff() {
        return staffDAO.findByStatus("Active");
    }

    public Staff getStaffById(String staffId) {
        return staffDAO.findById(staffId);
    }

    public void approveStaff(String staffId) throws Exception {
        Staff staff = staffDAO.findById(staffId);
        if (staff == null) {
            throw new Exception("Staff member not found: " + staffId);
        }
        if (!"Pending".equals(staff.getStatus())) {
            throw new Exception("Staff account is not pending approval");
        }
        if (!staffDAO.updateStatus(staffId, "Active")) {
            throw new Exception("Failed to approve staff. Please try again.");
        }
    }

    public void deactivateStaff(String staffId) throws Exception {
        Staff staff = staffDAO.findById(staffId);
        if (staff == null) {
            throw new Exception("Staff member not found: " + staffId);
        }
        if ("Inactive".equals(staff.getStatus())) {
            throw new Exception("Staff account is already inactive");
        }
        if (!staffDAO.updateStatus(staffId, "Inactive")) {
            throw new Exception("Failed to deactivate staff. Please try again.");
        }
    }

    public void reactivateStaff(String staffId) throws Exception {
        Staff staff = staffDAO.findById(staffId);
        if (staff == null) {
            throw new Exception("Staff member not found: " + staffId);
        }
        if (!"Inactive".equals(staff.getStatus())) {
            throw new Exception("Staff account is not inactive");
        }
        if (!staffDAO.updateStatus(staffId, "Active")) {
            throw new Exception("Failed to reactivate staff. Please try again.");
        }
    }

    public void deleteStaff(String staffId) throws Exception {
        Staff staff = staffDAO.findById(staffId);
        if (staff == null) {
            throw new Exception("Staff member not found: " + staffId);
        }
        if (!staffDAO.delete(staffId)) {
            throw new Exception("Failed to delete staff. Please try again.");
        }
    }

    public void updateStaff(String staffId, String fullName, String email,
                            String phone, String role) throws Exception {

        Staff existing = staffDAO.findById(staffId);
        if (existing == null) {
            throw new Exception("Staff member not found: " + staffId);
        }
        if (!ValidationUtil.isNotEmpty(fullName)) {
            throw new Exception("Full name is required");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            throw new Exception("Valid email is required");
        }
        if (!ValidationUtil.isValidPhone(phone)) {
            throw new Exception("Valid 10-digit phone number is required");
        }

        existing.setFullName(fullName.trim());
        existing.setEmail(email.trim());
        existing.setPhone(phone.trim());
        existing.setRole(role != null ? role.trim() : existing.getRole());

        if (!staffDAO.update(existing)) {
            throw new Exception("Failed to update staff. Please try again.");
        }
    }



    public Admin getAdminById(String adminId) {
        return adminDAO.findById(adminId);
    }

    public void updateAdminProfile(String adminId, String fullName,
                                   String email, String phone) throws Exception {
        Admin existing = adminDAO.findById(adminId);
        if (existing == null) {
            throw new Exception("Admin not found: " + adminId);
        }
        if (!ValidationUtil.isNotEmpty(fullName)) {
            throw new Exception("Full name is required");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            throw new Exception("Valid email is required");
        }

        existing.setFullName(fullName.trim());
        existing.setEmail(email.trim());
        existing.setPhone(phone != null ? phone.trim() : "");

        if (!adminDAO.update(existing)) {
            throw new Exception("Failed to update profile. Please try again.");
        }
    }

    public void changeAdminPassword(String adminId, String oldPassword,
                                    String newPassword) throws Exception {
        Admin existing = adminDAO.findById(adminId);
        if (existing == null) {
            throw new Exception("Admin not found: " + adminId);
        }
        if (!PasswordUtil.verifyPassword(oldPassword, existing.getPassword())) {
            throw new Exception("Current password is incorrect");
        }
        if (!ValidationUtil.isValidPassword(newPassword)) {
            throw new Exception("New password must be at least 6 characters");
        }

        String hashed = PasswordUtil.hashPassword(newPassword);
        if (!adminDAO.updatePassword(adminId, hashed)) {
            throw new Exception("Failed to change password. Please try again.");
        }
    }
}