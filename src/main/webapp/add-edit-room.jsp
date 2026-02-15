<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.oceanview.util.SessionManager" %>
<%@ page import="com.oceanview.model.Room" %>
<%@ page import="com.oceanview.model.RoomType" %>
<%@ page import="java.util.List" %>
<%
    if (!SessionManager.isLoggedIn(request)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    String formAction = (String) request.getAttribute("formAction");
    Room   room       = (Room) request.getAttribute("room");
    boolean isEdit    = "edit".equals(formAction);
    String username   = SessionManager.getLoggedInUsername(request);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= isEdit ? "Edit" : "Add" %> Room - Ocean View Resort</title>
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
        .form-control { border-radius: 0; }
        .form-select  { border-radius: 0; }
        .btn-primary  { background-color: #2c3e50; border-color: #2c3e50; border-radius: 0; }
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
               href="${pageContext.request.contextPath}/staff?action=manageRooms">
                Manage Rooms
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
        <h6><%= isEdit ? "Edit Room" : "Add New Room" %></h6>

        <form method="post"
              action="${pageContext.request.contextPath}/staff">
            <input type="hidden" name="action" value="saveRoom">
            <input type="hidden" name="roomId"
                   value="<%= isEdit && room != null
                              ? room.getRoomId() : "" %>">


            <div class="mb-3">
                <label class="form-label">Room Number *</label>
                <input type="text" class="form-control"
                       name="roomNumber"
                       value="<%= isEdit && room != null
                                  ? room.getRoomNumber() : "" %>"
                       placeholder="e.g. 101" required>
            </div>


            <div class="mb-3">
                <label class="form-label">Room Type *</label>
                <select class="form-select" name="typeId" required>
                    <option value="">-- Select Type --</option>
                    <%
                        List roomTypeList =
                                (List) request.getAttribute("roomTypeList");
                        if (roomTypeList != null) {
                            for (Object obj : roomTypeList) {
                                RoomType rt = (RoomType) obj;
                                boolean selected = isEdit && room != null &&
                                        room.getRoomType().getTypeId()
                                                .equals(rt.getTypeId());
                    %>
                    <option value="<%= rt.getTypeId() %>"
                            <%= selected ? "selected" : "" %>>
                        <%= rt.getTypeName() %> -
                        LKR <%= String.format("%.0f",
                            rt.getPricePerNight()) %>/night
                    </option>
                    <%      }
                    }
                    %>
                </select>
            </div>


            <div class="mb-3">
                <label class="form-label">Floor *</label>
                <input type="number" class="form-control"
                       name="floor" min="0" max="50"
                       value="<%= isEdit && room != null
                                  ? room.getFloor() : 1 %>"
                       required>
            </div>


            <% if (isEdit) { %>
            <div class="mb-3">
                <label class="form-label">Status</label>
                <select class="form-select" name="status">
                    <%
                        String currentStatus = room != null
                                ? room.getStatus() : "Available";
                    %>
                    <option value="Available"
                            <%= "Available".equals(currentStatus)
                                    ? "selected" : "" %>>Available</option>
                    <option value="Occupied"
                            <%= "Occupied".equals(currentStatus)
                                    ? "selected" : "" %>>Occupied</option>
                    <option value="Reserved"
                            <%= "Reserved".equals(currentStatus)
                                    ? "selected" : "" %>>Reserved</option>
                    <option value="Maintenance"
                            <%= "Maintenance".equals(currentStatus)
                                    ? "selected" : "" %>>Maintenance</option>
                </select>
            </div>
            <% } else { %>
            <input type="hidden" name="status" value="Available">
            <% } %>


            <div class="mb-3">
                <label class="form-label">Description</label>
                <textarea class="form-control" name="description"
                          rows="2"><%= isEdit && room != null
                        && room.getDescription() != null
                        ? room.getDescription() : "" %></textarea>
            </div>


            <div class="d-flex gap-2">
                <button type="submit" class="btn btn-primary">
                    <%= isEdit ? "Update Room" : "Add Room" %>
                </button>
                <a href="${pageContext.request.contextPath}/staff?action=manageRooms"
                   class="btn btn-secondary">
                    Cancel
                </a>
            </div>
        </form>
    </div>
</div>

</body>
</html>