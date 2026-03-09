<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đổi mật khẩu - AKDD House</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <style>
        body { background-color: #f4f6f9; }
        .cp-card { border-radius: 16px; border: none; }
        .form-control:focus {
            border-color: #764ba2;
            box-shadow: 0 0 0 0.2rem rgba(118,75,162,0.2);
        }
        .btn-change {
            background: linear-gradient(135deg, #667eea, #764ba2);
            border: none;
        }
        .btn-change:hover { background: linear-gradient(135deg, #5568d6, #6a3fa0); }
        .page-header {
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: white;
            border-radius: 12px;
            padding: 20px 24px;
            margin-bottom: 24px;
        }
        .strength-bar { height: 6px; border-radius: 3px; transition: all .3s; }
    </style>
</head>

<body>
    <%@ include file="../navbar.jsp" %>

    <div class="container mt-5" style="max-width: 600px;">

        <div class="page-header d-flex align-items-center gap-3">
            <i class="bi bi-shield-lock-fill fs-3"></i>
            <div>
                <h4 class="mb-0 fw-bold">Đổi mật khẩu</h4>
                <small class="opacity-75">Cập nhật mật khẩu tài khoản của bạn</small>
            </div>
        </div>

        <div class="card cp-card shadow-sm">
            <div class="card-body p-4">

                <c:if test="${not empty success}">
                    <div class="alert alert-success d-flex align-items-center" role="alert">
                        <i class="bi bi-check-circle-fill me-2"></i>
                        <span>${success}</span>
                    </div>
                </c:if>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger d-flex align-items-center" role="alert">
                        <i class="bi bi-exclamation-triangle-fill me-2"></i>
                        <span>${error}</span>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/auth" method="post" id="changePasswordForm">
                    <input type="hidden" name="action" value="changePassword">

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Mật khẩu hiện tại</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-lock"></i></span>
                            <input type="password" name="oldPassword" id="oldPassword" class="form-control"
                                   placeholder="Nhập mật khẩu hiện tại" required autofocus>
                            <button class="btn btn-outline-secondary" type="button"
                                    onclick="togglePassword('oldPassword','eyeOld')">
                                <i class="bi bi-eye" id="eyeOld"></i>
                            </button>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Mật khẩu mới</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-lock-fill"></i></span>
                            <input type="password" name="newPassword" id="newPassword" class="form-control"
                                   placeholder="Nhập mật khẩu mới (ít nhất 6 ký tự)"
                                   required oninput="checkStrength(this.value)">
                            <button class="btn btn-outline-secondary" type="button"
                                    onclick="togglePassword('newPassword','eyeNew')">
                                <i class="bi bi-eye" id="eyeNew"></i>
                            </button>
                        </div>
                        <div class="mt-2">
                            <div class="progress" style="height:6px;">
                                <div id="strengthBar" class="progress-bar strength-bar" style="width:0%"></div>
                            </div>
                            <small id="strengthText" class="text-muted"></small>
                        </div>
                    </div>

                    <div class="mb-4">
                        <label class="form-label fw-semibold">Xác nhận mật khẩu mới</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-shield-check"></i></span>
                            <input type="password" name="confirmPassword" id="confirmPassword" class="form-control"
                                   placeholder="Nhập lại mật khẩu mới"
                                   required oninput="checkMatch()">
                            <button class="btn btn-outline-secondary" type="button"
                                    onclick="togglePassword('confirmPassword','eyeConfirm')">
                                <i class="bi bi-eye" id="eyeConfirm"></i>
                            </button>
                        </div>
                        <small id="matchText" class="mt-1 d-block"></small>
                    </div>

                    <div class="d-flex gap-2">
                        <button type="submit" class="btn btn-change btn-primary flex-fill fw-semibold">
                            <i class="bi bi-arrow-repeat me-1"></i> Đổi mật khẩu
                        </button>
                        <a href="${pageContext.request.contextPath}/customer?action=profile"
                           class="btn btn-outline-secondary flex-fill fw-semibold">
                            <i class="bi bi-x-circle me-1"></i> Hủy
                        </a>
                    </div>

                </form>
            </div>
        </div>

        <div class="card cp-card shadow-sm mt-3">
            <div class="card-body p-3">
                <h6 class="text-muted fw-semibold mb-2">
                    <i class="bi bi-info-circle me-1"></i> Lưu ý bảo mật
                </h6>
                <ul class="text-muted small mb-0">
                    <li>Mật khẩu phải có ít nhất 6 ký tự</li>
                    <li>Nên kết hợp chữ hoa, chữ thường, số và ký tự đặc biệt</li>
                    <li>Không sử dụng mật khẩu giống tên đăng nhập</li>
                    <li>Không chia sẻ mật khẩu với người khác</li>
                </ul>
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

        function checkStrength(val) {
            var bar  = document.getElementById('strengthBar');
            var text = document.getElementById('strengthText');
            var score = 0;
            if (val.length >= 6)  score++;
            if (val.length >= 10) score++;
            if (/[A-Z]/.test(val)) score++;
            if (/[0-9]/.test(val)) score++;
            if (/[^A-Za-z0-9]/.test(val)) score++;

            var levels = [
                {w: '20%',  cls: 'bg-danger',  lbl: 'Rất yếu'},
                {w: '40%',  cls: 'bg-warning', lbl: 'Yếu'},
                {w: '60%',  cls: 'bg-info',    lbl: 'Trung bình'},
                {w: '80%',  cls: 'bg-primary', lbl: 'Mạnh'},
                {w: '100%', cls: 'bg-success', lbl: 'Rất mạnh'}
            ];
            var lvl = levels[Math.max(0, score - 1)] || levels[0];
            bar.style.width = lvl.w;
            bar.className = 'progress-bar strength-bar ' + lvl.cls;
            text.textContent = val.length > 0 ? 'Độ mạnh: ' + lvl.lbl : '';
        }

        function checkMatch() {
            var np = document.getElementById('newPassword').value;
            var cp = document.getElementById('confirmPassword').value;
            var mt = document.getElementById('matchText');
            if (cp.length === 0) { mt.textContent = ''; return; }
            if (np === cp) {
                mt.textContent = '✓ Mật khẩu khớp';
                mt.className = 'mt-1 d-block text-success small';
            } else {
                mt.textContent = '✗ Mật khẩu không khớp';
                mt.className = 'mt-1 d-block text-danger small';
            }
        }
    </script>
</body>
</html>
