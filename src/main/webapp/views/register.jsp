<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng ký - AKDD House</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #667eea, #764ba2);
            min-height: 100vh;
        }
        .register-card {
            border-radius: 16px;
            border: none;
        }
        .form-control:focus {
            border-color: #764ba2;
            box-shadow: 0 0 0 0.2rem rgba(118, 75, 162, 0.25);
        }
        .btn-register {
            background: linear-gradient(135deg, #667eea, #764ba2);
            border: none;
        }
        .btn-register:hover {
            background: linear-gradient(135deg, #5568d6, #6a3fa0);
        }
        .brand-title {
            color: #764ba2;
            font-weight: 700;
        }
    </style>
</head>

<body class="d-flex justify-content-center align-items-center py-5">

    <div class="card shadow-lg register-card" style="width: 480px;">
        <div class="card-body p-5">

            <div class="text-center mb-4">
                <i class="bi bi-house-heart-fill fs-1 text-purple" style="color:#764ba2"></i>
                <h3 class="brand-title mt-2">AKDD House</h3>
                <p class="text-muted small">Tạo tài khoản mới</p>
            </div>

            <c:if test="${not empty error}">
                <div class="alert alert-danger d-flex align-items-center" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>
                    <span>${error}</span>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/auth" method="post" novalidate>
                <input type="hidden" name="action" value="register">

                <div class="mb-3">
                    <label class="form-label fw-semibold">
                        Tên đăng nhập <span class="text-danger">*</span>
                    </label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="bi bi-person"></i></span>
                        <input type="text" name="username" class="form-control"
                               placeholder="Nhập tên đăng nhập"
                               value="${param.username}" required>
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold">
                        Mật khẩu <span class="text-danger">*</span>
                    </label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="bi bi-lock"></i></span>
                        <input type="password" name="password" id="password" class="form-control"
                               placeholder="Nhập mật khẩu (ít nhất 6 ký tự)" required>
                        <button class="btn btn-outline-secondary" type="button"
                                onclick="togglePassword('password','eyePassword')">
                            <i class="bi bi-eye" id="eyePassword"></i>
                        </button>
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold">
                        Xác nhận mật khẩu <span class="text-danger">*</span>
                    </label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="bi bi-lock-fill"></i></span>
                        <input type="password" name="confirmPassword" id="confirmPassword" class="form-control"
                               placeholder="Nhập lại mật khẩu" required>
                        <button class="btn btn-outline-secondary" type="button"
                                onclick="togglePassword('confirmPassword','eyeConfirm')">
                            <i class="bi bi-eye" id="eyeConfirm"></i>
                        </button>
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold">Họ và tên</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="bi bi-person-badge"></i></span>
                        <input type="text" name="fullName" class="form-control"
                               placeholder="Nhập họ và tên"
                               value="${param.fullName}">
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold">Email</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="bi bi-envelope"></i></span>
                        <input type="email" name="email" class="form-control"
                               placeholder="Nhập địa chỉ email"
                               value="${param.email}">
                    </div>
                </div>

                <div class="mb-4">
                    <label class="form-label fw-semibold">Số điện thoại</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="bi bi-telephone"></i></span>
                        <input type="tel" name="phone" class="form-control"
                               placeholder="Nhập số điện thoại"
                               value="${param.phone}">
                    </div>
                </div>

                <div class="d-grid mb-3">
                    <button type="submit" class="btn btn-register btn-primary fw-semibold">
                        <i class="bi bi-person-plus me-1"></i> Đăng ký
                    </button>
                </div>

                <div class="text-center">
                    <small class="text-muted">
                        Đã có tài khoản?
                        <a href="${pageContext.request.contextPath}/auth?action=login"
                           class="text-decoration-none fw-semibold">
                            Đăng nhập
                        </a>
                    </small>
                </div>

            </form>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function togglePassword(inputId, iconId) {
            var input = document.getElementById(inputId);
            var icon  = document.getElementById(iconId);
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
