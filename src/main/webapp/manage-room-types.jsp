<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.oceanview.util.SessionManager" %>
<%@ page import="com.oceanview.model.RoomType" %>
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
    <title>Room Types - Ocean View Resort</title>
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
    </style>
</head>
<body>


<nav class="navbar navbar-expand-lg">
    <div class="container-fluid">
        <span class="navbar-brand">Ocean View Resort</span>
        <div class="navbar-nav ms-auto">
            <a class="nav-link"
               href="${pageContext.request.contextPath}/staff?action=manageRooms">
                Rooms
            </a>
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
        <h5 style="color:#2c3e50">Room Types</h5>
        <a href="${pageContext.request.contextPath}/staff?action=addRoomType"
           class="btn btn-success">
            + Add Room Type
        </a>
    </div>


    <div class="section-title">All Room Types</div>
    <%
        List roomTypeList = (List) request.getAttribute("roomTypeList");
    %>
    <% if (roomTypeList == null || roomTypeList.isEmpty()) { %>
    <p class="text-muted" style="font-size:14px">No room types found.</p>
    <% } else { %>
    <table class="table table-striped table-bordered">
        <thead>
        <tr>
            <th>Type ID</th>
            <th>Type Name</th>
            <th>Price / Night</th>
            <th>Max Occupancy</th>
            <th>Description</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <% for (Object obj : roomTypeList) {
            RoomType rt = (RoomType) obj; %>
        <tr>
            <td><%= rt.getTypeId() %></td>
            <td><%= rt.getTypeName() %></td>
            <td>LKR <%= String.format("%.2f",
                    rt.getPricePerNight()) %></td>
            <td><%= rt.getMaxOccupancy() %> guests</td>
            <td><%= rt.getDescription() != null
                    ? rt.getDescription() : "" %></td>
            <td>

                <a href="${pageContext.request.contextPath}/staff?action=editRoomType&typeId=<%= rt.getTypeId() %>"
                   class="btn btn-primary btn-sm">
                    Edit
                </a>

                <form method="post"
                      action="${pageContext.request.contextPath}/staff"
                      style="display:inline">
                    <input type="hidden" name="action"
                           value="deleteRoomType">
                    <input type="hidden" name="typeId"
                           value="<%= rt.getTypeId() %>">
                    <button type="submit"
                            class="btn btn-danger btn-sm"
                            onclick="return confirm('Delete <%= rt.getTypeName() %> type? This will fail if rooms are using it.')">
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