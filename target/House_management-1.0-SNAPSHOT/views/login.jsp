<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <meta charset="UTF-8">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        body{
            background: linear-gradient(135deg, #667eea, #764ba2);
            height: 100vh;
        }
        .login-card{
            border-radius: 15px;
        }
    </style>
</head>

<body class="d-flex justify-content-center align-items-center">

    <div class="card shadow-lg login-card" style="width: 400px;">
        <div class="card-body p-4">

            <h3 class="text-center mb-4">Welcome To AKDD</h3>

            <!-- Show error message -->
            <%
                String error = (String) request.getAttribute("error");
                if (error != null) {
            %>
                <div class="alert alert-danger text-center">
                    <%= error %>
                </div>
            <%
                }
            %>

            <!-- LOGIN FORM -->
            <form action="${pageContext.request.contextPath}/auth" method="post">
                <input type="hidden" name="action" value="login"/>

                <div class="mb-3">
                    <label class="form-label">Username</label>
                    <input type="text" name="username" class="form-control"
                           placeholder="Enter username" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Password</label>
                    <input type="password" name="password" class="form-control"
                           placeholder="Enter password" required>
                </div>

                <div class="d-grid">
                    <button type="submit" class="btn btn-primary">
                        Login
                    </button>
                </div>

                <div class="text-center mt-3">
                    <small>
                        Don't have an account?
                        <a href="${pageContext.request.contextPath}/auth?action=register">
                            Register
                        </a>
                    </small>
                </div>

            </form>

        </div>
    </div>

</body>
</html>