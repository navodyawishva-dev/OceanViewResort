<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.oceanview.util.SessionManager" %>
<%@ page import="com.oceanview.model.Staff" %>
<%
    if (!SessionManager.isLoggedIn(request) || !SessionManager.isAdmin(request)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
       return;
     }
    String formAction = (String) request.getAttribute("formAction");
    Staff staff = (Staff) request.getAttribute("staff");
    boolean isEdit = "edit".equals(formAction);
   String username = SessionManager.getLoggedInUsername(request);

%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= isEdit ? "Edit" : "Add" %> Staff - Ocean View Resort</title>
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
            max-width: 600px;
        }
        .form-card h6 {
            background-color: #2c3e50;
            color: white;
            padding: 8px 15px;
            margin: -25px -25px 20px -25px;
            font-size: 14px;
        }
        .form-control { border-radius: 0; }
        .btn-primary { background-color: #2c3e50; border-color: #2c3e50; border-radius: 0; }
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
               href="${pageContext.request.contextPath}/admin?action=manageStaff">
                Manage Staff
            </a>
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


    <% String error = (String) request.getAttribute("errorMessage"); %>
    <% if (error != null) { %>
    <div class="alert alert-danger" style="max-width:600px">
        <%= error %>
    </div>
    <% } %>

    <div class="form-card">
        <h6><%= isEdit ? "Edit Staff Member" : "Add New Staff Member" %></h6>

        <form method="post"
              action="${pageContext.request.contextPath}/admin">
            <input type="hidden" name="action" value="saveStaff">
            <input type="hidden" name="staffId"
                   value="<%= isEdit && staff != null ? staff.getId() : "" %>">


            <div class="mb-3">
                <label class="form-label">Full Name *</label>
                <input type="text" class="form-control" name="fullName"
                       value="<%= isEdit && staff != null
                                  ? staff.getFullName()
                                  : (request.getAttribute("fullName") != null
                                     ? request.getAttribute("fullName") : "") %>"
                       required>
            </div>


            <% if (!isEdit) { %>
            <div class="mb-3">
                <label class="form-label">Username *</label>
                <input type="text" class="form-control" name="username"
                       value="<%= request.getAttribute("username") != null
                                  ? request.getAttribute("username") : "" %>"
                       required>
                <small class="text-muted">
                    4-20 characters, letters/numbers/underscore only
                </small>
            </div>
            <div class="mb-3">
                <label class="form-label">Password *</label>
                <input type="password" class="form-control"
                       name="password" required>
                <small class="text-muted">Minimum 6 characters</small>
            </div>
            <% } else { %>

            <div class="mb-3">
                <label class="form-label">Username</label>
                <input type="text" class="form-control"
                       value="<%= staff != null ? staff.getUsername() : "" %>"
                       disabled>
                <input type="hidden" name="username"
                       value="<%= staff != null ? staff.getUsername() : "" %>">
            </div>
            <% } %>


            <div class="mb-3">
                <label class="form-label">Email *</label>
                <input type="email" class="form-control" name="email"
                       value="<%= isEdit && staff != null
                                  ? staff.getEmail()
                                  : (request.getAttribute("email") != null
                                     ? request.getAttribute("email") : "") %>"
                       required>
            </div>


            <div class="mb-3">
                <label class="form-label">Phone *</label>
                <input type="text" class="form-control" name="phone"
                       value="<%= isEdit && staff != null
                                  ? staff.getPhone()
                                  : (request.getAttribute("phone") != null
                                     ? request.getAttribute("phone") : "") %>"
                       placeholder="0771234567" required>
            </div>


            <div class="mb-3">
                <label class="form-label">Role</label>
                <select class="form-control" name="role">
                    <%
                        String currentRole = isEdit && staff != null
                                ? staff.getRole() : "Receptionist";
                    %>
                    <option value="Receptionist"
                            <%= "Receptionist".equals(currentRole)
                                    ? "selected" : "" %>>
                        Receptionist
                    </option>
                    <option value="Manager"
                            <%= "Manager".equals(currentRole)
                                    ? "selected" : "" %>>
                        Manager
                    </option>
                    <option value="Housekeeping"
                            <%= "Housekeeping".equals(currentRole)
                                    ? "selected" : "" %>>
                        Housekeeping
                    </option>
                </select>
            </div>


            <div class="d-flex gap-2">
                <button type="submit" class="btn btn-primary">
                    <%= isEdit ? "Update Staff" : "Add Staff" %>
                </button>
                <a href="${pageContext.request.contextPath}/admin?action=manageStaff"
                   class="btn btn-secondary">
                    Cancel
                </a>
            </div>
        </form>
    </div>
</div>

</body>
</html>