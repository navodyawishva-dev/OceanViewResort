<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.oceanview.util.SessionManager" %>
<%@ page import="com.oceanview.util.DateUtil" %>
<%@ page import="com.oceanview.model.*" %>
<%
    if (!SessionManager.isLoggedIn(request)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    String username       = SessionManager.getLoggedInUsername(request);
    Reservation reservation = (Reservation) request.getAttribute("reservation");
    Bill bill               = (Bill) request.getAttribute("bill");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Process Checkout - Ocean View Resort</title>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css">
    <style>
        body { font-family: Arial, Helvetica, sans-serif; background-color: #ecf0f1; }
        .navbar { background-color: #2c3e50; }
        .navbar-brand, .nav-link { color: white !important; }
        .content { padding: 20px; }
        .checkout-card {
            background: white;
            border: 1px solid #dee2e6;
            padding: 25px;
            max-width: 600px;
        }
        .checkout-card h6 {
            background-color: #2c3e50;
            color: white;
            padding: 8px 15px;
            margin: -25px -25px 20px -25px;
            font-size: 14px;
        }
        .section-title {
            background-color: #ecf0f1;
            padding: 6px 10px;
            font-size: 13px;
            font-weight: bold;
            color: #2c3e50;
            margin: 15px 0 10px 0;
            border-left: 4px solid #2c3e50;
        }
        .info-row {
            display: flex;
            justify-content: space-between;
            font-size: 14px;
            padding: 6px 0;
            border-bottom: 1px solid #f5f5f5;
        }
        .info-row span:first-child { color: #7f8c8d; }
        .total-row {
            display: flex;
            justify-content: space-between;
            font-size: 18px;
            font-weight: bold;
            color: #2c3e50;
            padding: 10px 0;
        }
        .form-control { border-radius: 0; }
        .form-select  { border-radius: 0; }
        .btn-success  { background-color: #27ae60; border-color: #27ae60;
            border-radius: 0; }
        .btn-secondary { border-radius: 0; }
        .alert { border-radius: 0; }
    </style>
</head>
<body>


<nav class="navbar navbar-expand-lg">
    <div class="container-fluid">
        <span class="navbar-brand">Ocean View Resort</span>
        <div class="navbar-nav ms-auto">
            <a class="nav-link"
               href="${pageContext.request.contextPath}/staff?action=manageReservations">
                Reservations
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

    <% String error = (String) request.getAttribute("errorMessage"); %>
    <% if (error != null) { %>
    <div class="alert alert-danger" style="max-width:600px">
        <%= error %>
    </div>
    <% } %>

    <% if (reservation != null) {
        Customer cust = reservation.getCustomer();
        Room room     = reservation.getRoom();
        int nights    = reservation.getNumNights();
        double price  = room.getPricePerNight();
        double total  = nights * price;
    %>

    <div class="checkout-card">
        <h6>Process Checkout</h6>


        <div class="section-title">Guest Summary</div>
        <div class="info-row">
            <span>Guest</span>
            <span><strong><%= cust.getFullName() %></strong></span>
        </div>
        <div class="info-row">
            <span>Room</span>
            <span>Room <%= room.getRoomNumber() %>
                  (<%= room.getRoomType().getTypeName() %>)</span>
        </div>
        <div class="info-row">
            <span>Check-in</span>
            <span><%= DateUtil.formatDate(
                    reservation.getCheckInDate()) %></span>
        </div>
        <div class="info-row">
            <span>Check-out</span>
            <span><%= DateUtil.formatDate(
                    reservation.getCheckOutDate()) %></span>
        </div>
        <div class="info-row">
            <span>Duration</span>
            <span><%= nights %> night(s)</span>
        </div>


        <div class="section-title">Bill Summary</div>
        <div class="info-row">
            <span>Room Rate</span>
            <span>LKR <%= String.format("%.2f", price) %> / night</span>
        </div>
        <div class="info-row">
            <span>Subtotal</span>
            <span>LKR <%= String.format("%.2f", total) %></span>
        </div>
        <div class="info-row">
            <span>Tax</span>
            <span>LKR 0.00</span>
        </div>
        <div class="total-row">
            <span>TOTAL DUE</span>
            <span>LKR <%= String.format("%.2f", total) %></span>
        </div>


        <div class="section-title">Payment Method</div>
        <form method="post"
              action="${pageContext.request.contextPath}/staff">
            <input type="hidden" name="action" value="confirmCheckout">
            <input type="hidden" name="reservationId"
                   value="<%= reservation.getReservationId() %>">

            <div class="mb-4">
                <select class="form-select" name="paymentMethod">
                    <option value="Cash">Cash</option>
                    <option value="Card">Card</option>
                    <option value="Online">Online</option>
                </select>
            </div>

            <div class="d-flex gap-2">
                <button type="submit"
                        class="btn btn-success"
                        onclick="return confirm('Confirm checkout and process payment of LKR <%= String.format("%.2f", total) %>?')">
                    Confirm Checkout &amp; Pay
                </button>
                <a href="${pageContext.request.contextPath}/staff?action=manageReservations"
                   class="btn btn-secondary">
                    Cancel
                </a>
            </div>
        </form>
    </div>

    <% } else { %>
    <div class="alert alert-warning" style="max-width:600px">
        Reservation not found.
        <a href="${pageContext.request.contextPath}/staff?action=manageReservations">
            Back to Reservations
        </a>
    </div>
    <% } %>

</div>
</body>
</html>