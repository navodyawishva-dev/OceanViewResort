<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.oceanview.util.SessionManager" %>
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
    <title>Help - Ocean View Resort</title>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css">
    <style>
        body { font-family: Arial, Helvetica, sans-serif; background-color: #ecf0f1; }
        .navbar { background-color: #2c3e50; }
        .navbar-brand, .nav-link { color: white !important; }
        .content { padding: 20px; max-width: 800px; }
        .help-card {
            background: white;
            border: 1px solid #dee2e6;
            padding: 25px;
            margin-bottom: 20px;
        }
        .help-card h6 {
            background-color: #2c3e50;
            color: white;
            padding: 8px 15px;
            margin: -25px -25px 20px -25px;
            font-size: 14px;
        }
        .step {
            display: flex;
            gap: 15px;
            margin-bottom: 15px;
            align-items: flex-start;
        }
        .step-num {
            background-color: #2c3e50;
            color: white;
            width: 28px;
            height: 28px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 13px;
            flex-shrink: 0;
        }
        .step-text { font-size: 14px; padding-top: 4px; }
        .step-text strong { color: #2c3e50; }
        .badge-confirmed  { background-color: #2980b9; color: white;
            padding: 2px 8px; font-size: 11px; }
        .badge-checkedin  { background-color: #27ae60; color: white;
            padding: 2px 8px; font-size: 11px; }
        .badge-completed  { background-color: #7f8c8d; color: white;
            padding: 2px 8px; font-size: 11px; }
        .badge-cancelled  { background-color: #e74c3c; color: white;
            padding: 2px 8px; font-size: 11px; }
        .badge-available  { background-color: #27ae60; color: white;
            padding: 2px 8px; font-size: 11px; }
        .badge-occupied   { background-color: #e74c3c; color: white;
            padding: 2px 8px; font-size: 11px; }
        .badge-reserved   { background-color: #2980b9; color: white;
            padding: 2px 8px; font-size: 11px; }
        .badge-maintenance { background-color: #f39c12; color: white;
            padding: 2px 8px; font-size: 11px; }
        table { font-size: 13px; }
        .table th { background-color: #2c3e50; color: white; font-weight: normal; }
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
            <span class="nav-link"><%= username %></span>
            <a class="nav-link"
               href="${pageContext.request.contextPath}/auth?action=logout">
                Logout
            </a>
        </div>
    </div>
</nav>

<div class="content">

    <h5 style="color:#2c3e50" class="mb-4">Help &amp; User Guide</h5>


    <div class="help-card">
        <h6>How to Make a Reservation</h6>
        <div class="step">
            <div class="step-num">1</div>
            <div class="step-text">
                <strong>Register the Customer</strong> — Go to
                Customers → Register Customer. Enter guest name,
                NIC, phone. If the guest already exists (same NIC),
                the system reuses their profile automatically.
            </div>
        </div>
        <div class="step">
            <div class="step-num">2</div>
            <div class="step-text">
                <strong>Create Reservation</strong> — Go to
                Reservations → New Reservation. Select customer,
                room, check-in/out dates, number of guests.
                The system prevents double bookings automatically.
            </div>
        </div>
        <div class="step">
            <div class="step-num">3</div>
            <div class="step-text">
                <strong>Check In</strong> — On the reservations list,
                click <strong>Check In</strong> when the guest arrives.
                Status changes to
                <span class="badge-checkedin">Checked-In</span>
                and room becomes Occupied.
            </div>
        </div>
        <div class="step">
            <div class="step-num">4</div>
            <div class="step-text">
                <strong>Checkout &amp; Payment</strong> — Click
                <strong>Checkout</strong> on a checked-in reservation.
                Select payment method (Cash/Card/Online) and confirm.
                A printable receipt is generated automatically.
            </div>
        </div>
    </div>


    <div class="help-card">
        <h6>Reservation Status Guide</h6>
        <table class="table table-bordered">
            <thead>
            <tr>
                <th>Status</th>
                <th>Meaning</th>
                <th>Available Actions</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td><span class="badge-confirmed">Confirmed</span></td>
                <td>Booking created, guest not yet arrived</td>
                <td>Edit, Check In, Cancel</td>
            </tr>
            <tr>
                <td><span class="badge-checkedin">Checked-In</span></td>
                <td>Guest is currently in the room</td>
                <td>Generate Bill, Checkout</td>
            </tr>
            <tr>
                <td><span class="badge-completed">Completed</span></td>
                <td>Guest has checked out, bill paid</td>
                <td>View Bill</td>
            </tr>
            <tr>
                <td><span class="badge-cancelled">Cancelled</span></td>
                <td>Reservation was cancelled</td>
                <td>Delete</td>
            </tr>
            </tbody>
        </table>
    </div>


    <div class="help-card">
        <h6>Room Status Guide</h6>
        <table class="table table-bordered">
            <thead>
            <tr>
                <th>Status</th>
                <th>Meaning</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td><span class="badge-available">Available</span></td>
                <td>Room is free and can be booked</td>
            </tr>
            <tr>
                <td><span class="badge-reserved">Reserved</span></td>
                <td>Room has a confirmed booking</td>
            </tr>
            <tr>
                <td><span class="badge-occupied">Occupied</span></td>
                <td>Guest is currently checked in</td>
            </tr>
            <tr>
                <td><span class="badge-maintenance">Maintenance</span></td>
                <td>Room is under maintenance, not available</td>
            </tr>
            </tbody>
        </table>
    </div>


    <div class="help-card">
        <h6>Billing Guide</h6>
        <div class="step">
            <div class="step-num">1</div>
            <div class="step-text">
                <strong>Generate Bill</strong> — Click
                <strong>Bill</strong> on a checked-in reservation.
                The system calculates: Nights × Room Price = Total.
            </div>
        </div>
        <div class="step">
            <div class="step-num">2</div>
            <div class="step-text">
                <strong>Process Payment</strong> — Enter amount and
                select payment method. Partial payments are allowed —
                the bill is marked Paid once the full amount is received.
            </div>
        </div>
        <div class="step">
            <div class="step-num">3</div>
            <div class="step-text">
                <strong>Quick Checkout</strong> — Use
                <strong>Checkout</strong> to generate bill, process
                full payment, and complete the stay in one step.
                Receipt is shown immediately for printing.
            </div>
        </div>
        <div class="step">
            <div class="step-num">4</div>
            <div class="step-text">
                <strong>Print Receipt</strong> — Click
                <strong>Print Receipt</strong> on any paid bill.
                Use Ctrl+P or the Print button to print.
                The print buttons are hidden in the printed copy.
            </div>
        </div>
    </div>


    <% if ("Admin".equals(role)) { %>
    <div class="help-card">
        <h6>Admin Guide</h6>
        <div class="step">
            <div class="step-num">1</div>
            <div class="step-text">
                <strong>Approve Staff</strong> — When staff register
                themselves, their account is Pending. Go to
                Manage Staff and click Approve to activate their account.
            </div>
        </div>
        <div class="step">
            <div class="step-num">2</div>
            <div class="step-text">
                <strong>Deactivate Staff</strong> — Click Deactivate
                to prevent a staff member from logging in without
                deleting their records.
            </div>
        </div>
        <div class="step">
            <div class="step-num">3</div>
            <div class="step-text">
                <strong>Dashboard Stats</strong> — The admin dashboard
                shows live counts for staff, rooms, reservations,
                and total revenue from all paid bills.
            </div>
        </div>
    </div>
    <% } %>

    <a href="${pageContext.request.contextPath}/dashboard?role=<%= role.toLowerCase() %>"
       class="btn"
       style="background-color:#2c3e50; color:white; border-radius:0">
        Back to Dashboard
    </a>

</div>
</body>
</html>
