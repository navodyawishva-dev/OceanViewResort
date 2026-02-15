<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.oceanview.util.SessionManager" %>
<%@ page import="com.oceanview.model.Reservation" %>
<%@ page import="java.util.List" %>
<%
    if (!SessionManager.isLoggedIn(request)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    String username = SessionManager.getLoggedInUsername(request);
    String role     = SessionManager.getLoggedInRole(request);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manage Reservations - Ocean View Resort</title>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css">
    <style>
        body { font-family: Arial, Helvetica, sans-serif; background-color: #ecf0f1; }
        .navbar { background-color: #2c3e50; }
        .navbar-brand, .nav-link { color: white !important; }
        .content { padding: 20px; }
        .section-title {
            background-color: #2c3e50;
            color: white;
            padding: 8px 15px;
            font-size: 14px;
            margin-bottom: 15px;
        }
        .table { background: white; }
        .table th { background-color: #2c3e50; color: white; font-weight: normal; }
        .btn-primary  { background-color: #2c3e50; border-color: #2c3e50; border-radius: 0; }
        .btn-success  { background-color: #27ae60; border-color: #27ae60; border-radius: 0; }
        .btn-danger   { background-color: #e74c3c; border-color: #e74c3c; border-radius: 0; }
        .btn-warning  { background-color: #f39c12; border-color: #f39c12;
            border-radius: 0; color: white; }
        .btn-info     { background-color: #2980b9; border-color: #2980b9;
            border-radius: 0; color: white; }
        .btn-sm { font-size: 12px; }
        .alert { border-radius: 0; }
        .form-control { border-radius: 0; }
        .form-select  { border-radius: 0; }
        .badge-confirmed  { background-color: #2980b9; color: white;
            padding: 3px 8px; font-size: 11px; }
        .badge-checkedin  { background-color: #27ae60; color: white;
            padding: 3px 8px; font-size: 11px; }
        .badge-completed  { background-color: #7f8c8d; color: white;
            padding: 3px 8px; font-size: 11px; }
        .badge-cancelled  { background-color: #e74c3c; color: white;
            padding: 3px 8px; font-size: 11px; }
    </style>
</head>
<body>


<nav class="navbar navbar-expand-lg">
    <div class="container-fluid">
        <span class="navbar-brand">Ocean View Resort</span>
        <div class="navbar-nav ms-auto">
            <a class="nav-link"
               href="${pageContext.request.contextPath}/dashboard?role=<%= role.toLowerCase() %>">
                Dashboard
            </a>
            <span class="nav-link"><%= role %>: <%= username %></span>
            <a class="nav-link"
               href="${pageContext.request.contextPath}/auth?action=logout">
                Logout
            </a>
        </div>
    </div>
</nav>

<div class="content">


    <% String error = (String) request.getAttribute("errorMessage"); %>
    <% if (error != null) { %>
    <div class="alert alert-danger"><%= error %></div>
    <% } %>
    <% String success = (String) request.getAttribute("successMessage"); %>
    <% if (success != null) { %>
    <div class="alert alert-success"><%= success %></div>
    <% } %>


    <div class="d-flex justify-content-between align-items-center mb-3">
        <h5 style="color:#2c3e50">Manage Reservations</h5>
        <a href="${pageContext.request.contextPath}/staff?action=addReservation"
           class="btn btn-primary">
            + New Reservation
        </a>
    </div>


    <div class="section-title">Search & Filter</div>
    <form method="get"
          action="${pageContext.request.contextPath}/staff"
          class="row g-2 mb-4">
        <input type="hidden" name="action" value="searchReservations">
        <div class="col-md-4">
            <input type="text" class="form-control" name="keyword"
                   placeholder="Search by ID, guest name or room..."
                   value="<%= request.getAttribute("keyword") != null
                              ? request.getAttribute("keyword") : "" %>">
        </div>
        <div class="col-md-3">
            <select class="form-select" name="status">
                <option value="">-- All Statuses --</option>
                <option value="Confirmed"
                        <%= "Confirmed".equals(request.getAttribute("selectedStatus"))
                                ? "selected" : "" %>>Confirmed</option>
                <option value="Checked-In"
                        <%= "Checked-In".equals(request.getAttribute("selectedStatus"))
                                ? "selected" : "" %>>Checked-In</option>
                <option value="Completed"
                        <%= "Completed".equals(request.getAttribute("selectedStatus"))
                                ? "selected" : "" %>>Completed</option>
                <option value="Cancelled"
                        <%= "Cancelled".equals(request.getAttribute("selectedStatus"))
                                ? "selected" : "" %>>Cancelled</option>
            </select>
        </div>
        <div class="col-md-2">
            <button type="submit" class="btn btn-primary w-100">Search</button>
        </div>
        <div class="col-md-2">
            <a href="${pageContext.request.contextPath}/staff?action=manageReservations"
               class="btn btn-secondary w-100">Clear</a>
        </div>
    </form>


    <div class="section-title">Reservations List</div>
    <%
        List reservationList = (List) request.getAttribute("reservationList");
    %>
    <% if (reservationList == null || reservationList.isEmpty()) { %>
    <p class="text-muted" style="font-size:14px">No reservations found.</p>
    <% } else { %>
    <table class="table table-striped table-bordered">
        <thead>
        <tr>
            <th>ID</th>
            <th>Guest</th>
            <th>Room</th>
            <th>Type</th>
            <th>Check-in</th>
            <th>Check-out</th>
            <th>Nights</th>
            <th>Status</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <% for (Object obj : reservationList) {
            Reservation r = (Reservation) obj; %>
        <tr>
            <td><%= r.getReservationId() %></td>
            <td><%= r.getCustomer().getFullName() %></td>
            <td>Room <%= r.getRoom().getRoomNumber() %></td>
            <td><%= r.getRoom().getRoomType().getTypeName() %></td>
            <td><%= com.oceanview.util.DateUtil.formatDate(
                    r.getCheckInDate()) %></td>
            <td><%= com.oceanview.util.DateUtil.formatDate(
                    r.getCheckOutDate()) %></td>
            <td><%= r.getNumNights() %></td>
            <td>
                <% if ("Confirmed".equals(r.getStatus())) { %>
                <span class="badge-confirmed">Confirmed</span>
                <% } else if ("Checked-In".equals(r.getStatus())) { %>
                <span class="badge-checkedin">Checked-In</span>
                <% } else if ("Completed".equals(r.getStatus())) { %>
                <span class="badge-completed">Completed</span>
                <% } else { %>
                <span class="badge-cancelled">Cancelled</span>
                <% } %>
            </td>
            <td>

                <% if ("Confirmed".equals(r.getStatus())) { %>
                <a href="${pageContext.request.contextPath}/staff?action=editReservation&reservationId=<%= r.getReservationId() %>"
                   class="btn btn-primary btn-sm">Edit</a>


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


                <form method="post"
                      action="${pageContext.request.contextPath}/staff"
                      style="display:inline">
                    <input type="hidden" name="action" value="cancelReservation">
                    <input type="hidden" name="reservationId"
                           value="<%= r.getReservationId() %>">
                    <button type="submit"
                            class="btn btn-warning btn-sm"
                            onclick="return confirm('Cancel this reservation?')">
                        Cancel
                    </button>
                </form>
                <% } %>


                <% if ("Checked-In".equals(r.getStatus())) { %>
                <a href="${pageContext.request.contextPath}/staff?action=generateBill&reservationId=<%= r.getReservationId() %>"
                   class="btn btn-info btn-sm">
                    Bill
                </a>
                <a href="${pageContext.request.contextPath}/staff?action=processCheckout&reservationId=<%= r.getReservationId() %>"
                   class="btn btn-success btn-sm">
                    Checkout
                </a>
                <% } %>


                <% if ("Cancelled".equals(r.getStatus())) { %>
                <form method="post"
                      action="${pageContext.request.contextPath}/staff"
                      style="display:inline">
                    <input type="hidden" name="action"
                           value="deleteReservation">
                    <input type="hidden" name="reservationId"
                           value="<%= r.getReservationId() %>">
                    <button type="submit"
                            class="btn btn-danger btn-sm"
                            onclick="return confirm('Permanently delete this reservation?')">
                        Delete
                    </button>
                </form>
                <% } %>


                <% if ("Completed".equals(r.getStatus())) { %>
                <a href="${pageContext.request.contextPath}/staff?action=generateBill&reservationId=<%= r.getReservationId() %>"
                   class="btn btn-info btn-sm">
                    View Bill
                </a>
                <% } %>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>
    <% } %>
</div>

</body>
</html>