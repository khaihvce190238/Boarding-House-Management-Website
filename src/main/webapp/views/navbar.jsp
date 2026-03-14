<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark shadow">

    <div class="container">

        <!-- LOGO -->
        <a class="navbar-brand d-flex align-items-center"
           href="${pageContext.request.contextPath}/">

            <img src="${pageContext.request.contextPath}/assets/images/room/logo.png"
                 width="40"
                 class="me-2">

            <strong>AKDD House</strong>
        </a>

        <!-- MOBILE BUTTON -->
        <button class="navbar-toggler"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#navbarMain">

            <span class="navbar-toggler-icon"></span>
        </button>

        <!-- MENU -->
        <div class="collapse navbar-collapse" id="navbarMain">

            <ul class="navbar-nav me-auto">

                <li class="nav-item">
                    <a class="nav-link"
                       href="${pageContext.request.contextPath}/">
                        Home
                    </a>
                </li>

                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="${pageContext.request.contextPath}/room?action=categories" data-bs-toggle="dropdown">
                        Rooms
                    </a>
                    <ul class="dropdown-menu">
                        <li>
                            <a class="dropdown-item"
                               href="${pageContext.request.contextPath}/room?action=categories">
                                <i class="bi bi-grid-3x3-gap me-2"></i>Room Categories
                            </a>
                        </li>
                        <li>
                            <a class="dropdown-item"
                               href="${pageContext.request.contextPath}/room?action=publicList">
                                <i class="bi bi-list-ul me-2"></i>All Rooms
                            </a>
                        </li>
                        <li>
                            <a class="dropdown-item"
                               href="${pageContext.request.contextPath}/room?action=publicList&status=available">
                                <i class="bi bi-check-circle me-2 text-success"></i>Available Rooms
                            </a>
                        </li>
                    </ul>
                </li>

                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle"
                       href="${pageContext.request.contextPath}/services"
                       data-bs-toggle="dropdown">
                        Services
                    </a>
                    <ul class="dropdown-menu">
                        <li>
                            <a class="dropdown-item"
                               href="${pageContext.request.contextPath}/services">
                                <i class="bi bi-grid-3x3-gap me-2"></i>All Services
                            </a>
                        </li>
                        <c:if test="${not empty sessionScope.user}">
                            <li>
                                <a class="dropdown-item"
                                   href="${pageContext.request.contextPath}/services?action=myHistory">
                                    <i class="bi bi-clock-history me-2"></i>My Service History
                                </a>
                            </li>
                        </c:if>
                        <c:if test="${sessionScope.user.role == 'admin' or sessionScope.user.role == 'staff'}">
                            <li><hr class="dropdown-divider"></li>
                            <li>
                                <a class="dropdown-item"
                                   href="${pageContext.request.contextPath}/services?action=adminList">
                                    <i class="bi bi-gear me-2"></i>Manage Services
                                </a>
                            </li>
                            <li>
                                <a class="dropdown-item"
                                   href="${pageContext.request.contextPath}/services?action=manageRequests">
                                    <i class="bi bi-clipboard2-check me-2"></i>Manage Requests
                                </a>
                            </li>
                            <li>
                                <a class="dropdown-item"
                                   href="${pageContext.request.contextPath}/services?action=requestList">
                                    <i class="bi bi-list-check me-2"></i>All Usage Records
                                </a>
                            </li>
                        </c:if>
                    </ul>
                </li>

                <c:if test="${sessionScope.user.role == 'admin' or sessionScope.user.role == 'staff'}">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" data-bs-toggle="dropdown">
                        <i class="bi bi-tools me-1"></i>Management
                    </a>
                    <ul class="dropdown-menu">
                        <li>
                            <h6 class="dropdown-header">
                                <i class="bi bi-lightning-charge-fill text-warning me-1"></i>Utilities
                            </h6>
                        </li>
                        <li>
                            <a class="dropdown-item"
                               href="${pageContext.request.contextPath}/utility">
                                <i class="bi bi-list-ul me-2"></i>Manage Utilities
                            </a>
                        </li>
                        <li><hr class="dropdown-divider"></li>
                        <li>
                            <h6 class="dropdown-header">
                                <i class="bi bi-stars text-info me-1"></i>Amenities
                            </h6>
                        </li>
                        <li>
                            <a class="dropdown-item"
                               href="${pageContext.request.contextPath}/amenity">
                                <i class="bi bi-list-ul me-2"></i>Manage Amenities
                            </a>
                        </li>
                        <li><hr class="dropdown-divider"></li>
                        <li>
                            <a class="dropdown-item"
                               href="${pageContext.request.contextPath}/facility">
                                <i class="bi bi-wrench me-2"></i>Manage Facilities
                            </a>
                        </li>
                        <li><hr class="dropdown-divider"></li>
                        <li>
                            <h6 class="dropdown-header">
                                <i class="bi bi-file-earmark-text-fill text-primary me-1"></i>Contracts
                            </h6>
                        </li>
                        <li>
                            <a class="dropdown-item"
                               href="${pageContext.request.contextPath}/contract?action=list">
                                <i class="bi bi-list-ul me-2"></i>Manage Contracts
                            </a>
                        </li>
                        <li>
                            <a class="dropdown-item"
                               href="${pageContext.request.contextPath}/contract?action=create">
                                <i class="bi bi-file-earmark-plus me-2"></i>New Contract
                            </a>
                        </li>
                        <li><hr class="dropdown-divider"></li>
                        <li>
                            <h6 class="dropdown-header">
                                <i class="bi bi-people-fill text-success me-1"></i>Customers
                            </h6>
                        </li>
                        <li>
                            <a class="dropdown-item"
                               href="${pageContext.request.contextPath}/manage-customer">
                                <i class="bi bi-person-lines-fill me-2"></i>Manage Customers
                            </a>
                        </li>
                        <li>
                            <a class="dropdown-item"
                               href="${pageContext.request.contextPath}/activity-log">
                                <i class="bi bi-activity me-2"></i>Activity Logs
                            </a>
                        </li>
                    </ul>
                </li>
                </c:if>

                <li class="nav-item">
                    <a class="nav-link"
                       href="${pageContext.request.contextPath}/contact">
                        Contact
                    </a>
                </li>

                <c:if test="${not empty sessionScope.user}">
                <li class="nav-item">
                    <a class="nav-link d-flex align-items-center gap-1"
                       href="${pageContext.request.contextPath}/notification?action=publicList">
                        <i class="bi bi-bell-fill"></i>
                        Notifications
                    </a>
                </li>
                </c:if>

            </ul>

            <!-- USER MENU -->
            <ul class="navbar-nav">

                <c:choose>

                    <c:when test="${not empty sessionScope.user}">

                        <li class="nav-item dropdown">

                            <a class="nav-link dropdown-toggle d-flex align-items-center"
                               href="#"
                               data-bs-toggle="dropdown">

                                <img src="${pageContext.request.contextPath}/assets/images/user/avatar.png"
                                     width="32"
                                     height="32"
                                     class="rounded-circle me-2">

                                ${sessionScope.user.fullName}

                            </a>

                            <ul class="dropdown-menu dropdown-menu-end">

                                <li>
                                    <a class="dropdown-item"
                                       href="${pageContext.request.contextPath}/customer?action=profile">
                                        <i class="bi bi-person me-2"></i>My Profile
                                    </a>
                                </li>

                                <%-- Manage Users: visible to admin/staff only --%>
                                <c:if test="${sessionScope.user.role == 'admin' or sessionScope.user.role == 'staff'}">
                                <li>
                                    <a class="dropdown-item"
                                       href="${pageContext.request.contextPath}/user?action=list">
                                        <i class="bi bi-people me-2"></i>Manage Users
                                    </a>
                                </li>
                                </c:if>

                                <li>
                                    <a class="dropdown-item"
                                       href="${pageContext.request.contextPath}/room?action=mybooking">
                                        My Room
                                    </a>
                                </li>

                                <li>
                                    <a class="dropdown-item"
                                       href="${pageContext.request.contextPath}/contract?action=mycontract">
                                        <i class="bi bi-file-earmark-text me-2"></i>My Contracts
                                    </a>
                                </li>
                                <c:if test="${sessionScope.user.role == 'customer'}">
                                <li>
                                    <a class="dropdown-item"
                                       href="${pageContext.request.contextPath}/contract?action=signContract">
                                        <i class="bi bi-pen me-2"></i>Sign a Contract
                                    </a>
                                </li>
                                </c:if>

                                <li>
                                    <a class="dropdown-item"
                                       href="${pageContext.request.contextPath}/bill?action=mybill">
                                        My Bill
                                    </a>
                                </li>

                                <li>
                                    <a class="dropdown-item"
                                       href="${pageContext.request.contextPath}/auth?action=changePassword">
                                        Change Password
                                    </a>
                                </li>

                                <li><hr class="dropdown-divider"></li>

                                <li>
                                    <a class="dropdown-item text-danger"
                                       href="${pageContext.request.contextPath}/auth?action=logout">
                                        Logout
                                    </a>
                                </li>

                            </ul>

                        </li>

                    </c:when>

                    <c:otherwise>

                        <li class="nav-item">
                            <a class="btn btn-outline-light me-2"
                               href="${pageContext.request.contextPath}/auth?action=login">
                                Login
                            </a>
                        </li>

                        <li class="nav-item">
                            <a class="btn btn-primary"
                               href="${pageContext.request.contextPath}/auth?action=register">
                                Register
                            </a>
                        </li>

                    </c:otherwise>

                </c:choose>

            </ul>

        </div>

    </div>

</nav>