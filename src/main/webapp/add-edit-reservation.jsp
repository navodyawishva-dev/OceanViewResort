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
    String formAction   = (String) request.getAttribute("formAction");
    Reservation reservation = (Reservation) request.getAttribute("reservation");
    boolean isEdit      = "edit".equals(formAction);
    String username     = SessionManager.getLoggedInUsername(request);
    String role         = SessionManager.getLoggedInRole(request);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= isEdit ? "Edit" : "New" %> Reservation - Ocean View Resort</title>
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
            max-width: 700px;
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
        .price-display {
            background-color: #ecf0f1;
            padding: 10px 15px;
            border: 1px solid #dee2e6;
            font-size: 14px;
        }
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
    <div class="alert alert-danger" style="max-width:700px">
        <%= error %>
    </div>
    <% } %>

    <div class="form-card">
        <h6><%= isEdit ? "Edit Reservation" : "New Reservation" %></h6>

        <form method="post"
              action="${pageContext.request.contextPath}/staff">
            <input type="hidden" name="action" value="saveReservation">
            <input type="hidden" name="reservationId"
                   value="<%= isEdit && reservation != null
                              ? reservation.getReservationId() : "" %>">


            <div class="mb-3">
                <label class="form-label">Customer *</label>
                <select class="form-select" name="customerId" required>
                    <option value="">-- Select Customer --</option>
                    <%
                        List customerList =
                                (List) request.getAttribute("customerList");
                        if (customerList != null) {
                            for (Object obj : customerList) {
                                Customer c = (Customer) obj;
                                boolean selected = isEdit && reservation != null &&
                                        reservation.getCustomer().getId()
                                                .equals(c.getId());
                    %>
                    <option value="<%= c.getId() %>"
                            <%= selected ? "selected" : "" %>>
                        <%= c.getFullName() %> -
                        <%= c.getPhone() %>
                    </option>
                    <%      }
                    }
                    %>
                </select>
                <small class="text-muted">
                    Customer not listed?
                    <a href="${pageContext.request.contextPath}/staff?action=addCustomer"
                       target="_blank">Register new customer</a>
                </small>
            </div>


            <div class="mb-3">
                <label class="form-label">Room *</label>
                <select class="form-select" name="roomId" required
                        onchange="updateRoomPrice(this)">
                    <option value="">-- Select Room --</option>
                    <%
                        List roomList = (List) request.getAttribute("roomList");
                        if (roomList != null) {
                            for (Object obj : roomList) {
                                Room r = (Room) obj;
                                boolean selected = isEdit && reservation != null &&
                                        reservation.getRoom().getRoomId()
                                                .equals(r.getRoomId());
                    %>
                    <option value="<%= r.getRoomId() %>"
                            data-price="<%= r.getPricePerNight() %>"
                            <%= selected ? "selected" : "" %>>
                        Room <%= r.getRoomNumber() %> -
                        <%= r.getRoomType().getTypeName() %> -
                        LKR <%= String.format("%.0f",
                            r.getPricePerNight()) %>/night
                    </option>
                    <%      }
                    }
                    %>
                </select>
            </div>


            <div class="price-display mb-3" id="priceDisplay"
                 style="display:none">
                Room Price: <strong id="priceText">LKR 0</strong> per night
            </div>


            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">Check-in Date *</label>
                    <input type="date" class="form-control"
                           name="checkInDate"
                           value="<%= isEdit && reservation != null
                                      ? DateUtil.formatDateForInput(
                                            reservation.getCheckInDate()) : "" %>"
                           onchange="calculateNights()"
                           required>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label">Check-out Date *</label>
                    <input type="date" class="form-control"
                           name="checkOutDate"
                           value="<%= isEdit && reservation != null
                                      ? DateUtil.formatDateForInput(
                                            reservation.getCheckOutDate()) : "" %>"
                           onchange="calculateNights()"
                           required>
                </div>
            </div>


            <div class="price-display mb-3" id="totalDisplay"
                 style="display:none">
                Stay: <strong id="nightsText">0 nights</strong> |
                Estimated Total:
                <strong id="totalText">LKR 0</strong>
            </div>


            <div class="mb-3">
                <label class="form-label">Number of Guests *</label>
                <input type="number" class="form-control"
                       name="numGuests" min="1" max="10"
                       value="<%= isEdit && reservation != null
                                  ? reservation.getNumGuests() : 1 %>"
                       required>
            </div>


            <div class="mb-3">
                <label class="form-label">Special Requests</label>
                <textarea class="form-control" name="specialRequests"
                          rows="3"
                          placeholder="Any special requests..."><%= isEdit && reservation != null
                        && reservation.getSpecialRequests() != null
                        ? reservation.getSpecialRequests() : "" %></textarea>
            </div>


            <div class="d-flex gap-2">
                <button type="submit" class="btn btn-primary">
                    <%= isEdit ? "Update Reservation" : "Create Reservation" %>
                </button>
                <a href="${pageContext.request.contextPath}/staff?action=manageReservations"
                   class="btn btn-secondary">
                    Cancel
                </a>
            </div>
        </form>
    </div>
</div>

<script>

    function updateRoomPrice(select) {
        var price = select.options[select.selectedIndex]
            .getAttribute('data-price');
        if (price) {
            document.getElementById('priceDisplay').style.display = 'block';
            document.getElementById('priceText').textContent =
                'LKR ' + parseFloat(price).toLocaleString();
            calculateNights();
        }
    }


    function calculateNights() {
        var checkIn  = document.querySelector('[name="checkInDate"]').value;
        var checkOut = document.querySelector('[name="checkOutDate"]').value;
        var roomSelect = document.querySelector('[name="roomId"]');
        var price = roomSelect.options[roomSelect.selectedIndex]
            .getAttribute('data-price');

        if (checkIn && checkOut && price) {
            var d1     = new Date(checkIn);
            var d2     = new Date(checkOut);
            var nights = Math.round((d2 - d1) / (1000 * 60 * 60 * 24));
            if (nights > 0) {
                var total = nights * parseFloat(price);
                document.getElementById('totalDisplay').style.display = 'block';
                document.getElementById('nightsText').textContent =
                    nights + ' night' + (nights > 1 ? 's' : '');
                document.getElementById('totalText').textContent =
                    'LKR ' + total.toLocaleString();
            }
        }
    }
</script>

</body>
</html>