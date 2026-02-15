<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.oceanview.util.SessionManager" %>
<%@ page import="com.oceanview.model.Staff" %>
<%@ page import="java.util.List" %>
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
    <title>Manage Staff - Ocean View Resort</title>
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
        .btn-primary { background-color: #2c3e50; border-color: #2c3e50; border-radius: 0; }
        .btn-success { background-color: #27ae60; border-color: #27ae60; border-radius: 0; }
        .btn-danger  { background-color: #e74c3c; border-color: #e74c3c; border-radius: 0; }
        .btn-warning { background-color: #f39c12; border-color: #f39c12;
            border-radius: 0; color: white; }
        .btn-sm { font-size: 12px; }
        .alert { border-radius: 0; }
        .badge-active   { background-color: #27ae60; color: white;
            padding: 3px 8px; font-size: 11px; }
        .badge-pending  { background-color: #f39c12; color: white;
            padding: 3px 8px; font-size: 11px; }
        .badge-inactive { background-color: #e74c3c; color: white;
            padding: 3px 8px; font-size: 11px; }
    </style>
</head>
<body>

<!-- Navbar -->
<nav class="navbar navbar-expand-lg">
    <div class="container-fluid">
        <span class="navbar-brand">Ocean View Resort</span>
        <div class="navbar-nav ms-auto">
            <a class="nav-link"
               href="${pageContext.request.contextPath}/dashboard?role=admin">
                Dashboard
            </a>
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

    <!-- Header -->
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h5 style="color:#2c3e50">Manage Staff</h5>
        <a href="${pageContext.request.contextPath}/admin?action=addStaff"
           class="btn btn-primary">
            + Add Staff
        </a>
    </div>

    <!-- Staff Table -->
    <div class="section-title">All Staff Members</div>
    <%
        List staffList = (List) request.getAttribute("staffList");
    %>
    <% if (staffList == null || staffList.isEmpty()) { %>
    <p class="text-muted" style="font-size:14px">No staff members found.</p>
    <% } else { %>
    <table class="table table-striped table-bordered">
        <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Username</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Role</th>
            <th>Status</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <% for (Object obj : staffList) {
            Staff s = (Staff) obj; %>
        <tr>
            <td><%= s.getId() %></td>
            <td><%= s.getFullName() %></td>
            <td><%= s.getUsername() %></td>
            <td><%= s.getEmail() %></td>
            <td><%= s.getPhone() %></td>
            <td><%= s.getRole() %></td>
            <td>
                <% if ("Active".equals(s.getStatus())) { %>
                <span class="badge-active">Active</span>
                <% } else if ("Pending".equals(s.getStatus())) { %>
                <span class="badge-pending">Pending</span>
                <% } else { %>
                <span class="badge-inactive">Inactive</span>
                <% } %>
            </td>
            <td>
                <!-- Edit -->
                <a href="${pageContext.request.contextPath}/admin?action=editStaff&staffId=<%= s.getId() %>"
                   class="btn btn-primary btn-sm">Edit</a>

                <!-- Approve (only for Pending) -->
                <% if ("Pending".equals(s.getStatus())) { %>
                <form method="post"
                      action="${pageContext.request.contextPath}/admin"
                      style="display:inline">
                    <input type="hidden" name="action" value="approveStaff">
                    <input type="hidden" name="staffId" value="<%= s.getId() %>">
                    <button type="submit"
                            class="btn btn-success btn-sm">
                        Approve
                    </button>
                </form>
                <% } %>

                <!-- Deactivate (only for Active) -->
                <% if ("Active".equals(s.getStatus())) { %>
                <form method="post"
                      action="${pageContext.request.contextPath}/admin"
                      style="display:inline">
                    <input type="hidden" name="action" value="deactivateStaff">
                    <input type="hidden" name="staffId" value="<%= s.getId() %>">
                    <button type="submit"
                            class="btn btn-warning btn-sm"
                            onclick="return confirm('Deactivate this staff account?')">
                        Deactivate
                    </button>
                </form>
                <% } %>

                <!-- Reactivate (only for Inactive) -->
                <% if ("Inactive".equals(s.getStatus())) { %>
                <form method="post"
                      action="${pageContext.request.contextPath}/admin"
                      style="display:inline">
                    <input type="hidden" name="action" value="reactivateStaff">
                    <input type="hidden" name="staffId" value="<%= s.getId() %>">
                    <button type="submit"
                            class="btn btn-success btn-sm">
                        Reactivate
                    </button>
                </form>
                <% } %>

                <!-- Delete -->
                <form method="post"
                      action="${pageContext.request.contextPath}/admin"
                      style="display:inline">
                    <input type="hidden" name="action" value="deleteStaff">
                    <input type="hidden" name="staffId" value="<%= s.getId() %>">
                    <button type="submit"
                            class="btn btn-danger btn-sm"
                            onclick="return confirm('Permanently delete this staff member?')">
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