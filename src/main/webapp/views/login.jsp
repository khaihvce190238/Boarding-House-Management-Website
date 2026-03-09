<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng nhập - AKDD House</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #667eea, #764ba2);
            min-height: 100vh;
        }
        .login-card {
            border-radius: 16px;
            border: none;
        }
        .form-control:focus {
            border-color: #764ba2;
            box-shadow: 0 0 0 0.2rem rgba(118, 75, 162, 0.25);
        }
        .btn-login {
            background: linear-gradient(135deg, #667eea, #764ba2);
            border: none;
        }
        .btn-login:hover {
            background: linear-gradient(135deg, #5568d6, #6a3fa0);
        }
        .brand-title {
            color: #764ba2;
            font-weight: 700;
        }
    </style>
</head>

<body class="d-flex justify-content-center align-items-center">

    <div class="card shadow-lg login-card" style="width: 420px;">
        <div class="card-body p-5">

            <div class="text-center mb-4">
                <i class="bi bi-house-heart-fill fs-1" style="color:#764ba2"></i>
                <h3 class="brand-title mt-2">AKDD House</h3>
                <p class="text-muted small">Chào mừng bạn trở lại</p>
            </div>

            <%-- Success message (from register / reset password) --%>
            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success d-flex align-items-center" role="alert">
                    <i class="bi bi-check-circle-fill me-2"></i>
                    <span>${sessionScope.successMessage}</span>
                </div>
                <% session.removeAttribute("successMessage"); %>
            </c:if>

            <%-- Error message --%>
            <c:if test="${not empty error}">
                <div class="alert alert-danger d-flex align-items-center" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>
                    <span>${error}</span>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/auth" method="post">
                <input type="hidden" name="action" value="login">

                <div class="mb-3">
                    <label class="form-label fw-semibold">Tên đăng nhập</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="bi bi-person"></i></span>
                        <input type="text" name="username" class="form-control"
                               placeholder="Nhập tên đăng nhập"
                               value="${param.username}" required autofocus>
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold">Mật khẩu</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="bi bi-lock"></i></span>
                        <input type="password" name="password" id="password" class="form-control"
                               placeholder="Nhập mật khẩu" required>
                        <button class="btn btn-outline-secondary" type="button"
                                onclick="togglePassword()">
                            <i class="bi bi-eye" id="eyeIcon"></i>
                        </button>
                    </div>
                    <div class="text-end mt-1">
                        <a href="${pageContext.request.contextPath}/auth?action=forgetPassword"
                           class="text-decoration-none small" style="color:#764ba2">
                            Quên mật khẩu?
                        </a>
                    </div>
                </div>

                <div class="d-grid mb-3">
                    <button type="submit" class="btn btn-login btn-primary fw-semibold">
                        <i class="bi bi-box-arrow-in-right me-1"></i> Đăng nhập
                    </button>
                </div>

                <div class="text-center">
                    <small class="text-muted">
                        Chưa có tài khoản?
                        <a href="${pageContext.request.contextPath}/auth?action=register"
                           class="text-decoration-none fw-semibold">
                            Đăng ký ngay
                        </a>
                    </small>
                </div>

            </form>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function togglePassword() {
            var input = document.getElementById('password');
            var icon  = document.getElementById('eyeIcon');
            if (input.type === 'password') {
                input.type = 'text';
                icon.classList.replace('bi-eye', 'bi-eye-slash');
            } else {
                input.type = 'password';
                icon.classList.replace('bi-eye-slash', 'bi-eye');
            }
        }
    </script>
</body>
</html>
