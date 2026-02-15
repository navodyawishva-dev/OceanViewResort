<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.oceanview.util.SessionManager" %>
<%@ page import="com.oceanview.model.Room" %>
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
    <title>Manage Rooms - Ocean View Resort</title>
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
        .badge-available    { background-color: #27ae60; color: white;
            padding: 3px 8px; font-size: 11px; }
        .badge-occupied     { background-color: #e74c3c; color: white;
            padding: 3px 8px; font-size: 11px; }
        .badge-reserved     { background-color: #2980b9; color: white;
            padding: 3px 8px; font-size: 11px; }
        .badge-maintenance  { background-color: #f39c12; color: white;
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
    <div class="alert alert-danger"><%= error %></div>
    <% } %>
    <% String success = (String) request.getAttribute("successMessage"); %>
    <% if (success != null) { %>
    <div class="alert alert-success"><%= success %></div>
    <% } %>


    <div class="d-flex justify-content-between align-items-center mb-3">
        <h5 style="color:#2c3e50">Manage Rooms</h5>
        <div>
            <a href="${pageContext.request.contextPath}/staff?action=manageRoomTypes"
               class="btn btn-primary me-2">
                Room Types
            </a>
            <a href="${pageContext.request.contextPath}/staff?action=addRoom"
               class="btn btn-success">
                + Add Room
            </a>
        </div>
    </div>


    <div class="section-title">All Rooms</div>
    <%
        List roomList = (List) request.getAttribute("roomList");
    %>
    <% if (roomList == null || roomList.isEmpty()) { %>
    <p class="text-muted" style="font-size:14px">No rooms found.</p>
    <% } else { %>
    <table class="table table-striped table-bordered">
        <thead>
        <tr>
            <th>Room ID</th>
            <th>Room No.</th>
            <th>Type</th>
            <th>Floor</th>
            <th>Price/Night</th>
            <th>Status</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <% for (Object obj : roomList) {
            Room r = (Room) obj; %>
        <tr>
            <td><%= r.getRoomId() %></td>
            <td><%= r.getRoomNumber() %></td>
            <td><%= r.getRoomType().getTypeName() %></td>
            <td><%= r.getFloor() %></td>
            <td>LKR <%= String.format("%.0f",
                    r.getPricePerNight()) %></td>
            <td>
                <% if ("Available".equals(r.getStatus())) { %>
                <span class="badge-available">Available</span>
                <% } else if ("Occupied".equals(r.getStatus())) { %>
                <span class="badge-occupied">Occupied</span>
                <% } else if ("Reserved".equals(r.getStatus())) { %>
                <span class="badge-reserved">Reserved</span>
                <% } else { %>
                <span class="badge-maintenance">Maintenance</span>
                <% } %>
            </td>
            <td>

                <a href="${pageContext.request.contextPath}/staff?action=editRoom&roomId=<%= r.getRoomId() %>"
                   class="btn btn-primary btn-sm">
                    Edit
                </a>

                <form method="post"
                      action="${pageContext.request.contextPath}/staff"
                      style="display:inline">
                    <input type="hidden" name="action" value="deleteRoom">
                    <input type="hidden" name="roomId"
                           value="<%= r.getRoomId() %>">
                    <button type="submit"
                            class="btn btn-danger btn-sm"
                            onclick="return confirm('Delete Room <%= r.getRoomNumber() %>?')">
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