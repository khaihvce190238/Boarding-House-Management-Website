<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quên mật khẩu - AKDD House</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #667eea, #764ba2);
            min-height: 100vh;
        }
        .fp-card {
            border-radius: 16px;
            border: none;
        }
        .form-control:focus {
            border-color: #764ba2;
            box-shadow: 0 0 0 0.2rem rgba(118, 75, 162, 0.25);
        }
        .btn-fp {
            background: linear-gradient(135deg, #667eea, #764ba2);
            border: none;
        }
        .btn-fp:hover {
            background: linear-gradient(135deg, #5568d6, #6a3fa0);
        }
        .step-badge {
            width: 32px; height: 32px;
            border-radius: 50%;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            font-weight: 700;
            font-size: 14px;
        }
        .step-active { background: #764ba2; color: white; }
        .step-done   { background: #28a745; color: white; }
        .step-inactive { background: #dee2e6; color: #6c757d; }
    </style>
</head>

<body class="d-flex justify-content-center align-items-center">

    <div class="card shadow-lg fp-card" style="width: 460px;">
        <div class="card-body p-5">

            <div class="text-center mb-4">
                <i class="bi bi-shield-lock-fill fs-1" style="color:#764ba2"></i>
                <h3 class="fw-bold mt-2" style="color:#764ba2">Quên mật khẩu</h3>
            </div>

            <%-- Step indicator --%>
            <div class="d-flex align-items-center justify-content-center mb-4 gap-2">
                <span class="step-badge ${phase == 1 ? 'step-active' : 'step-done'}">1</span>
                <span class="text-muted small">Xác minh tài khoản</span>
                <span class="text-muted mx-1">—</span>
                <span class="step-badge ${phase == 2 ? 'step-active' : 'step-inactive'}">2</span>
                <span class="text-muted small">Đặt mật khẩu mới</span>
            </div>

            <c:if test="${not empty error}">
                <div class="alert alert-danger d-flex align-items-center" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>
                    <span>${error}</span>
                </div>
            </c:if>

            <%-- ========== PHASE 1: Verify username + email ========== --%>
            <c:if test="${phase == 1}">
                <p class="text-muted small text-center mb-4">
                    Nhập tên đăng nhập và email của bạn để xác minh tài khoản.
                </p>

                <form action="${pageContext.request.contextPath}/auth" method="post">
                    <input type="hidden" name="action" value="verifyReset">

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Tên đăng nhập</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-person"></i></span>
                            <input type="text" name="username" class="form-control"
                                   placeholder="Nhập tên đăng nhập" required autofocus>
                        </div>
                    </div>

                    <div class="mb-4">
                        <label class="form-label fw-semibold">Email đã đăng ký</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-envelope"></i></span>
                            <input type="email" name="email" class="form-control"
                                   placeholder="Nhập email tài khoản" required>
                        </div>
                    </div>

                    <div class="d-grid mb-3">
                        <button type="submit" class="btn btn-fp btn-primary fw-semibold">
                            <i class="bi bi-arrow-right-circle me-1"></i> Xác minh
                        </button>
                    </div>

                </form>
            </c:if>

            <%-- ========== PHASE 2: Set new password ========== --%>
            <c:if test="${phase == 2}">
                <div class="alert alert-success d-flex align-items-center mb-4">
                    <i class="bi bi-check-circle-fill me-2"></i>
                    <span>Xác minh thành công! Hãy đặt mật khẩu mới.</span>
                </div>

                <form action="${pageContext.request.contextPath}/auth" method="post">
                    <input type="hidden" name="action" value="doResetPassword">

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Mật khẩu mới</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-lock"></i></span>
                            <input type="password" name="newPassword" id="newPassword" class="form-control"
                                   placeholder="Nhập mật khẩu mới (ít nhất 6 ký tự)" required autofocus>
                            <button class="btn btn-outline-secondary" type="button"
                                    onclick="togglePassword('newPassword','eyeNew')">
                                <i class="bi bi-eye" id="eyeNew"></i>
                            </button>
                        </div>
                    </div>

                    <div class="mb-4">
                        <label class="form-label fw-semibold">Xác nhận mật khẩu mới</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-lock-fill"></i></span>
                            <input type="password" name="confirmPassword" id="confirmPassword" class="form-control"
                                   placeholder="Nhập lại mật khẩu mới" required>
                            <button class="btn btn-outline-secondary" type="button"
                                    onclick="togglePassword('confirmPassword','eyeConfirm')">
                                <i class="bi bi-eye" id="eyeConfirm"></i>
                            </button>
                        </div>
                    </div>

                    <div class="d-grid mb-3">
                        <button type="submit" class="btn btn-fp btn-primary fw-semibold">
                            <i class="bi bi-shield-check me-1"></i> Đặt lại mật khẩu
                        </button>
                    </div>

                </form>
            </c:if>

            <div class="text-center mt-2">
                <a href="${pageContext.request.contextPath}/auth?action=login"
                   class="text-decoration-none small text-muted">
                    <i class="bi bi-arrow-left me-1"></i> Quay lại đăng nhập
                </a>
            </div>

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
