package com.oceanview.controller;

import com.oceanview.service.AdminService;
import com.oceanview.service.StaffService;
import com.oceanview.util.SessionManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AdminController extends HttpServlet {

    private final AdminService adminService = new AdminService();
    private final StaffService staffService = new StaffService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {


        if (!SessionManager.isLoggedIn(request) ||
                !SessionManager.isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "";

        switch (action) {
            case "manageStaff":
                showManageStaff(request, response);
                break;
            case "addStaff":
                showAddStaffForm(request, response);
                break;
            case "editStaff":
                showEditStaffForm(request, response);
                break;
            default:
                response.sendRedirect(
                        request.getContextPath() + "/dashboard?role=admin");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {


        if (!SessionManager.isLoggedIn(request) ||
                !SessionManager.isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "";

        switch (action) {
            case "saveStaff":
                handleSaveStaff(request, response);
                break;
            case "deleteStaff":
                handleDeleteStaff(request, response);
                break;
            case "approveStaff":
                handleApproveStaff(request, response);
                break;
            case "deactivateStaff":
                handleDeactivateStaff(request, response);
                break;
            case "reactivateStaff":
                handleReactivateStaff(request, response);
                break;
            default:
                response.sendRedirect(
                        request.getContextPath() + "/dashboard?role=admin");
        }
    }



    private void showManageStaff(HttpServletRequest request,
                                 HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("staffList", adminService.getAllStaff());
            request.setAttribute("pendingList", adminService.getPendingStaff());
            request.getRequestDispatcher("/manage-staff.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/manage-staff.jsp")
                    .forward(request, response);
        }
    }


    private void showAddStaffForm(HttpServletRequest request,
                                  HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("formAction", "add");
        request.getRequestDispatcher("/add-edit-staff.jsp")
                .forward(request, response);
    }

    private void showEditStaffForm(HttpServletRequest request,
                                   HttpServletResponse response)
            throws ServletException, IOException {
        String staffId = request.getParameter("staffId");
        try {
            request.setAttribute("staff", adminService.getStaffById(staffId));
            request.setAttribute("formAction", "edit");
            request.getRequestDispatcher("/add-edit-staff.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            showManageStaff(request, response);
        }
    }


    private void handleSaveStaff(HttpServletRequest request,
                                 HttpServletResponse response)
            throws ServletException, IOException {

        String staffId   = request.getParameter("staffId");
        String fullName  = request.getParameter("fullName");
        String username  = request.getParameter("username");
        String password  = request.getParameter("password");
        String email     = request.getParameter("email");
        String phone     = request.getParameter("phone");
        String role      = request.getParameter("role");

        try {
            if (staffId == null || staffId.trim().isEmpty()) {

                staffService.addStaff(
                        fullName, username, password, email, phone, role);
                request.setAttribute("successMessage",
                        "Staff member added successfully!");
            } else {

                adminService.updateStaff(
                        staffId, fullName, email, phone, role);
                request.setAttribute("successMessage",
                        "Staff member updated successfully!");
            }

            request.setAttribute("staffList", adminService.getAllStaff());
            request.setAttribute("pendingList", adminService.getPendingStaff());
            request.getRequestDispatcher("/manage-staff.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("formAction",
                    (staffId == null || staffId.isEmpty()) ? "add" : "edit");

            request.setAttribute("fullName", fullName);
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.setAttribute("role", role);
            request.getRequestDispatcher("/add-edit-staff.jsp")
                    .forward(request, response);
        }
    }


    private void handleDeleteStaff(HttpServletRequest request,
                                   HttpServletResponse response)
            throws ServletException, IOException {
        String staffId = request.getParameter("staffId");
        try {
            adminService.deleteStaff(staffId);
            request.setAttribute("successMessage",
                    "Staff member deleted successfully!");
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
        }

        request.setAttribute("staffList", adminService.getAllStaff());
        request.setAttribute("pendingList", adminService.getPendingStaff());
        request.getRequestDispatcher("/manage-staff.jsp")
                .forward(request, response);
    }


    private void handleApproveStaff(HttpServletRequest request,
                                    HttpServletResponse response)
            throws ServletException, IOException {
        String staffId = request.getParameter("staffId");
        try {
            adminService.approveStaff(staffId);
            request.setAttribute("successMessage",
                    "Staff account approved successfully!");
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        request.setAttribute("staffList", adminService.getAllStaff());
        request.setAttribute("pendingList", adminService.getPendingStaff());
        request.getRequestDispatcher("/manage-staff.jsp")
                .forward(request, response);
    }


    private void handleDeactivateStaff(HttpServletRequest request,
                                       HttpServletResponse response)
            throws ServletException, IOException {
        String staffId = request.getParameter("staffId");
        try {
            adminService.deactivateStaff(staffId);
            request.setAttribute("successMessage",
                    "Staff account deactivated.");
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        request.setAttribute("staffList", adminService.getAllStaff());
        request.setAttribute("pendingList", adminService.getPendingStaff());
        request.getRequestDispatcher("/manage-staff.jsp")
                .forward(request, response);
    }


    private void handleReactivateStaff(HttpServletRequest request,
                                       HttpServletResponse response)
            throws ServletException, IOException {
        String staffId = request.getParameter("staffId");
        try {
            adminService.reactivateStaff(staffId);
            request.setAttribute("successMessage",
                    "Staff account reactivated successfully!");
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        request.setAttribute("staffList", adminService.getAllStaff());
        request.setAttribute("pendingList", adminService.getPendingStaff());
        request.getRequestDispatcher("/manage-staff.jsp")
                .forward(request, response);
    }
}
