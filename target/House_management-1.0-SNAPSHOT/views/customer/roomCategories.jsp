<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
            color: #fff; padding: 48px 0 56px; margin-bottom: -32px;
        }
        .page-hero h1 { font-weight: 700; font-size: 2rem; }
        .page-hero p  { opacity: .85; }

        /* Summary strip */
        .summary-strip {
            display: flex; gap: 1.5rem; flex-wrap: wrap;
            margin-bottom: 2.5rem;
        }
        .s-chip {
            background: #fff; border-radius: 14px;
            padding: 14px 22px;
            box-shadow: 0 2px 12px rgba(0,0,0,.07);
            display: flex; align-items: center; gap: .75rem;
            flex: 1; min-width: 140px;
        }
        .s-chip .chip-icon {
            width: 42px; height: 42px; border-radius: 10px;
            display: flex; align-items: center; justify-content: center;
            font-size: 1.2rem; flex-shrink: 0;
        }
        .chip-avail  .chip-icon { background:#d1fae5; color:#059669; }
        .chip-occup  .chip-icon { background:#fee2e2; color:#dc2626; }
        .chip-maint  .chip-icon { background:#fef9c3; color:#d97706; }
        .s-chip .chip-num  { font-size: 1.4rem; font-weight: 700; line-height:1; color:#1e1b4b; }
        .s-chip .chip-lbl  { font-size: .78rem; color:#9ca3af; margin-top:2px; }

        /* Category cards */
        .cat-card {
            border-radius: 20px; border: none;
            box-shadow: 0 4px 24px rgba(0,0,0,.08);
            overflow: hidden; transition: transform .2s, box-shadow .2s;
            text-decoration: none; display: block; background: #fff;
        }
        .cat-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 14px 40px rgba(0,0,0,.13);
        }
        .cat-header {
            height: 130px;
            display: flex; align-items: center; justify-content: center;
            font-size: 3.2rem; position: relative;
        }
        .cat-header .price-tag {
            position: absolute; bottom: 12px; right: 14px;
            background: rgba(255,255,255,.85);
            border-radius: 10px; padding: 3px 10px;
            font-size: .78rem; font-weight: 700;
            color: #1e1b4b;
        }

        /* Palette cycling — 5 colours for 5 default categories */
        .palette-0 { background: linear-gradient(135deg,#e0e7ff,#c7d2fe); color:#3730a3; }
        .palette-1 { background: linear-gradient(135deg,#d1fae5,#a7f3d0); color:#065f46; }
        .palette-2 { background: linear-gradient(135deg,#ede9fe,#ddd6fe); color:#5b21b6; }
        .palette-3 { background: linear-gradient(135deg,#fef3c7,#fde68a); color:#92400e; }
        .palette-4 { background: linear-gradient(135deg,#fce7f3,#fbcfe8); color:#9d174d; }

        .cat-body { padding: 1.4rem; }
        .cat-name { font-weight: 700; font-size: 1.1rem; color: #1e1b4b; margin-bottom: .3rem; }
        .cat-desc { font-size: .82rem; color: #6b7280; min-height: 36px; }
        .cat-footer {
            display: flex; align-items: center; justify-content: space-between;
            margin-top: 1rem; padding-top: .75rem; border-top: 1px solid #f3f4f6;
        }
        .cat-rooms-count {
            font-size: .82rem; color: #9ca3af; font-weight: 500;
        }
        .cat-rooms-count strong { color: #4f46e5; }
        .cat-cta {
            font-size: .82rem; font-weight: 700; color: #7c3aed;
            display: flex; align-items: center; gap: .3rem;
        }

        /* View-all button */
        .btn-all {
            background: linear-gradient(135deg,#4f46e5,#7c3aed);
            color: #fff; border: none; border-radius: 50px;
            padding: 11px 36px; font-weight: 600;
            text-decoration: none; transition: opacity .2s;
        }
        .btn-all:hover { opacity: .9; color: #fff; }

        .empty-state { text-align:center; padding:4rem 1rem; color:#9ca3af; }
        .empty-state i { font-size:3rem; margin-bottom:1rem; }
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
            <p class="mb-0">Browse rooms by type — find the perfect fit for your needs</p>
        </div>
    </div>

    <div class="container pb-5" style="padding-top: 48px;">

        <%-- Status summary strip --%>
        <div class="summary-strip">
            <a href="${pageContext.request.contextPath}/room?action=publicList&status=available"
               class="s-chip chip-avail text-decoration-none">
                <div class="chip-icon"><i class="bi bi-check-circle-fill"></i></div>
                <div>
                    <div class="chip-num">${statusCounts['available']}</div>
                    <div class="chip-lbl">Available rooms</div>
                </div>
            </a>
            <a href="${pageContext.request.contextPath}/room?action=publicList&status=occupied"
               class="s-chip chip-occup text-decoration-none">
                <div class="chip-icon"><i class="bi bi-person-fill-check"></i></div>
                <div>
                    <div class="chip-num">${statusCounts['occupied']}</div>
                    <div class="chip-lbl">Occupied rooms</div>
                </div>
            </a>
            <a href="${pageContext.request.contextPath}/room?action=publicList&status=maintenance"
               class="s-chip chip-maint text-decoration-none">
                <div class="chip-icon"><i class="bi bi-tools"></i></div>
                <div>
                    <div class="chip-num">${statusCounts['maintenance']}</div>
                    <div class="chip-lbl">Under maintenance</div>
                </div>
            </a>
        </div>

        <%-- Heading --%>
        <div class="d-flex align-items-center justify-content-between mb-3">
            <h5 class="fw-bold mb-0" style="color:#1e1b4b">All Room Types</h5>
            <a href="${pageContext.request.contextPath}/room?action=publicList"
               class="btn-all">
                <i class="bi bi-list-ul me-1"></i>View All Rooms
            </a>
        </div>

        <%-- Category cards --%>
        <c:choose>
            <c:when test="${empty categories}">
                <div class="empty-state">
                    <i class="bi bi-grid d-block"></i>
                    <h5>No room categories found</h5>
                    <p>Room categories have not been set up yet.</p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="row g-4">
                    <c:forEach var="cat" items="${categories}" varStatus="loop">
                        <div class="col-sm-6 col-lg-4">
                            <a href="${pageContext.request.contextPath}/room?action=publicList&categoryId=${cat.categoryId}"
                               class="cat-card">
                                <div class="cat-header palette-${loop.index % 5}">
                                    <%-- Icon based on rough category position --%>
                                    <c:choose>
                                        <c:when test="${loop.index == 0}"><i class="bi bi-person"></i></c:when>
                                        <c:when test="${loop.index == 1}"><i class="bi bi-people-fill"></i></c:when>
                                        <c:when test="${loop.index == 2}"><i class="bi bi-grid-1x2-fill"></i></c:when>
                                        <c:when test="${loop.index == 3}"><i class="bi bi-house-heart-fill"></i></c:when>
                                        <c:otherwise><i class="bi bi-star-fill"></i></c:otherwise>
                                    </c:choose>
                                    <span class="price-tag">
                                        From <fmt:formatNumber value="${cat.basePrice}" groupingUsed="true" maxFractionDigits="0"/>₫/mo
                                    </span>
                                </div>
                                <div class="cat-body">
                                    <div class="cat-name">${cat.categoryName}</div>
                                    <div class="cat-desc">
                                        <c:choose>
                                            <c:when test="${not empty cat.description}">${cat.description}</c:when>
                                            <c:otherwise>—</c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="cat-footer">
                                        <div class="cat-rooms-count">
                                            <strong>${cat.roomCount}</strong> room<c:if test="${cat.roomCount != 1}">s</c:if>
                                        </div>
                                        <div class="cat-cta">
                                            Browse <i class="bi bi-arrow-right"></i>
                                        </div>
                                    </div>
                                </div>
                            </a>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>

    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
