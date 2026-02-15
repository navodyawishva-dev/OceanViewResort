<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.oceanview.util.SessionManager" %>
<%@ page import="com.oceanview.util.DateUtil" %>
<%@ page import="com.oceanview.model.*" %>
<%@ page import="java.util.List" %>
<%
    if (!SessionManager.isLoggedIn(request)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    String username = SessionManager.getLoggedInUsername(request);
    String role     = SessionManager.getLoggedInRole(request);
    Bill bill       = (Bill) request.getAttribute("bill");
    List payments   = (List) request.getAttribute("payments");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bill - Ocean View Resort</title>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css">
    <style>
        body { font-family: Arial, Helvetica, sans-serif; background-color: #ecf0f1; }
        .navbar { background-color: #2c3e50; }
        .navbar-brand, .nav-link { color: white !important; }
        .content { padding: 20px; }
        .bill-card {
            background: white;
            border: 1px solid #dee2e6;
            padding: 25px;
            max-width: 700px;
        }
        .bill-header {
            background-color: #2c3e50;
            color: white;
            padding: 15px;
            margin: -25px -25px 20px -25px;
        }
        .bill-header h5 { margin: 0; }
        .bill-header p  { margin: 3px 0 0 0; font-size: 13px; color: #bdc3c7; }
        .section-title {
            background-color: #ecf0f1;
            padding: 6px 10px;
            font-size: 13px;
            font-weight: bold;
            color: #2c3e50;
            margin: 15px 0 10px 0;
            border-left: 4px solid #2c3e50;
        }
        .table { font-size: 14px; }
        .table th { background-color: #2c3e50; color: white; font-weight: normal; }
        .total-row td { font-weight: bold; font-size: 16px; color: #2c3e50; }
        .btn-primary  { background-color: #2c3e50; border-color: #2c3e50; border-radius: 0; }
        .btn-success  { background-color: #27ae60; border-color: #27ae60; border-radius: 0; }
        .btn-secondary { border-radius: 0; }
        .form-control { border-radius: 0; }
        .form-select  { border-radius: 0; }
        .alert { border-radius: 0; }
        .badge-paid   { background-color: #27ae60; color: white;
            padding: 4px 10px; font-size: 12px; }
        .badge-unpaid { background-color: #e74c3c; color: white;
            padding: 4px 10px; font-size: 12px; }
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
    <div class="alert alert-danger" style="max-width:700px">
        <%= error %>
    </div>
    <% } %>
    <% String success = (String) request.getAttribute("successMessage"); %>
    <% if (success != null) { %>
    <div class="alert alert-success" style="max-width:700px">
        <%= success %>
    </div>
    <% } %>

    <% if (bill != null) {
        Reservation res = bill.getReservation();
        Customer cust   = res.getCustomer();
        Room room       = res.getRoom();
    %>

    <div class="bill-card">


        <div class="bill-header">
            <h5>Ocean View Resort — Bill</h5>
            <p>Bill ID: <%= bill.getBillId() %> &nbsp;|&nbsp;
                Generated:
                <%= bill.getGeneratedAt() != null
                        ? DateUtil.formatDateTime(bill.getGeneratedAt()) : "—" %>
                &nbsp;|&nbsp;
                Status:
                <% if ("Paid".equals(bill.getPaymentStatus())) { %>
                <span class="badge-paid">PAID</span>
                <% } else { %>
                <span class="badge-unpaid">UNPAID</span>
                <% } %>
            </p>
        </div>


        <div class="section-title">Guest Information</div>
        <table class="table table-bordered" style="font-size:14px">
            <tr>
                <td width="150"><strong>Guest Name</strong></td>
                <td><%= cust.getFullName() %></td>
                <td width="150"><strong>NIC</strong></td>
                <td><%= cust.getNationalId() %></td>
            </tr>
            <tr>
                <td><strong>Phone</strong></td>
                <td><%= cust.getPhone() %></td>
                <td><strong>Email</strong></td>
                <td><%= cust.getEmail() != null ? cust.getEmail() : "—" %></td>
            </tr>
        </table>


        <div class="section-title">Reservation Details</div>
        <table class="table table-bordered" style="font-size:14px">
            <tr>
                <td width="150"><strong>Reservation ID</strong></td>
                <td><%= res.getReservationId() %></td>
                <td width="150"><strong>Room</strong></td>
                <td>Room <%= room.getRoomNumber() %>
                    (<%= room.getRoomType().getTypeName() %>)</td>
            </tr>
            <tr>
                <td><strong>Check-in</strong></td>
                <td><%= DateUtil.formatDate(res.getCheckInDate()) %></td>
                <td><strong>Check-out</strong></td>
                <td><%= DateUtil.formatDate(res.getCheckOutDate()) %></td>
            </tr>
            <tr>
                <td><strong>Guests</strong></td>
                <td><%= res.getNumGuests() %></td>
                <td><strong>Nights</strong></td>
                <td><%= bill.getNumNights() %></td>
            </tr>
        </table>


        <div class="section-title">Bill Breakdown</div>
        <table class="table table-bordered" style="font-size:14px">
            <tr>
                <td>Room Rate</td>
                <td>LKR <%= String.format("%.2f", bill.getRoomPrice()) %>
                    / night</td>
            </tr>
            <tr>
                <td>Number of Nights</td>
                <td><%= bill.getNumNights() %></td>
            </tr>
            <tr>
                <td>Subtotal</td>
                <td>LKR <%= String.format("%.2f", bill.getSubtotal()) %></td>
            </tr>
            <tr>
                <td>Tax</td>
                <td>LKR <%= String.format("%.2f", bill.getTaxAmount()) %></td>
            </tr>
            <tr class="total-row">
                <td>TOTAL</td>
                <td>LKR <%= String.format("%.2f", bill.getTotalAmount()) %></td>
            </tr>
        </table>


        <div class="section-title">Payments Made</div>
        <% if (payments == null || payments.isEmpty()) { %>
        <p class="text-muted" style="font-size:14px">
            No payments recorded yet.
        </p>
        <% } else { %>
        <table class="table table-bordered" style="font-size:14px">
            <thead>
            <tr>
                <th>Payment ID</th>
                <th>Amount</th>
                <th>Method</th>
                <th>Date</th>
                <th>Processed By</th>
            </tr>
            </thead>
            <tbody>
            <% for (Object obj : payments) {
                Payment p = (Payment) obj; %>
            <tr>
                <td><%= p.getPaymentId() %></td>
                <td>LKR <%= String.format("%.2f",
                        p.getAmountPaid()) %></td>
                <td><%= p.getPaymentMethod() %></td>
                <td><%= DateUtil.formatDateTime(
                        p.getPaymentDate()) %></td>
                <td><%= p.getProcessedBy() %></td>
            </tr>
            <% } %>
            </tbody>
        </table>
        <% } %>


        <% if ("Unpaid".equals(bill.getPaymentStatus())) { %>
        <div class="section-title">Process Payment</div>
        <form method="post"
              action="${pageContext.request.contextPath}/staff">
            <input type="hidden" name="action" value="processPayment">
            <input type="hidden" name="billId"
                   value="<%= bill.getBillId() %>">
            <div class="row g-2 align-items-end">
                <div class="col-md-4">
                    <label class="form-label" style="font-size:13px">
                        Amount (LKR)
                    </label>
                    <input type="number" class="form-control"
                           name="amountPaid"
                           value="<%= String.format("%.2f",
                                       bill.getTotalAmount()) %>"
                           step="0.01" min="1" required>
                </div>
                <div class="col-md-4">
                    <label class="form-label" style="font-size:13px">
                        Payment Method
                    </label>
                    <select class="form-select" name="paymentMethod">
                        <option value="Cash">Cash</option>
                        <option value="Card">Card</option>
                        <option value="Online">Online</option>
                    </select>
                </div>
                <div class="col-md-4">
                    <button type="submit" class="btn btn-success w-100">
                        Process Payment
                    </button>
                </div>
            </div>
        </form>
        <% } %>


        <div class="mt-4 d-flex gap-2">
            <% if ("Paid".equals(bill.getPaymentStatus())) { %>
            <a href="${pageContext.request.contextPath}/staff?action=printBill&billId=<%= bill.getBillId() %>"
               class="btn btn-primary">
                Print Receipt
            </a>
            <% } %>
            <a href="${pageContext.request.contextPath}/staff?action=manageReservations"
               class="btn btn-secondary">
                Back to Reservations
            </a>
        </div>

    </div>

    <% } else { %>
    <div class="alert alert-warning" style="max-width:700px">
        Bill not found.
        <a href="${pageContext.request.contextPath}/staff?action=manageReservations">
            Back to Reservations
        </a>
    </div>
    <% } %>

</div>
</body>
</html>