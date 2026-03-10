<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Room ${room.roomNumber} - AKDD House</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        body { background: #f4f6f9; font-family: 'Inter', sans-serif; }

        /* ── Hero image ── */
        .room-hero {
            width: 100%;
            height: 320px;
            object-fit: cover;
            border-radius: 0 0 24px 24px;
            display: block;
        }
        .hero-wrap { position: relative; background: #1e1b4b; }
        .hero-overlay {
            position: absolute; inset: 0;
            background: linear-gradient(to top, rgba(30,27,75,.85) 0%, transparent 60%);
            border-radius: 0 0 24px 24px;
        }
        .hero-info {
            position: absolute; bottom: 24px; left: 32px; right: 32px;
            color: #fff;
        }
        .hero-info h2 { font-weight: 700; font-size: 1.8rem; margin-bottom: .4rem; }

        /* ── Status badge ── */
        .status-pill {
            display: inline-block; border-radius: 20px;
            padding: 5px 16px; font-weight: 700;
            font-size: .8rem; text-transform: uppercase; letter-spacing: .5px;
        }
        .s-available   { background:#d1fae5; color:#059669; }
        .s-occupied    { background:#fee2e2; color:#dc2626; }
        .s-maintenance { background:#fef9c3; color:#d97706; }

        /* ── Section cards ── */
        .info-card {
            background: #fff;
            border-radius: 16px;
            border: none;
            box-shadow: 0 2px 16px rgba(0,0,0,.07);
            padding: 1.5rem;
        }
        .section-title {
            font-weight: 700; font-size: 1rem; color: #1e1b4b;
            display: flex; align-items: center; gap: .6rem;
            margin-bottom: 1.25rem;
            padding-bottom: .75rem;
            border-bottom: 2px solid #f0f0f0;
        }
        .section-title i { color: #7c3aed; font-size: 1.1rem; }

        /* ── Info row ── */
        .info-row {
            display: flex; align-items: center;
            padding: 10px 0;
            border-bottom: 1px solid #f9f9f9;
            font-size: .9rem;
        }
        .info-row:last-child { border-bottom: none; }
        .info-key { width: 130px; flex-shrink: 0; color: #9ca3af; font-weight: 600; font-size: .8rem; text-transform: uppercase; letter-spacing: .3px; }
        .info-val { color: #1f2937; font-weight: 500; }

        /* ── Amenity cards ── */
        .amenity-card {
            border-radius: 12px;
            border: 1.5px solid #e5e7eb;
            padding: 1rem;
            display: flex; align-items: flex-start; gap: .85rem;
            transition: border-color .2s, box-shadow .2s;
            background: #fff;
        }
        .amenity-card:hover { border-color: #7c3aed; box-shadow: 0 4px 16px rgba(124,58,237,.1); }
        .amenity-icon-wrap {
            width: 44px; height: 44px; flex-shrink: 0;
            background: #ede9fe;
            border-radius: 10px;
            display: flex; align-items: center; justify-content: center;
        }
        .amenity-icon-wrap img {
            width: 28px; height: 28px; object-fit: cover; border-radius: 6px;
        }
        .amenity-name { font-weight: 600; font-size: .9rem; color: #1f2937; }
        .amenity-desc { font-size: .8rem; color: #6b7280; margin-top: 2px; }
        .amenity-qty  {
            margin-left: auto; flex-shrink: 0;
            background: #ede9fe; color: #7c3aed;
            border-radius: 8px; padding: 3px 10px;
            font-size: .78rem; font-weight: 700;
        }

        .empty-amenity {
            text-align: center; padding: 2.5rem; color: #9ca3af;
        }
        .empty-amenity i { font-size: 2.5rem; margin-bottom: .75rem; }

        /* ── Back + action buttons ── */
        .btn-back {
            display: inline-flex; align-items: center; gap: .4rem;
            color: #7c3aed; font-weight: 600; font-size: .88rem;
            text-decoration: none; margin-bottom: 1.25rem;
        }
        .btn-back:hover { text-decoration: underline; color: #6d28d9; }
    </style>
</head>
<body>
    <%@ include file="../navbar.jsp" %>

    <c:if test="${empty room}">
        <div class="container mt-5 text-center">
            <i class="bi bi-exclamation-circle text-danger" style="font-size:3rem"></i>
            <h4 class="mt-3">Room not found</h4>
            <a href="${pageContext.request.contextPath}/room?action=publicList" class="btn btn-primary mt-2">
                Back to Room List
            </a>
        </div>
    </c:if>

    <c:if test="${not empty room}">

        <%-- Hero image --%>
        <div class="hero-wrap">
            <img class="room-hero"
                 src="${pageContext.request.contextPath}/assets/images/room/${not empty room.image ? room.image : 'default.jpg'}"
                 alt="Room ${room.roomNumber}"
                 onerror="this.onerror=null;this.style.display='none';this.parentElement.style.background='linear-gradient(135deg,#4f46e5,#7c3aed)'">
            <div class="hero-overlay"></div>
            <div class="hero-info">
                <div class="mb-2">
                    <span class="status-pill s-${room.status}">
                        <c:choose>
                            <c:when test="${room.status == 'available'}">Available</c:when>
                            <c:when test="${room.status == 'occupied'}">Occupied</c:when>
                            <c:otherwise>Maintenance</c:otherwise>
                        </c:choose>
                    </span>
                </div>
                <h2><i class="bi bi-door-closed me-2"></i>Room ${room.roomNumber}</h2>
            </div>
        </div>

        <div class="container py-4" style="max-width: 860px;">

            <a href="${pageContext.request.contextPath}/room?action=publicList
                <c:if test="${not empty room and room.categoryId > 0}">?categoryId=${room.categoryId}</c:if>"
               class="btn-back">
                <i class="bi bi-arrow-left"></i> Back to Room List
            </a>

            <div class="row g-4">

                <%-- Left: Room info ─────────────────────────────────── --%>
                <div class="col-md-5">
                    <div class="info-card">
                        <div class="section-title">
                            <i class="bi bi-info-circle-fill"></i> Room Information
                        </div>
                        <div class="info-row">
                            <div class="info-key">Room No.</div>
                            <div class="info-val fw-bold">${room.roomNumber}</div>
                        </div>
                        <div class="info-row">
                            <div class="info-key">Status</div>
                            <div class="info-val">
                                <span class="status-pill s-${room.status}" style="font-size:.75rem;padding:3px 12px;">
                                    <c:choose>
                                        <c:when test="${room.status == 'available'}">
                                            <i class="bi bi-check-circle me-1"></i>Available
                                        </c:when>
                                        <c:when test="${room.status == 'occupied'}">
                                            <i class="bi bi-person-fill-check me-1"></i>Occupied
                                        </c:when>
                                        <c:otherwise>
                                            <i class="bi bi-tools me-1"></i>Maintenance
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                            </div>
                        </div>
                        <c:if test="${not empty room.categoryName}">
                        <div class="info-row">
                            <div class="info-key">Type</div>
                            <div class="info-val">
                                <span style="background:#ede9fe;color:#6d28d9;border-radius:8px;padding:2px 10px;font-size:.8rem;font-weight:600;">
                                    <i class="bi bi-tag me-1"></i>${room.categoryName}
                                </span>
                            </div>
                        </div>
                        </c:if>
                        <c:if test="${not empty room.basePrice and room.basePrice > 0}">
                        <div class="info-row">
                            <div class="info-key">Base Price</div>
                            <div class="info-val fw-bold" style="color:#4f46e5">
                                <fmt:formatNumber value="${room.basePrice}" groupingUsed="true" maxFractionDigits="0"/>₫
                                <span class="text-muted fw-normal" style="font-size:.8rem"> / month</span>
                            </div>
                        </div>
                        </c:if>
                        <div class="info-row">
                            <div class="info-key">Room ID</div>
                            <div class="info-val text-muted">#${room.roomId}</div>
                        </div>
                    </div>

                    <%-- Contact / action card --%>
                    <div class="info-card mt-4">
                        <div class="section-title">
                            <i class="bi bi-lightning-fill"></i> Quick Actions
                        </div>
                        <c:choose>
                            <c:when test="${room.status == 'available'}">
                                <p class="text-muted small mb-3">This room is available. Contact us to arrange a viewing or sign a contract.</p>
                                <a href="${pageContext.request.contextPath}/contract?action=create&roomId=${room.roomId}"
                                   class="btn w-100 fw-semibold mb-2"
                                   style="background:linear-gradient(135deg,#4f46e5,#7c3aed);color:#fff;border:none;border-radius:10px;height:42px">
                                    <i class="bi bi-file-earmark-text me-1"></i> Sign Contract
                                </a>
                            </c:when>
                            <c:when test="${room.status == 'occupied'}">
                                <p class="text-muted small mb-2">This room is currently occupied.</p>
                                <div class="alert alert-warning py-2 small rounded-3 mb-0">
                                    <i class="bi bi-clock me-1"></i> Check back later for availability.
                                </div>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted small mb-2">This room is temporarily unavailable.</p>
                                <div class="alert alert-secondary py-2 small rounded-3 mb-0">
                                    <i class="bi bi-tools me-1"></i> Under maintenance.
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <%-- Right: Amenities ────────────────────────────────── --%>
                <div class="col-md-7">
                    <div class="info-card">
                        <div class="section-title">
                            <i class="bi bi-stars"></i>
                            Room Amenities
                            <span class="badge ms-auto rounded-pill"
                                  style="background:#ede9fe;color:#7c3aed;font-size:.78rem">
                                ${amenities.size()} items
                            </span>
                        </div>

                        <c:choose>
                            <c:when test="${empty amenities}">
                                <div class="empty-amenity">
                                    <i class="bi bi-inbox d-block"></i>
                                    <p class="mb-0">No amenities listed for this room yet.</p>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="d-flex flex-column gap-3">
                                    <c:forEach var="a" items="${amenities}">
                                        <div class="amenity-card">
                                            <div class="amenity-icon-wrap">
                                                <c:choose>
                                                    <c:when test="${not empty a.image}">
                                                        <img src="${pageContext.request.contextPath}/assets/images/facility/${a.image}"
                                                             alt="${a.facilityName}"
                                                             onerror="this.style.display='none';this.parentElement.innerHTML='<i class=\'bi bi-box\' style=\'color:#7c3aed;font-size:1.2rem\'></i>'">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <i class="bi bi-box" style="color:#7c3aed;font-size:1.2rem"></i>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div class="flex-grow-1">
                                                <div class="amenity-name">${a.facilityName}</div>
                                                <c:if test="${not empty a.description}">
                                                    <div class="amenity-desc">${a.description}</div>
                                                </c:if>
                                            </div>
                                            <div class="amenity-qty">×${a.quantity}</div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

            </div>
        </div>

    </c:if>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
