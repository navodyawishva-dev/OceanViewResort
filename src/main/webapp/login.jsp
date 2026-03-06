<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ocean View Resort - Login</title>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css">
    <style>
        body {
            background-color: #ecf0f1;
            font-family: Arial, Helvetica, sans-serif;
        }
        .login-container {
            max-width: 400px;
            margin: 80px auto;
        }
        .card {
            border: 1px solid #dee2e6;
            border-radius: 0;
        }
        .card-header {
            background-color: #2c3e50;
            color: white;
            text-align: center;
            padding: 20px;
            border-radius: 0;
        }
        .card-header h4 {
            margin: 0;
            font-size: 18px;
        }
        .card-header p {
            margin: 5px 0 0 0;
            font-size: 13px;
            color: #bdc3c7;
        }
        .btn-primary {
            background-color: #2c3e50;
            border-color: #2c3e50;
            border-radius: 0;
            width: 100%;
        }
        .btn-primary:hover {
            background-color: #1a252f;
            border-color: #1a252f;
        }
        .form-control {
            border-radius: 0;
        }
        .alert {
            border-radius: 0;
        }
        .signup-link {
            text-align: center;
            margin-top: 15px;
            font-size: 14px;
        }
        .password-wrapper {
            position: relative;
        }
        .password-wrapper .form-control {
            padding-right: 60px;
        }
        .toggle-password {
            position: absolute;
            right: 10px;
            top: 50%;
            transform: translateY(-50%);
            cursor: pointer;
            font-size: 12px;
            color: #7f8c8d;
            user-select: none;
            background: none;
            border: none;
            padding: 0;
        }
        .toggle-password:hover {
            color: #2c3e50;
        }
    </style>
</head>
<body>

<div class="login-container">
    <div class="card">
        <div class="card-header">
            <h4>Ocean View Resort</h4>
            <p>Resort Management System</p>
        </div>
        <div class="card-body p-4">


            <% String message = request.getParameter("message"); %>
            <% if ("loggedout".equals(message)) { %>
            <div class="alert alert-success">
                You have been logged out successfully.
            </div>
            <% } else if ("signup_success".equals(message)) { %>
            <div class="alert alert-success">
                Registration successful! Please wait for admin approval.
            </div>
            <% } %>

            <%-- Error message from failed login --%>
            <% String error = (String) request.getAttribute("errorMessage"); %>
            <% if (error != null) { %>
            <div class="alert alert-danger"><%= error %></div>
            <% } %>

            <form method="post"
                  action="${pageContext.request.contextPath}/auth">
                <input type="hidden" name="action" value="login">

                <div class="mb-3">
                    <label for="username" class="form-label">Username</label>
                    <input type="text"
                           class="form-control"
                           id="username"
                           name="username"
                           value="<%= request.getAttribute("username") != null
                                    ? request.getAttribute("username") : "" %>"
                           required autofocus>
                </div>

                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <div class="password-wrapper">
                        <input type="password"
                               class="form-control"
                               id="password"
                               name="password"
                               required>
                        <button type="button"
                                class="toggle-password"
                                onclick="togglePassword('password', this)">
                            Show
                        </button>
                    </div>
                </div>

                <button type="submit" class="btn btn-primary">Login</button>
            </form>
        </div>
    </div>

    <div class="signup-link">
        <a href="${pageContext.request.contextPath}/auth?action=signup">
            Staff? Register for an account
        </a>
    </div>
</div>

<script>
    function togglePassword(fieldId, btn) {
        var field = document.getElementById(fieldId);
        if (field.type === "password") {
            field.type = "text";
            btn.textContent = "Hide";
        } else {
            field.type = "password";
            btn.textContent = "Show";
        }
    }
</script>

</body>
</html>