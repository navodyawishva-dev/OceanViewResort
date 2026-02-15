package com.oceanview.service;

import com.oceanview.dao.AdminDAO;
import com.oceanview.dao.StaffDAO;
import com.oceanview.exception.AuthenticationException;
import com.oceanview.model.Admin;
import com.oceanview.model.Staff;
import com.oceanview.util.IdGenerator;
import com.oceanview.util.PasswordUtil;
import com.oceanview.util.ValidationUtil;


public class AuthenticationService {

    private final AdminDAO adminDAO = new AdminDAO();
    private final StaffDAO staffDAO = new StaffDAO();


    public Object login(String username, String password) {

        if (username == null || username.trim().isEmpty()) {
            throw new AuthenticationException("Username is required");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new AuthenticationException("Password is required");
        }


        try {
            Admin admin = adminDAO.findByUsername(username.trim());
            if (admin != null) {
                if (!PasswordUtil.verifyPassword(password, admin.getPassword())) {
                    throw new AuthenticationException(
                            "Invalid username or password");
                }
                return admin;
            }
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("Admin lookup error: " + e.getMessage());
        }


        try {
            Staff staff = staffDAO.findByUsername(username.trim());
            if (staff != null) {
                if (!PasswordUtil.verifyPassword(password, staff.getPassword())) {
                    throw new AuthenticationException(
                            "Invalid username or password");
                }
                if ("Pending".equals(staff.getStatus())) {
                    throw new AuthenticationException(
                            "Your account is pending admin approval.");
                }
                if ("Inactive".equals(staff.getStatus())) {
                    throw new AuthenticationException(
                            "Your account has been deactivated. Contact admin.");
                }
                return staff;
            }
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("Staff lookup error: " + e.getMessage());
        }

        throw new AuthenticationException("Invalid username or password");
    }


    public void registerStaff(String username, String password,
                              String fullName,  String email,
                              String phone,     String role) {

        if (username == null || username.trim().isEmpty()) {
            throw new AuthenticationException("Username is required");
        }
        if (password == null || password.length() < 6) {
            throw new AuthenticationException(
                    "Password must be at least 6 characters");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new AuthenticationException("Full name is required");
        }


        if (staffDAO.usernameExists(username.trim())) {
            throw new AuthenticationException(
                    "Username already taken. Choose another.");
        }
        if (adminDAO.usernameExists(username.trim())) {
            throw new AuthenticationException(
                    "Username already taken. Choose another.");
        }


        Staff staff = new Staff();
        staff.setId(IdGenerator.generateStaffId());
        staff.setUsername(username.trim());
        staff.setPassword(PasswordUtil.hashPassword(password));
        staff.setFullName(fullName.trim());
        staff.setEmail(email  != null ? email.trim()  : "");
        staff.setPhone(phone  != null ? phone.trim()  : "");
        staff.setRole(role    != null ? role.trim()   : "Receptionist");
        staff.setStatus("Pending");

        boolean saved = staffDAO.insert(staff);
        if (!saved) {
            throw new AuthenticationException(
                    "Registration failed. Please try again.");
        }
    }
}