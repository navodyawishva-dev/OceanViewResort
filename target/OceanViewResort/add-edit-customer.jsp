<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.oceanview.util.SessionManager" %>
<%@ page import="com.oceanview.model.Customer" %>
<%
    if (!SessionManager.isLoggedIn(request)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    String formAction = (String) request.getAttribute("formAction");
    Customer customer = (Customer) request.getAttribute("customer");
    boolean isEdit    = "edit".equals(formAction);
    String username   = SessionManager.getLoggedInUsername(request);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= isEdit ? "Edit" : "Register" %> Customer - Ocean View Resort</title>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css">
    <style>
        body { font-family: Arial, Helvetica, sans-serif; background-color: #ecf0f1; }
        .navbar { background-color: #2c3e50; }
        .navbar-brand, .nav-link { color: white !important; }
        .content { padding: 20px; }
        .form-card {
            background: white;
            border: 1px solid #dee2e6;
            padding: 25px;
            max-width: 550px;
        }
        .form-card h6 {
            background-color: #2c3e50;
            color: white;
            padding: 8px 15px;
            margin: -25px -25px 20px -25px;
            font-size: 14px;
        }
        .form-control  { border-radius: 0; }
        .btn-primary   { background-color: #2c3e50; border-color: #2c3e50; border-radius: 0; }
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
               href="${pageContext.request.contextPath}/staff?action=manageCustomers">
                Customers
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
    <div class="alert alert-danger" style="max-width:550px">
        <%= error %>
    </div>
    <% } %>

    <div class="form-card">
        <h6><%= isEdit ? "Edit Customer" : "Register New Customer" %></h6>

        <form method="post"
              action="${pageContext.request.contextPath}/staff">
            <input type="hidden" name="action" value="saveCustomer">
            <input type="hidden" name="customerId"
                   value="<%= isEdit && customer != null
                              ? customer.getId() : "" %>">


            <div class="mb-3">
                <label class="form-label">Full Name *</label>
                <input type="text" class="form-control"
                       name="fullName"
                       value="<%= isEdit && customer != null
                                  ? customer.getFullName() : "" %>"
                       required>
            </div>


            <div class="mb-3">
                <label class="form-label">National ID (NIC) *</label>
                <% if (isEdit) { %>
                <input type="text" class="form-control"
                       value="<%= customer != null
                                  ? customer.getNationalId() : "" %>"
                       disabled>
                <input type="hidden" name="nationalId"
                       value="<%= customer != null
                                  ? customer.getNationalId() : "" %>">
                <% } else { %>
                <input type="text" class="form-control"
                       name="nationalId"
                       placeholder="e.g. 123456789V or 200012345678"
                       required>
                <% } %>
            </div>


            <div class="mb-3">
                <label class="form-label">Phone *</label>
                <input type="text" class="form-control"
                       name="phone"
                       value="<%= isEdit && customer != null
                                  ? customer.getPhone() : "" %>"
                       placeholder="0771234567" required>
            </div>


            <div class="mb-3">
                <label class="form-label">Email</label>
                <input type="email" class="form-control"
                       name="email"
                       value="<%= isEdit && customer != null
                                  && customer.getEmail() != null
                                  ? customer.getEmail() : "" %>">
            </div>


            <div class="mb-3">
                <label class="form-label">Address</label>
                <textarea class="form-control" name="address"
                          rows="2"><%= isEdit && customer != null
                        && customer.getAddress() != null
                        ? customer.getAddress() : "" %></textarea>
            </div>


            <div class="d-flex gap-2">
                <button type="submit" class="btn btn-primary">
                    <%= isEdit ? "Update Customer" : "Register Customer" %>
                </button>
                <a href="${pageContext.request.contextPath}/staff?action=manageCustomers"
                   class="btn btn-secondary">
                    Cancel
                </a>
            </div>
        </form>
    </div>
</div>

</body>
</html>