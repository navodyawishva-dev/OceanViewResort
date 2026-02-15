<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.oceanview.util.SessionManager" %>
<%
    if (!SessionManager.isLoggedIn(request) || !SessionManager.isStaff(request)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    String username = SessionManager.getLoggedInUsername(request);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Staff Dashboard - Ocean View Resort</title>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css">
    <style>
        body { font-family: Arial, Helvetica, sans-serif; background-color: #ecf0f1; }
        .navbar  { background-color: #2c3e50; }
        .navbar-brand, .nav-link { color: white !important; }
        .sidebar {
            background-color: #2c3e50;
            min-height: calc(100vh - 56px);
            padding: 0;
        }
        .sidebar a {
            display: block;
            color: #bdc3c7;
            padding: 12px 20px;
            text-decoration: none;
            font-size: 14px;
            border-bottom: 1px solid #34495e;
        }
        .sidebar a:hover  { background-color: #34495e; color: white; }
        .sidebar .section-header {
            color: #7f8c8d;
            padding: 10px 20px 5px;
            font-size: 11px;
            text-transform: uppercase;
            letter-spacing: 1px;
        }
        .stat-card {
            background: white;
            border: 1px solid #dee2e6;
            padding: 15px;
            margin-bottom: 15px;
        }
        .stat-card h3 { font-size: 28px; color: #2c3e50; margin: 0; }
        .stat-card p  { color: #7f8c8d; margin: 5px 0 0 0; font-size: 13px; }
        .section-title {
            background-color: #2c3e50;
            color: white;
            padding: 8px 15px;
            font-size: 14px;
            margin-bottom: 15px;
        }
        .table { background: white; }
        .table th { background-color: #2c3e50; color: white; font-weight: normal; }
        .btn-primary { background-color: #2c3e50; border-color: #2c3e50; border-radius: 0; }
        .btn-success { background-color: #27ae60; border-color: #27ae60; border-radius: 0; }
        .alert { border-radius: 0; }
        .content { padding: 20px; }
    </style>
</head>
<body>

<!-- Navbar -->
<nav class="navbar navbar-expand-lg">
    <div class="container-fluid">
        <span class="navbar-brand">Ocean View Resort</span>
        <div class="navbar-nav ms-auto">
            <span class="nav-link">Staff: <%= username %></span>
            <a class="nav-link"
               href="${pageContext.request.contextPath}/auth?action=logout">
                Logout
            </a>
        </div>
    </div>
</nav>

<div class="container-fluid">
    <div class="row">

        <!-- Sidebar -->
        <div class="col-md-2 sidebar">
            <div class="section-header">Reservations</div>
            <a href="${pageContext.request.contextPath}/staff?action=manageReservations">
                All Reservations
            </a>
            <a href="${pageContext.request.contextPath}/staff?action=addReservation">
                New Reservation
            </a>
            <div class="section-header">Rooms</div>
            <a href="${pageContext.request.contextPath}/staff?action=manageRooms">
                Manage Rooms
            </a>
            <a href="${pageContext.request.contextPath}/staff?action=manageRoomTypes">
                Room Types
            </a>
            <div class="section-header">Guests</div>
            <a href="${pageContext.request.contextPath}/staff?action=manageCustomers">
                Customers
            </a>
            <div class="section-header">Billing</div>
            <a href="${pageContext.request.contextPath}/staff?action=manageReservations">
                Generate Bill
            </a>
            <div class="section-header">System</div>
            <a href="${pageContext.request.contextPath}/staff?action=help">
                Help
            </a>
            <a href="${pageContext.request.contextPath}/auth?action=logout">
                Logout
            </a>
        </div>

        <!-- Main Content -->
        <div class="col-md-10 content">

            <h5 style="color:#2c3e50" class="mb-3">Staff Dashboard</h5>

            <!-- Messages -->
            <% String error = (String) request.getAttribute("errorMessage"); %>
            <% if (error != null) { %>
            <div class="alert alert-danger"><%= error %></div>
            <% } %>

            <!-- Stats Row 1 -->
            <div class="row">
                <div class="col-md-3">
                    <div class="stat-card">
                        <h3 style="color:#27ae60">
                            <%= request.getAttribute("availableRooms") != null
                                    ? request.getAttribute("availableRooms") : 0 %>
                        </h3>
                        <p>Available Rooms</p>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stat-card">
                        <h3 style="color:#e74c3c">
                            <%= request.getAttribute("occupiedRooms") != null
                                    ? request.getAttribute("occupiedRooms") : 0 %>
                        </h3>
                        <p>Occupied Rooms</p>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stat-card">
                        <h3>
                            <%= request.getAttribute("confirmedReservations") != null
                                    ? request.getAttribute("confirmedReservations") : 0 %>
                        </h3>
                        <p>Confirmed Reservations</p>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stat-card">
                        <h3 style="color:#e74c3c">
                            <%= request.getAttribute("checkedInReservations") != null
                                    ? request.getAttribute("checkedInReservations") : 0 %>
                        </h3>
                        <p>Checked In</p>
                    </div>
                </div>
            </div>

            <!-- Stats Row 2 -->
            <div class="row">
                <div class="col-md-3">
                    <div class="stat-card">
                        <h3 style="color:#27ae60">
                            <%= request.getAttribute("paidBills") != null
                                    ? request.getAttribute("paidBills") : 0 %>
                        </h3>
                        <p>Paid Bills</p>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stat-card">
                        <h3 style="color:#e74c3c">
                            <%= request.getAttribute("unpaidBills") != null
                                    ? request.getAttribute("unpaidBills") : 0 %>
                        </h3>
                        <p>Unpaid Bills</p>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stat-card">
                        <h3 style="color:#27ae60">
                            LKR <%= request.getAttribute("totalRevenue") != null
                                ? String.format("%.0f",
                                (Double)request.getAttribute("totalRevenue"))
                                : 0 %>
                        </h3>
                        <p>Total Revenue</p>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stat-card">
                        <h3>
                            <%= request.getAttribute("completedReservations") != null
                                    ? request.getAttribute("completedReservations") : 0 %>
                        </h3>
                        <p>Completed Stays</p>
                    </div>
                </div>
            </div>

            <!-- Currently Checked In -->
            <div class="section-title">Currently Checked In</div>
            <%
                java.util.List checkedInList =
                        (java.util.List) request.getAttribute("checkedInList");
            %>
            <% if (checkedInList == null || checkedInList.isEmpty()) { %>
            <p class="text-muted" style="font-size:14px">
                No guests currently checked in.
            </p>
            <% } else { %>
            <table class="table table-striped table-bordered mb-4">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Guest</th>
                    <th>Room</th>
                    <th>Check-in</th>
                    <th>Check-out</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <% for (Object obj : checkedInList) {
                    com.oceanview.model.Reservation r =
                            (com.oceanview.model.Reservation) obj; %>
                <tr>
                    <td><%= r.getReservationId() %></td>
                    <td><%= r.getCustomer().getFullName() %></td>
                    <td>Room <%= r.getRoom().getRoomNumber() %></td>
                    <td><%= com.oceanview.util.DateUtil.formatDate(
                            r.getCheckInDate()) %></td>
                    <td><%= com.oceanview.util.DateUtil.formatDate(
                            r.getCheckOutDate()) %></td>
                    <td>
                        <a href="${pageContext.request.contextPath}/staff?action=generateBill&reservationId=<%= r.getReservationId() %>"
                           class="btn btn-primary btn-sm">
                            Generate Bill
                        </a>
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>
            <% } %>

            <!-- Confirmed Reservations -->
            <div class="section-title">Confirmed Reservations</div>
            <%
                java.util.List confirmedList =
                        (java.util.List) request.getAttribute("confirmedList");
            %>
            <% if (confirmedList == null || confirmedList.isEmpty()) { %>
            <p class="text-muted" style="font-size:14px">
                No confirmed reservations.
            </p>
            <% } else { %>
            <table class="table table-striped table-bordered">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Guest</th>
                    <th>Room</th>
                    <th>Check-in</th>
                    <th>Check-out</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <% for (Object obj : confirmedList) {
                    com.oceanview.model.Reservation r =
                            (com.oceanview.model.Reservation) obj; %>
                <tr>
                    <td><%= r.getReservationId() %></td>
                    <td><%= r.getCustomer().getFullName() %></td>
                    <td>Room <%= r.getRoom().getRoomNumber() %></td>
                    <td><%= com.oceanview.util.DateUtil.formatDate(
                            r.getCheckInDate()) %></td>
                    <td><%= com.oceanview.util.DateUtil.formatDate(
                            r.getCheckOutDate()) %></td>
                    <td>
                        <form method="post"
                              action="${pageContext.request.contextPath}/staff"
                              style="display:inline">
                            <input type="hidden" name="action" value="checkIn">
                            <input type="hidden" name="reservationId"
                                   value="<%= r.getReservationId() %>">
                            <button type="submit"
                                    class="btn btn-success btn-sm">
                                Check In
                            </button>
                        </form>
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>
            <% } %>

        </div><!-- end main content -->
    </div><!-- end row -->
</div><!-- end container -->

</body>
</html>