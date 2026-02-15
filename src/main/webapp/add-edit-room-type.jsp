<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.oceanview.util.SessionManager" %>
<%@ page import="com.oceanview.model.RoomType" %>
<%
    if (!SessionManager.isLoggedIn(request)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    String formAction = (String) request.getAttribute("formAction");
    RoomType roomType = (RoomType) request.getAttribute("roomType");
    boolean isEdit    = "edit".equals(formAction);
    String username   = SessionManager.getLoggedInUsername(request);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= isEdit ? "Edit" : "Add" %> Room Type - Ocean View Resort</title>
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
            max-width: 500px;
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
               href="${pageContext.request.contextPath}/staff?action=manageRoomTypes">
                Room Types
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
    <div class="alert alert-danger" style="max-width:500px">
        <%= error %>
    </div>
    <% } %>

    <div class="form-card">
        <h6><%= isEdit ? "Edit Room Type" : "Add New Room Type" %></h6>

        <form method="post"
              action="${pageContext.request.contextPath}/staff">
            <input type="hidden" name="action" value="saveRoomType">
            <input type="hidden" name="typeId"
                   value="<%= isEdit && roomType != null
                              ? roomType.getTypeId() : "" %>">


            <div class="mb-3">
                <label class="form-label">Type Name *</label>
                <input type="text" class="form-control"
                       name="typeName"
                       value="<%= isEdit && roomType != null
                                  ? roomType.getTypeName() : "" %>"
                       placeholder="e.g. Deluxe" required>
            </div>


            <div class="mb-3">
                <label class="form-label">Price Per Night (LKR) *</label>
                <input type="number" class="form-control"
                       name="pricePerNight"
                       value="<%= isEdit && roomType != null
                                  ? roomType.getPricePerNight() : "" %>"
                       min="1" step="0.01"
                       placeholder="e.g. 8000" required>
            </div>


            <div class="mb-3">
                <label class="form-label">Max Occupancy *</label>
                <input type="number" class="form-control"
                       name="maxOccupancy"
                       value="<%= isEdit && roomType != null
                                  ? roomType.getMaxOccupancy() : 2 %>"
                       min="1" max="20" required>
            </div>


            <div class="mb-3">
                <label class="form-label">Description</label>
                <textarea class="form-control" name="description"
                          rows="2"
                          placeholder="Optional description..."><%= isEdit && roomType != null
                        && roomType.getDescription() != null
                        ? roomType.getDescription() : "" %></textarea>
            </div>


            <div class="d-flex gap-2">
                <button type="submit" class="btn btn-primary">
                    <%= isEdit ? "Update Room Type" : "Add Room Type" %>
                </button>
                <a href="${pageContext.request.contextPath}/staff?action=manageRoomTypes"
                   class="btn btn-secondary">
                    Cancel
                </a>
            </div>
        </form>
    </div>
</div>

</body>
</html>