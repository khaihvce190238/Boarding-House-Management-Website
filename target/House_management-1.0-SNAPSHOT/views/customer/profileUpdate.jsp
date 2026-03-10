<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Chỉnh sửa hồ sơ - AKDD House</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <style>
        body { background-color: #f4f6f9; }
        .pu-card { border-radius: 16px; border: none; }
        .form-control:focus, .form-select:focus {
            border-color: #764ba2;
            box-shadow: 0 0 0 0.2rem rgba(118,75,162,0.2);
        }
        .btn-save {
            background: linear-gradient(135deg, #667eea, #764ba2);
            border: none;
        }
        .btn-save:hover { background: linear-gradient(135deg, #5568d6, #6a3fa0); }
        .page-header {
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: white;
            border-radius: 12px;
            padding: 18px 24px;
            margin-bottom: 24px;
        }
        .avatar-circle {
            width: 80px; height: 80px;
            border-radius: 50%;
            background: rgba(255,255,255,0.2);
            display: flex; align-items: center; justify-content: center;
            font-size: 36px;
            border: 3px solid rgba(255,255,255,0.4);
        }
    </style>
</head>
<body>
    <%@ include file="../navbar.jsp" %>

    <div class="container mt-4 mb-5" style="max-width: 640px;">

        <div class="page-header d-flex align-items-center gap-3">
            <div class="avatar-circle">
                <i class="bi bi-person-fill"></i>
            </div>
            <div>
                <h4 class="mb-0 fw-bold">Chỉnh sửa hồ sơ</h4>
                <small class="opacity-75">@${user.username}</small>
            </div>
        </div>

        <div class="card pu-card shadow-sm">
            <div class="card-body p-4">

                <c:if test="${not empty error}">
                    <div class="alert alert-danger d-flex align-items-center">
                        <i class="bi bi-exclamation-triangle-fill me-2"></i>
                        <span>${error}</span>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/customer" method="post">
                    <input type="hidden" name="action" value="updateProfile">

                    <div class="mb-3">
                        <label class="form-label fw-semibold">
                            Tên đăng nhập
                        </label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-at"></i></span>
                            <input type="text" class="form-control bg-light"
                                   value="${user.username}" readonly>
                        </div>
                        <div class="form-text text-muted">Tên đăng nhập không thể thay đổi.</div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">
                            Họ và tên <span class="text-danger">*</span>
                        </label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-person"></i></span>
                            <input type="text" name="fullName" class="form-control"
                                   placeholder="Nhập họ và tên"
                                   value="${user.fullName}" required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Email</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-envelope"></i></span>
                            <input type="email" name="email" class="form-control"
                                   placeholder="Nhập địa chỉ email"
                                   value="${user.email}">
                        </div>
                    </div>

                    <div class="mb-4">
                        <label class="form-label fw-semibold">Số điện thoại</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-telephone"></i></span>
                            <input type="tel" name="phone" class="form-control"
                                   placeholder="Nhập số điện thoại"
                                   value="${user.phone}">
                        </div>
                    </div>

                    <div class="d-flex gap-2">
                        <button type="submit" class="btn btn-save btn-primary flex-fill fw-semibold">
                            <i class="bi bi-check-circle me-1"></i> Lưu thay đổi
                        </button>
                        <a href="${pageContext.request.contextPath}/customer?action=profile"
                           class="btn btn-outline-secondary flex-fill fw-semibold">
                            <i class="bi bi-x-circle me-1"></i> Hủy
                        </a>
                    </div>

                </form>
            </div>
        </div>

    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
