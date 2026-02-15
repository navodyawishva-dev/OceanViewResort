<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.oceanview.util.SessionManager" %>
<%@ page import="com.oceanview.model.Customer" %>
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
    <title>Manage Customers - Ocean View Resort</title>
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
        .btn-sm { font-size: 12px; }
        .alert { border-radius: 0; }
        .form-control { border-radius: 0; }
    </style>
</head>
<body>

<!-- Navbar -->
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

    <!-- Messages -->
    <% String error = (String) request.getAttribute("errorMessage"); %>
    <% if (error != null) { %>
    <div class="alert alert-danger"><%= error %></div>
    <% } %>
    <% String success = (String) request.getAttribute("successMessage"); %>
    <% if (success != null) { %>
    <div class="alert alert-success"><%= success %></div>
    <% } %>

    <!-- Header -->
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h5 style="color:#2c3e50">Manage Customers</h5>
        <a href="${pageContext.request.contextPath}/staff?action=addCustomer"
           class="btn btn-success">
            + Register Customer
        </a>
    </div>

    <!-- Customers Table -->
    <div class="section-title">All Customers</div>
    <%
        List customerList = (List) request.getAttribute("customerList");
    %>
    <% if (customerList == null || customerList.isEmpty()) { %>
    <p class="text-muted" style="font-size:14px">
        No customers registered yet.
    </p>
    <% } else { %>
    <table class="table table-striped table-bordered">
        <thead>
        <tr>
            <th>ID</th>
            <th>Full Name</th>
            <th>NIC</th>
            <th>Phone</th>
            <th>Email</th>
            <th>Address</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <% for (Object obj : customerList) {
            Customer c = (Customer) obj; %>
        <tr>
            <td><%= c.getId() %></td>
            <td><%= c.getFullName() %></td>
            <td><%= c.getNationalId() %></td>
            <td><%= c.getPhone() %></td>
            <td><%= c.getEmail() != null
                    ? c.getEmail() : "" %></td>
            <td><%= c.getAddress() != null
                    ? c.getAddress() : "" %></td>
            <td>
                <!-- Edit -->
                <a href="${pageContext.request.contextPath}/staff?action=editCustomer&customerId=<%= c.getId() %>"
                   class="btn btn-primary btn-sm">
                    Edit
                </a>
                <!-- View Reservations -->
                <a href="${pageContext.request.contextPath}/staff?action=searchReservations&keyword=<%= c.getFullName() %>"
                   class="btn btn-primary btn-sm">
                    Reservations
                </a>
                <!-- Delete -->
                <form method="post"
                      action="${pageContext.request.contextPath}/staff"
                      style="display:inline">
                    <input type="hidden" name="action"
                           value="deleteCustomer">
                    <input type="hidden" name="customerId"
                           value="<%= c.getId() %>">
                    <button type="submit"
                            class="btn btn-danger btn-sm"
                            onclick="return confirm('Delete customer <%= c.getFullName() %>?')">
                        Delete
                    </button>
                </form>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>
    <% } %>
</div>

</body>
</html>