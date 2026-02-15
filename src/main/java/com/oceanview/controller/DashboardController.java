package com.oceanview.controller;

import com.oceanview.service.AdminService;
import com.oceanview.service.BillingService;
import com.oceanview.service.ReservationService;
import com.oceanview.service.RoomService;
import com.oceanview.util.SessionManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


public class DashboardController extends HttpServlet {

    private final AdminService       adminService       = new AdminService();
    private final ReservationService reservationService = new ReservationService();
    private final RoomService        roomService        = new RoomService();
    private final BillingService     billingService     = new BillingService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {


        if (!SessionManager.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String role = request.getParameter("role");
        if (role == null) role = SessionManager.getLoggedInRole(request);

        if ("Admin".equalsIgnoreCase(role)) {
            showAdminDashboard(request, response);
        } else if ("Staff".equalsIgnoreCase(role)) {
            showStaffDashboard(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }


    private void showAdminDashboard(HttpServletRequest request,
                                    HttpServletResponse response)
            throws ServletException, IOException {


        if (!SessionManager.isAdmin(request)) {
            response.sendRedirect(request.getContextPath() +
                    "/dashboard?role=staff");
            return;
        }

        try {

            Map<String, Object> stats = adminService.getDashboardStats();


            request.setAttribute("pendingStaffList",
                    adminService.getPendingStaff());


            request.setAttribute("checkedInList",
                    reservationService.getReservationsByStatus("Checked-In"));


            for (Map.Entry<String, Object> entry : stats.entrySet()) {
                request.setAttribute(entry.getKey(), entry.getValue());
            }

            request.getRequestDispatcher("/admin-dashboard.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            request.setAttribute("errorMessage",
                    "Failed to load dashboard: " + e.getMessage());
            request.getRequestDispatcher("/admin-dashboard.jsp")
                    .forward(request, response);
        }
    }


    private void showStaffDashboard(HttpServletRequest request,
                                    HttpServletResponse response)
            throws ServletException, IOException {


        if (!SessionManager.isStaff(request)) {
            response.sendRedirect(request.getContextPath() +
                    "/dashboard?role=admin");
            return;
        }

        try {

            int[] roomCounts = roomService.getRoomCounts();
            request.setAttribute("totalRooms",       roomCounts[0]);
            request.setAttribute("availableRooms",   roomCounts[1]);
            request.setAttribute("occupiedRooms",    roomCounts[2]);
            request.setAttribute("reservedRooms",    roomCounts[3]);
            request.setAttribute("maintenanceRooms", roomCounts[4]);


            int[] resCounts = reservationService.getReservationCounts();
            request.setAttribute("confirmedReservations", resCounts[0]);
            request.setAttribute("checkedInReservations", resCounts[1]);
            request.setAttribute("completedReservations", resCounts[2]);
            request.setAttribute("cancelledReservations", resCounts[3]);


            int[] billCounts = billingService.getBillCounts();
            request.setAttribute("paidBills",   billCounts[0]);
            request.setAttribute("unpaidBills", billCounts[1]);


            request.setAttribute("totalRevenue",
                    billingService.getTotalRevenue());


            request.setAttribute("checkedInList",
                    reservationService.getReservationsByStatus("Checked-In"));


            request.setAttribute("confirmedList",
                    reservationService.getReservationsByStatus("Confirmed"));

            request.getRequestDispatcher("/staff-dashboard.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            request.setAttribute("errorMessage",
                    "Failed to load dashboard: " + e.getMessage());
            request.getRequestDispatcher("/staff-dashboard.jsp")
                    .forward(request, response);
        }
    }
}
;