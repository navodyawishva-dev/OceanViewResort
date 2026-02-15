<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Staff Registration - Ocean View Resort</title>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css">
    <style>
        body {
            background-color: #ecf0f1;
            font-family: Arial, Helvetica, sans-serif;
        }
        .signup-container {
            max-width: 500px;
            margin: 50px auto;
        }
        .card { border-radius: 0; }
        .card-header {
            background-color: #2c3e50;
            color: white;
            border-radius: 0;
            padding: 15px 20px;
        }
        .btn-primary {
            background-color: #2c3e50;
            border-color: #2c3e50;
            border-radius: 0;
        }
        .form-control { border-radius: 0; }
        .alert { border-radius: 0; }
        .eye-toggle {
            position: absolute;
            right: 10px;
            top: 50%;
            transform: translateY(-50%);
            cursor: pointer;
            color: #7f8c8d;
            font-size: 16px;
            user-select: none;
        }
        .eye-toggle:hover { color: #2c3e50; }
    </style>
</head>
<body>

<div class="signup-container">
    <div class="card">
        <div class="card-header">
            <h5 class="mb-0">Staff Registration</h5>
        </div>
        <div class="card-body p-4">

            <% String error = (String) request.getAttribute("errorMessage"); %>
            <% if (error != null) { %>
            <div class="alert alert-danger"><%= error %></div>
            <% } %>

            <form method="post"
                  action="${pageContext.request.contextPath}/auth">
                <input type="hidden" name="action" value="signup">

                <div class="mb-3">
                    <label class="form-label">Full Name *</label>
                    <input type="text" class="form-control"
                           name="fullName"
                           value="<%= request.getAttribute("fullName") != null
                                    ? request.getAttribute("fullName") : "" %>"
                           required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Username *</label>
                    <input type="text" class="form-control"
                           name="username"
                           value="<%= request.getAttribute("username") != null
                                    ? request.getAttribute("username") : "" %>"
                           required>
                    <small class="text-muted">
                        4-20 characters, letters/numbers/underscore
                    </small>
                </div>

                <div class="mb-3">
                    <label class="form-label">Password *</label>
                    <div style="position:relative">
                        <input type="password"
                               class="form-control"
                               id="signupPassword"
                               name="password" required>
                        <span class="eye-toggle"
                              onclick="togglePassword('signupPassword', this)">
                            👁️
                        </span>
                    </div>
                    <small class="text-muted">Minimum 6 characters</small>
                </div>

                <div class="mb-3">
                    <label class="form-label">Email</label>
                    <input type="email" class="form-control"
                           name="email"
                           value="<%= request.getAttribute("email") != null
                                    ? request.getAttribute("email") : "" %>">
                </div>

                <div class="mb-3">
                    <label class="form-label">Phone</label>
                    <input type="text" class="form-control"
                           name="phone"
                           value="<%= request.getAttribute("phone") != null
                                    ? request.getAttribute("phone") : "" %>"
                           placeholder="0771234567">
                </div>

                <div class="mb-3">
                    <label class="form-label">Role</label>
                    <select class="form-control" name="role">
                        <option value="Receptionist">Receptionist</option>

                    </select>
                </div>

                <div class="d-flex gap-2">
                    <button type="submit" class="btn btn-primary">
                        Register
                    </button>
                    <a href="${pageContext.request.contextPath}/login.jsp"
                       class="btn btn-secondary"
                       style="border-radius:0">
                        Back to Login
                    </a>
                </div>
            </form>
        </div>
    </div>
    <p class="text-center mt-3 text-muted" style="font-size:13px">
        After registration, wait for Admin approval before logging in.
    </p>
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