package com.oceanview.service;

import com.oceanview.dao.StaffDAO;
import com.oceanview.exception.DataAccessException;
import com.oceanview.model.Staff;
import com.oceanview.util.IdGenerator;
import com.oceanview.util.PasswordUtil;
import com.oceanview.util.ValidationUtil;

import java.util.List;


public class StaffService {

    private final StaffDAO staffDAO = new StaffDAO();


    public List<Staff> getAllStaff() {
        return staffDAO.findAll();
    }


    public List<Staff> getStaffByStatus(String status) {
        return staffDAO.findByStatus(status);
    }


    public Staff getStaffById(String staffId) {
        return staffDAO.findById(staffId);
    }


    public void addStaff(String fullName, String username, String password,
                         String email, String phone, String role) throws Exception {


        if (!ValidationUtil.isNotEmpty(fullName)) {
            throw new Exception("Full name is required");
        }
        if (!ValidationUtil.isValidUsername(username)) {
            throw new Exception("Username must be 4-20 characters (letters, numbers, underscore)");
        }
        if (!ValidationUtil.isValidPassword(password)) {
            throw new Exception("Password must be at least 6 characters");
        }
        if (!ValidationUtil.isNotEmpty(email) || !ValidationUtil.isValidEmail(email)) {
            throw new Exception("Valid email is required");
        }
        if (!ValidationUtil.isValidPhone(phone)) {
            throw new Exception("Valid 10-digit phone number is required");
        }


        if (staffDAO.usernameExists(username.trim())) {
            throw new Exception("Username '" + username + "' is already taken");
        }


        Staff staff = new Staff();
        staff.setId(IdGenerator.generateStaffId());
        staff.setFullName(fullName.trim());
        staff.setUsername(username.trim());
        staff.setPassword(PasswordUtil.hashPassword(password));
        staff.setEmail(email.trim());
        staff.setPhone(phone.trim());
        staff.setRole(role != null ? role.trim() : "Receptionist");
        staff.setStatus("Active");

        if (!staffDAO.insert(staff)) {
            throw new Exception("Failed to add staff. Please try again.");
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
        if (!ValidationUtil.isNotEmpty(email) || !ValidationUtil.isValidEmail(email)) {
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
        if (!staffDAO.updateStatus(staffId, "Inactive")) {
            throw new Exception("Failed to deactivate staff. Please try again.");
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


    public void resetPassword(String staffId, String newPassword) throws Exception {
        if (!ValidationUtil.isValidPassword(newPassword)) {
            throw new Exception("Password must be at least 6 characters");
        }
        Staff staff = staffDAO.findById(staffId);
        if (staff == null) {
            throw new Exception("Staff member not found: " + staffId);
        }
        String hashed = PasswordUtil.hashPassword(newPassword);
        if (!staffDAO.updatePassword(staffId, hashed)) {
            throw new Exception("Failed to reset password. Please try again.");
        }
    }


    public int[] getStaffCounts() {
        int active   = staffDAO.countByStatus("Active");
        int pending  = staffDAO.countByStatus("Pending");
        int inactive = staffDAO.countByStatus("Inactive");
        int total    = active + pending + inactive;
        return new int[]{total, active, pending, inactive};
    }
}
