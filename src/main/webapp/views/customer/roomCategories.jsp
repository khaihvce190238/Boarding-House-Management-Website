<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Room Categories - AKDD House</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        body { background: #f4f6f9; font-family: 'Inter', sans-serif; }

        .page-hero {
            background: linear-gradient(135deg, #4f46e5, #7c3aed);
            color: #fff;
            padding: 48px 0 56px;
            margin-bottom: -32px;
        }
        .page-hero h1 { font-weight: 700; font-size: 2rem; }
        .page-hero p  { opacity: .85; font-size: 1rem; }

        .cat-card {
            border-radius: 18px;
            border: none;
            box-shadow: 0 4px 24px rgba(0,0,0,.08);
            overflow: hidden;
            transition: transform .2s, box-shadow .2s;
            text-decoration: none;
            display: block;
            background: #fff;
        }
        .cat-card:hover {
            transform: translateY(-4px);
            box-shadow: 0 12px 36px rgba(0,0,0,.13);
        }
        .cat-icon-wrap {
            height: 120px;
            display: flex; align-items: center; justify-content: center;
            font-size: 3rem;
        }
        .cat-available   .cat-icon-wrap { background: linear-gradient(135deg,#d1fae5,#a7f3d0); color: #059669; }
        .cat-occupied    .cat-icon-wrap { background: linear-gradient(135deg,#fee2e2,#fecaca); color: #dc2626; }
        .cat-maintenance .cat-icon-wrap { background: linear-gradient(135deg,#fef9c3,#fde68a); color: #d97706; }

        .cat-body { padding: 1.5rem; }
        .cat-title { font-weight: 700; font-size: 1.15rem; color: #1e1b4b; margin-bottom: .4rem; }
        .cat-count {
            font-size: 2rem; font-weight: 700; line-height: 1;
        }
        .cat-available   .cat-count { color: #059669; }
        .cat-occupied    .cat-count { color: #dc2626; }
        .cat-maintenance .cat-count { color: #d97706; }

        .cat-sub { font-size: .85rem; color: #6b7280; margin-top: .25rem; }
        .cat-btn {
            display: inline-flex; align-items: center; gap: .4rem;
            margin-top: 1rem; font-size: .85rem; font-weight: 600;
            color: #7c3aed; text-decoration: none;
        }
        .cat-btn:hover { text-decoration: underline; }

        .badge-available   { background:#d1fae5; color:#059669; }
        .badge-occupied    { background:#fee2e2; color:#dc2626; }
        .badge-maintenance { background:#fef9c3; color:#d97706; }
    </style>
</head>
<body>
    <%@ include file="../navbar.jsp" %>

    <%-- Hero --%>
    <div class="page-hero">
        <div class="container">
            <nav aria-label="breadcrumb" class="mb-3">
                <ol class="breadcrumb" style="--bs-breadcrumb-divider-color:rgba(255,255,255,.5);color:rgba(255,255,255,.7)">
                    <li class="breadcrumb-item">
                        <a href="${pageContext.request.contextPath}/" class="text-white text-opacity-75">Home</a>
                    </li>
                    <li class="breadcrumb-item active text-white">Room Categories</li>
                </ol>
            </nav>
            <h1><i class="bi bi-grid-3x3-gap me-2"></i>Room Categories</h1>
            <p class="mb-0">Browse rooms by availability status</p>
        </div>
    </div>

    <div class="container pb-5" style="padding-top: 48px;">

        <div class="row g-4 justify-content-center">

            <%-- Available ─────────────────────────────────────────────────── --%>
            <div class="col-md-4">
                <a href="${pageContext.request.contextPath}/room?action=publicList&status=available"
                   class="cat-card cat-available">
                    <div class="cat-icon-wrap">
                        <i class="bi bi-check-circle-fill"></i>
                    </div>
                    <div class="cat-body">
                        <div class="d-flex align-items-center gap-2 mb-1">
                            <span class="badge rounded-pill badge-available px-3 py-1">Available</span>
                        </div>
                        <div class="cat-title">Available Rooms</div>
                        <div class="cat-count">${counts['available']}</div>
                        <div class="cat-sub">rooms ready to rent</div>
                        <div class="cat-btn">
                            View rooms <i class="bi bi-arrow-right"></i>
                        </div>
                    </div>
                </a>
            </div>

            <%-- Occupied ──────────────────────────────────────────────────── --%>
            <div class="col-md-4">
                <a href="${pageContext.request.contextPath}/room?action=publicList&status=occupied"
                   class="cat-card cat-occupied">
                    <div class="cat-icon-wrap">
                        <i class="bi bi-person-fill-check"></i>
                    </div>
                    <div class="cat-body">
                        <div class="d-flex align-items-center gap-2 mb-1">
                            <span class="badge rounded-pill badge-occupied px-3 py-1">Occupied</span>
                        </div>
                        <div class="cat-title">Occupied Rooms</div>
                        <div class="cat-count">${counts['occupied']}</div>
                        <div class="cat-sub">rooms currently rented</div>
                        <div class="cat-btn">
                            View rooms <i class="bi bi-arrow-right"></i>
                        </div>
                    </div>
                </a>
            </div>

            <%-- Maintenance ───────────────────────────────────────────────── --%>
            <div class="col-md-4">
                <a href="${pageContext.request.contextPath}/room?action=publicList&status=maintenance"
                   class="cat-card cat-maintenance">
                    <div class="cat-icon-wrap">
                        <i class="bi bi-tools"></i>
                    </div>
                    <div class="cat-body">
                        <div class="d-flex align-items-center gap-2 mb-1">
                            <span class="badge rounded-pill badge-maintenance px-3 py-1">Maintenance</span>
                        </div>
                        <div class="cat-title">Under Maintenance</div>
                        <div class="cat-count">${counts['maintenance']}</div>
                        <div class="cat-sub">rooms temporarily unavailable</div>
                        <div class="cat-btn">
                            View rooms <i class="bi bi-arrow-right"></i>
                        </div>
                    </div>
                </a>
            </div>

        </div>

        <%-- View all rooms button --%>
        <div class="text-center mt-5">
            <a href="${pageContext.request.contextPath}/room?action=publicList"
               class="btn btn-primary px-5 py-2 fw-semibold rounded-pill"
               style="background:linear-gradient(135deg,#4f46e5,#7c3aed);border:none">
                <i class="bi bi-list-ul me-2"></i>View All Rooms
            </a>
        </div>

    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
