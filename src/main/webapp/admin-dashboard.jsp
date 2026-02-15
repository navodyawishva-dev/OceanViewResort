<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.oceanview.util.SessionManager" %>
<%
    if (!SessionManager.isLoggedIn(request) || !SessionManager.isAdmin(request)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
       return;
    }
    String username = SessionManager.getLoggedInUsername(request);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard - Ocean View Resort</title>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css">
    <style>
        body { font-family: Arial, Helvetica, sans-serif; background-color: #ecf0f1; }
        .navbar { background-color: #2c3e50; }
        .navbar-brand, .nav-link { color: white !important; }
        .stat-card {
            background: white;
            border: 1px solid #dee2e6;
            padding: 20px;
            margin-bottom: 20px;
        }
        .stat-card h3 { font-size: 32px; color: #2c3e50; margin: 0; }
        .stat-card p  { color: #7f8c8d; margin: 5px 0 0 0; font-size: 14px; }
        .section-title {
            background-color: #2c3e50;
            color: white;
            padding: 8px 15px;
            font-size: 14px;
            margin-bottom: 15px;
        }
        .table { background: white; }
        .table th { background-color: #2c3e50; color: white; font-weight: normal; }
        .btn-sm { border-radius: 0; font-size: 12px; }
        .btn-success { background-color: #27ae60; border-color: #27ae60; border-radius: 0; }
        .btn-danger  { background-color: #e74c3c; border-color: #e74c3c; border-radius: 0; }
        .btn-primary { background-color: #2c3e50; border-color: #2c3e50; border-radius: 0; }
        .badge-pending  { background-color: #f39c12; color: white; padding: 3px 8px; font-size: 12px; }
        .badge-active   { background-color: #27ae60; color: white; padding: 3px 8px; font-size: 12px; }
        .badge-inactive { background-color: #e74c3c; color: white; padding: 3px 8px; font-size: 12px; }
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
            <span class="nav-link">Admin: <%= username %></span>
            <a class="nav-link"
               href="${pageContext.request.contextPath}/auth?action=logout">
                Logout
            </a>
        </div>
    </div>
</nav>

<div class="content">

    <!-- Messages -->
    <% String error = (String) request.getAttribute("errorMessage"); %>
    <% if (error != null) { %>
    <div class="alert alert-danger"><%= error %></div>
    <% } %>
    <% String success = (String) request.getAttribute("successMessage"); %>
    <% if (success != null) { %>
    <div class="alert alert-success"><%= success %></div>
    <% } %>

    <!-- Page Title -->
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h5 style="color:#2c3e50">Admin Dashboard</h5>
    </div>

    <!-- Stats Row 1: Staff -->
    <div class="row">
        <div class="col-md-3">
            <div class="stat-card">
                <h3><%= request.getAttribute("totalStaff") != null
                        ? request.getAttribute("totalStaff") : 0 %></h3>
                <p>Active Staff</p>
            </div>
        </div>
        <div class="col-md-3">
            <div class="stat-card">
                <h3 style="color:#f39c12">
                    <%= request.getAttribute("pendingStaff") != null
                            ? request.getAttribute("pendingStaff") : 0 %>
                </h3>
                <p>Pending Approvals</p>
            </div>
        </div>
        <div class="col-md-3">
            <div class="stat-card">
                <h3><%= request.getAttribute("totalCustomers") != null
                        ? request.getAttribute("totalCustomers") : 0 %></h3>
                <p>Total Customers</p>
            </div>
        </div>
        <div class="col-md-3">
            <div class="stat-card">
                <h3 style="color:#27ae60">
                    LKR <%= request.getAttribute("totalRevenue") != null
                        ? String.format("%.0f",
                        (Double)request.getAttribute("totalRevenue")) : 0 %>
                </h3>
                <p>Total Revenue</p>
            </div>
        </div>
    </div>

    <!-- Stats Row 2: Rooms -->
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
                <h3 style="color:#2c3e50">
                    <%= request.getAttribute("confirmedReservations") != null
                            ? request.getAttribute("confirmedReservations") : 0 %>
                </h3>
                <p>Confirmed Reservations</p>
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
    </div>

    <!-- Quick Nav -->
    <div class="section-title">Quick Actions</div>
    <div class="mb-4">
        <a href="${pageContext.request.contextPath}/admin?action=manageStaff"
           class="btn btn-primary me-2">Manage Staff</a>

    </div>

    <!-- Pending Staff Approvals -->
    <div class="section-title">Pending Staff Approvals</div>
    <%
        java.util.List pendingList =
                (java.util.List) request.getAttribute("pendingStaffList");
    %>
    <% if (pendingList == null || pendingList.isEmpty()) { %>
    <p class="text-muted" style="font-size:14px">
        No pending approvals.
    </p>
    <% } else { %>
    <table class="table table-striped table-bordered mb-4">
        <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Username</th>
            <th>Email</th>
            <th>Role</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <% for (Object obj : pendingList) {
            com.oceanview.model.Staff s = (com.oceanview.model.Staff) obj; %>
        <tr>
            <td><%= s.getId() %></td>
            <td><%= s.getFullName() %></td>
            <td><%= s.getUsername() %></td>
            <td><%= s.getEmail() %></td>
            <td><%= s.getRole() %></td>
            <td>
                <form method="post"
                      action="${pageContext.request.contextPath}/admin"
                      style="display:inline">
                    <input type="hidden" name="action" value="approveStaff">
                    <input type="hidden" name="staffId" value="<%= s.getId() %>">
                    <button type="submit" class="btn btn-success btn-sm">
                        Approve
                    </button>
                </form>
                <form method="post"
                      action="${pageContext.request.contextPath}/admin"
                      style="display:inline">
                    <input type="hidden" name="action" value="deleteStaff">
                    <input type="hidden" name="staffId" value="<%= s.getId() %>">
                    <button type="submit" class="btn btn-danger btn-sm"
                            onclick="return confirm('Reject this application?')">
                        Reject
                    </button>
                </form>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>
    <% } %>

    <!-- Currently Checked In -->
    <div class="section-title">Currently Checked In</div>
    <%
        java.util.List checkedInList =
                (java.util.List) request.getAttribute("checkedInList");
    %>
    <% if (checkedInList == null || checkedInList.isEmpty()) { %>
    <p class="text-muted" style="font-size:14px">No guests currently checked in.</p>
    <% } else { %>
    <table class="table table-striped table-bordered">
        <thead>
        <tr>
            <th>Reservation ID</th>
            <th>Guest Name</th>
            <th>Room</th>
            <th>Check-in</th>
            <th>Check-out</th>
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
            <td><%= com.oceanview.util.DateUtil.formatDate(r.getCheckInDate()) %></td>
            <td><%= com.oceanview.util.DateUtil.formatDate(r.getCheckOutDate()) %></td>
        </tr>
        <% } %>
        </tbody>
    </table>
    <% } %>

</div>
</body>
</html>