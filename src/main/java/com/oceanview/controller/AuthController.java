package com.oceanview.controller;

import com.oceanview.model.Admin;
import com.oceanview.model.Staff;
import com.oceanview.service.AuthenticationService;
import com.oceanview.util.SessionManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthController extends HttpServlet {

    private final AuthenticationService authService =
            new AuthenticationService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "";

        switch (action) {
            case "signup":
                request.getRequestDispatcher("/signup.jsp")
                        .forward(request, response);
                break;
            case "logout":
                handleLogout(request, response);
                break;
            default:
                response.sendRedirect(
                        request.getContextPath() + "/login.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "";

        switch (action) {
            case "login":
                handleLogin(request, response);
                break;
            case "logout":
                handleLogout(request, response);
                break;
            case "signup":
                handleSignup(request, response);
                break;
            default:
                response.sendRedirect(
                        request.getContextPath() + "/login.jsp");
        }
    }

    private void handleLogin(HttpServletRequest request,
                             HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            Object user = authService.login(username, password);

            if (user instanceof Admin) {
                Admin admin = (Admin) user;
                SessionManager.createAdminSession(request, admin);
                response.sendRedirect(
                        request.getContextPath() + "/dashboard?role=admin");

            } else if (user instanceof Staff) {
                Staff staff = (Staff) user;
                SessionManager.createStaffSession(request, staff);
                response.sendRedirect(
                        request.getContextPath() + "/dashboard?role=staff");
            }

        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("username", username);
            request.getRequestDispatcher("/login.jsp")
                    .forward(request, response);
        }
    }

    private void handleLogout(HttpServletRequest request,
                              HttpServletResponse response)
            throws IOException {

        SessionManager.destroySession(request);
        response.sendRedirect(
                request.getContextPath() + "/login.jsp?message=loggedout");
    }

    private void handleSignup(HttpServletRequest request,
                              HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String email    = request.getParameter("email");
        String phone    = request.getParameter("phone");
        String role     = request.getParameter("role");

        try {
            authService.registerStaff(
                    username, password, fullName, email, phone, role);

            response.sendRedirect(
                    request.getContextPath() +
                            "/login.jsp?message=signup_success");

        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("username", username);
            request.setAttribute("fullName", fullName);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.getRequestDispatcher("/signup.jsp")
                    .forward(request, response);
        }
    }
}
