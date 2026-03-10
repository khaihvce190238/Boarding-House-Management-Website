<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Room List - AKDD House</title>
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

        /* Filter tabs */
        .filter-bar {
            background: #fff;
            border-radius: 50px;
            padding: 6px;
            display: inline-flex;
            gap: 4px;
            box-shadow: 0 2px 12px rgba(0,0,0,.1);
        }
        .filter-btn {
            border-radius: 50px;
            border: none;
            padding: 8px 20px;
            font-weight: 600; font-size: .85rem;
            cursor: pointer;
            text-decoration: none;
            display: inline-flex; align-items: center; gap: .4rem;
            color: #6b7280;
            background: transparent;
            transition: all .2s;
        }
        .filter-btn:hover  { background: #f3f4f6; color: #374151; }
        .filter-btn.active { background: linear-gradient(135deg,#4f46e5,#7c3aed); color: #fff; }
        .filter-count {
            background: rgba(255,255,255,.25);
            border-radius: 20px;
            padding: 1px 7px;
            font-size: .75rem;
        }
        .filter-btn:not(.active) .filter-count {
            background: #e5e7eb; color: #374151;
        }

        /* Room cards */
        .room-card {
            border-radius: 16px;
            border: none;
            box-shadow: 0 2px 16px rgba(0,0,0,.07);
            overflow: hidden;
            transition: transform .2s, box-shadow .2s;
            background: #fff;
        }
        .room-card:hover {
            transform: translateY(-4px);
            box-shadow: 0 10px 32px rgba(0,0,0,.12);
        }
        .room-img-wrap {
            position: relative;
            height: 180px; overflow: hidden;
            background: linear-gradient(135deg, #e0e7ff 0%, #ede9fe 100%);
        }
        .room-img-wrap img {
            width: 100%; height: 100%; object-fit: cover;
            transition: transform .3s;
        }
        .room-img-wrap img[data-no-img] {
            display: none;
        }
        .room-img-wrap .room-placeholder {
            width: 100%; height: 100%;
            display: flex; flex-direction: column;
            align-items: center; justify-content: center;
            gap: .5rem; color: #818cf8;
        }
        .room-img-wrap .room-placeholder i { font-size: 3rem; }
        .room-img-wrap .room-placeholder span { font-size: .85rem; font-weight: 600; }
        .room-card:hover .room-img-wrap img { transform: scale(1.05); }

        .status-badge {
            position: absolute; top: 12px; right: 12px;
            border-radius: 20px;
            padding: 4px 12px;
            font-size: .75rem; font-weight: 700;
            text-transform: uppercase; letter-spacing: .5px;
        }
        .status-available   { background: #d1fae5; color: #059669; }
        .status-occupied    { background: #fee2e2; color: #dc2626; }
        .status-maintenance { background: #fef9c3; color: #d97706; }

        .room-body { padding: 1.2rem; }
        .room-number {
            font-weight: 700; font-size: 1.1rem; color: #1e1b4b;
            margin-bottom: .3rem;
        }
        .room-meta {
            font-size: .82rem; color: #9ca3af; margin-bottom: 1rem;
        }
        .btn-view {
            display: block; text-align: center;
            background: linear-gradient(135deg, #4f46e5, #7c3aed);
            color: #fff; border-radius: 10px;
            padding: 9px 0; font-weight: 600; font-size: .88rem;
            text-decoration: none; transition: opacity .2s;
        }
        .btn-view:hover { opacity: .9; color: #fff; }
        .btn-view.disabled {
            background: #e5e7eb; color: #9ca3af;
            pointer-events: none;
        }

        .empty-state {
            text-align: center; padding: 4rem 1rem; color: #9ca3af;
        }
        .empty-state i { font-size: 3rem; margin-bottom: 1rem; }
    </style>
</head>
<body>
    <%@ include file="../navbar.jsp" %>

    <%-- Hero --%>
    <div class="page-hero">
        <div class="container">
            <nav aria-label="breadcrumb" class="mb-3">
                <ol class="breadcrumb" style="--bs-breadcrumb-divider-color:rgba(255,255,255,.5)">
                    <li class="breadcrumb-item">
                        <a href="${pageContext.request.contextPath}/" class="text-white text-opacity-75">Home</a>
                    </li>
                    <li class="breadcrumb-item">
                        <a href="${pageContext.request.contextPath}/room?action=categories"
                           class="text-white text-opacity-75">Categories</a>
                    </li>
                    <li class="breadcrumb-item active text-white">Room List</li>
                </ol>
            </nav>
            <h1><i class="bi bi-door-closed me-2"></i>Room List</h1>
            <p class="mb-0 opacity-85">Find and explore all available rooms</p>
        </div>
    </div>

    <div class="container pb-5" style="padding-top: 48px;">

        <%-- Filter bar --%>
        <div class="d-flex justify-content-center mb-4">
            <div class="filter-bar">
                <a href="${pageContext.request.contextPath}/room?action=publicList"
                   class="filter-btn ${activeStatus == 'all' ? 'active' : ''}">
                    <i class="bi bi-grid"></i> All
                    <span class="filter-count">
                        ${(counts['available'] + counts['occupied'] + counts['maintenance'])}
                    </span>
                </a>
                <a href="${pageContext.request.contextPath}/room?action=publicList&status=available"
                   class="filter-btn ${activeStatus == 'available' ? 'active' : ''}">
                    <i class="bi bi-check-circle"></i> Available
                    <span class="filter-count">${counts['available']}</span>
                </a>
                <a href="${pageContext.request.contextPath}/room?action=publicList&status=occupied"
                   class="filter-btn ${activeStatus == 'occupied' ? 'active' : ''}">
                    <i class="bi bi-person-fill-check"></i> Occupied
                    <span class="filter-count">${counts['occupied']}</span>
                </a>
                <a href="${pageContext.request.contextPath}/room?action=publicList&status=maintenance"
                   class="filter-btn ${activeStatus == 'maintenance' ? 'active' : ''}">
                    <i class="bi bi-tools"></i> Maintenance
                    <span class="filter-count">${counts['maintenance']}</span>
                </a>
            </div>
        </div>

        <%-- Room grid --%>
        <c:choose>
            <c:when test="${empty rooms}">
                <div class="empty-state">
                    <i class="bi bi-door-open d-block"></i>
                    <h5>No rooms found</h5>
                    <p>There are no rooms matching the selected filter.</p>
                    <a href="${pageContext.request.contextPath}/room?action=publicList"
                       class="btn btn-primary rounded-pill px-4">View All Rooms</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="row g-4">
                    <c:forEach var="room" items="${rooms}">
                        <div class="col-sm-6 col-lg-4">
                            <div class="room-card">
                                <div class="room-img-wrap">
                                    <img src="${pageContext.request.contextPath}/assets/images/room/${not empty room.image ? room.image : 'default.jpg'}"
                                         alt="Room ${room.roomNumber}"
                                         onerror="this.onerror=null;this.setAttribute('data-no-img','1');this.parentElement.querySelector('.room-placeholder').style.display='flex';">
                                    <div class="room-placeholder" style="display:none;position:absolute;inset:0;">
                                        <i class="bi bi-door-closed"></i>
                                        <span>Room ${room.roomNumber}</span>
                                    </div>
                                    <span class="status-badge status-${room.status}">
                                        <c:choose>
                                            <c:when test="${room.status == 'available'}">Available</c:when>
                                            <c:when test="${room.status == 'occupied'}">Occupied</c:when>
                                            <c:otherwise>Maintenance</c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>
                                <div class="room-body">
                                    <div class="room-number">
                                        <i class="bi bi-door-closed me-1 text-primary"></i>
                                        Room ${room.roomNumber}
                                    </div>
                                    <div class="room-meta">
                                        <i class="bi bi-hash me-1"></i>ID: ${room.roomId}
                                    </div>
                                    <a href="${pageContext.request.contextPath}/room?action=publicDetail&id=${room.roomId}"
                                       class="btn-view ${room.status == 'maintenance' ? 'disabled' : ''}">
                                        <i class="bi bi-eye me-1"></i>
                                        ${room.status == 'maintenance' ? 'Unavailable' : 'View Details'}
                                    </a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>

    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
