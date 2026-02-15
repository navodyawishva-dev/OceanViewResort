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
  Bill bill     = (Bill) request.getAttribute("bill");
  List payments = (List) request.getAttribute("payments");
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Receipt - Ocean View Resort</title>
  <link rel="stylesheet"
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css">
  <style>
    body {
      font-family: Arial, Helvetica, sans-serif;
      background-color: white;
      padding: 30px;
    }
    .receipt {
      max-width: 600px;
      margin: 0 auto;
      border: 2px solid #2c3e50;
      padding: 30px;
    }
    .receipt-header {
      text-align: center;
      border-bottom: 2px solid #2c3e50;
      padding-bottom: 15px;
      margin-bottom: 20px;
    }
    .receipt-header h4 {
      color: #2c3e50;
      margin: 0;
      font-size: 22px;
    }
    .receipt-header p {
      color: #7f8c8d;
      margin: 3px 0 0 0;
      font-size: 13px;
    }
    .receipt-title {
      text-align: center;
      font-size: 16px;
      font-weight: bold;
      color: #2c3e50;
      margin: 15px 0;
      text-transform: uppercase;
      letter-spacing: 2px;
    }
    .info-row {
      display: flex;
      justify-content: space-between;
      font-size: 13px;
      margin-bottom: 5px;
    }
    .info-row span:first-child { color: #7f8c8d; }
    .divider {
      border-top: 1px dashed #bdc3c7;
      margin: 15px 0;
    }
    .total-line {
      display: flex;
      justify-content: space-between;
      font-size: 18px;
      font-weight: bold;
      color: #2c3e50;
      margin-top: 10px;
    }
    .paid-stamp {
      text-align: center;
      color: #27ae60;
      font-size: 28px;
      font-weight: bold;
      border: 3px solid #27ae60;
      padding: 5px 20px;
      display: inline-block;
      margin: 15px auto;
      transform: rotate(-5deg);
    }
    .stamp-wrapper { text-align: center; margin: 15px 0; }
    .receipt-footer {
      text-align: center;
      font-size: 12px;
      color: #7f8c8d;
      border-top: 1px solid #dee2e6;
      padding-top: 15px;
      margin-top: 20px;
    }
    .no-print { margin-top: 20px; text-align: center; }
    .btn-primary   { background-color: #2c3e50; border-color: #2c3e50;
      border-radius: 0; }
    .btn-secondary { border-radius: 0; }
    @media print {
      .no-print { display: none; }
      body { padding: 0; }
    }
  </style>
</head>
<body>

<% if (bill != null) {
  Reservation res = bill.getReservation();
  Customer cust   = res.getCustomer();
  Room room       = res.getRoom();
%>

<div class="receipt">


  <div class="receipt-header">
    <h4>Ocean View Resort</h4>
    <p>Galle, Sri Lanka &nbsp;|&nbsp; Tel: +94 91 234 5678</p>
  </div>

  <div class="receipt-title">Official Receipt</div>


  <div class="info-row">
    <span>Bill ID</span>
    <span><%= bill.getBillId() %></span>
  </div>
  <div class="info-row">
    <span>Reservation ID</span>
    <span><%= res.getReservationId() %></span>
  </div>
  <div class="info-row">
    <span>Date Issued</span>
    <span><%= bill.getGeneratedAt() != null
            ? DateUtil.formatDateTime(bill.getGeneratedAt()) : "—" %></span>
  </div>

  <div class="divider"></div>


  <div class="info-row">
    <span>Guest Name</span>
    <span><strong><%= cust.getFullName() %></strong></span>
  </div>
  <div class="info-row">
    <span>NIC</span>
    <span><%= cust.getNationalId() %></span>
  </div>
  <div class="info-row">
    <span>Phone</span>
    <span><%= cust.getPhone() %></span>
  </div>

  <div class="divider"></div>


  <div class="info-row">
    <span>Room</span>
    <span>Room <%= room.getRoomNumber() %>
              (<%= room.getRoomType().getTypeName() %>)</span>
  </div>
  <div class="info-row">
    <span>Check-in</span>
    <span><%= DateUtil.formatDate(res.getCheckInDate()) %></span>
  </div>
  <div class="info-row">
    <span>Check-out</span>
    <span><%= DateUtil.formatDate(res.getCheckOutDate()) %></span>
  </div>
  <div class="info-row">
    <span>Duration</span>
    <span><%= bill.getNumNights() %> night(s)</span>
  </div>

  <div class="divider"></div>


  <div class="info-row">
    <span>Room Rate</span>
    <span>LKR <%= String.format("%.2f",
            bill.getRoomPrice()) %> / night</span>
  </div>
  <div class="info-row">
        <span>Subtotal
              (<%= bill.getNumNights() %> nights)</span>
    <span>LKR <%= String.format("%.2f",
            bill.getSubtotal()) %></span>
  </div>
  <div class="info-row">
    <span>Tax</span>
    <span>LKR <%= String.format("%.2f",
            bill.getTaxAmount()) %></span>
  </div>

  <div class="divider"></div>


  <div class="total-line">
    <span>TOTAL</span>
    <span>LKR <%= String.format("%.2f",
            bill.getTotalAmount()) %></span>
  </div>


  <% if (payments != null && !payments.isEmpty()) { %>
  <div class="divider"></div>
  <div style="font-size:13px; color:#7f8c8d; margin-bottom:5px">
    Payments:
  </div>
  <% for (Object obj : payments) {
    Payment p = (Payment) obj; %>
  <div class="info-row">
    <span><%= p.getPaymentMethod() %></span>
    <span>LKR <%= String.format("%.2f",
            p.getAmountPaid()) %></span>
  </div>
  <% } %>
  <% } %>


  <% if ("Paid".equals(bill.getPaymentStatus())) { %>
  <div class="stamp-wrapper">
    <div class="paid-stamp">PAID</div>
  </div>
  <% } %>


  <div class="receipt-footer">
    <p>Thank you for staying at Ocean View Resort!</p>
    <p>We hope to welcome you again soon.</p>
  </div>

</div>


<div class="no-print">
  <button onclick="window.print()"
          class="btn btn-primary me-2">
    Print Receipt
  </button>
  <a href="${pageContext.request.contextPath}/staff?action=manageReservations"
     class="btn btn-secondary">
    Back to Reservations
  </a>
</div>

<% } else { %>
<div class="alert alert-warning">
  Bill not found.
  <a href="${pageContext.request.contextPath}/staff?action=manageReservations">
    Back to Reservations
  </a>
</div>
<% } %>

</body>
</html>