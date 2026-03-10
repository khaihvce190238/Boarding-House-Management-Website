<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t"   tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<t:layout>

    <%-- Page header --%>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2 class="mb-0">
                <i class="bi bi-clipboard2-check me-2 text-primary"></i>Manage Service Requests
            </h2>
            <p class="text-muted mb-0 small">Review and approve/reject service requests from tenants</p>
        </div>
        <div class="d-flex gap-2">
            <a href="${pageContext.request.contextPath}/services?action=adminList"
               class="btn btn-outline-secondary btn-sm">
                <i class="bi bi-gear me-1"></i>Manage Services
            </a>
            <a href="${pageContext.request.contextPath}/services?action=requestList"
               class="btn btn-outline-info btn-sm">
                <i class="bi bi-list-check me-1"></i>All Usage
            </a>
        </div>
    </div>

    <%-- Stats cards --%>
    <div class="row g-3 mb-4">
        <div class="col-md-4">
            <div class="card border-0 shadow-sm h-100">
                <div class="card-body d-flex align-items-center gap-3">
                    <div class="rounded-circle p-3" style="background:#fef9c3">
                        <i class="bi bi-clock-fill fs-4" style="color:#ca8a04"></i>
                    </div>
                    <div>
                        <div class="fs-3 fw-bold">${pendingCount}</div>
                        <div class="text-muted small">Pending Review</div>
                    </div>
                    <a href="${pageContext.request.contextPath}/services?action=manageRequests&status=pending"
                       class="btn btn-sm btn-warning ms-auto">View</a>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card border-0 shadow-sm h-100">
                <div class="card-body d-flex align-items-center gap-3">
                    <div class="rounded-circle p-3" style="background:#d1fae5">
                        <i class="bi bi-check-circle-fill fs-4" style="color:#059669"></i>
                    </div>
                    <div>
                        <div class="fs-3 fw-bold">${approvedCount}</div>
                        <div class="text-muted small">Approved</div>
                    </div>
                    <a href="${pageContext.request.contextPath}/services?action=manageRequests&status=approved"
                       class="btn btn-sm btn-success ms-auto">View</a>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card border-0 shadow-sm h-100">
                <div class="card-body d-flex align-items-center gap-3">
                    <div class="rounded-circle p-3" style="background:#fee2e2">
                        <i class="bi bi-x-circle-fill fs-4" style="color:#dc2626"></i>
                    </div>
                    <div>
                        <div class="fs-3 fw-bold">${rejectedCount}</div>
                        <div class="text-muted small">Rejected</div>
                    </div>
                    <a href="${pageContext.request.contextPath}/services?action=manageRequests&status=rejected"
                       class="btn btn-sm btn-danger ms-auto">View</a>
                </div>
            </div>
        </div>
    </div>

    <%-- Filter tabs --%>
    <div class="card border-0 shadow-sm mb-0" style="border-radius:16px 16px 0 0;">
        <div class="card-body py-2 px-3 border-bottom">
            <div class="d-flex gap-1 flex-wrap">
                <a href="${pageContext.request.contextPath}/services?action=manageRequests"
                   class="btn btn-sm ${empty statusFilter ? 'btn-primary' : 'btn-outline-secondary'}">
                    <i class="bi bi-grid me-1"></i>All
                    <span class="badge ${empty statusFilter ? 'bg-white text-primary' : 'bg-secondary'} ms-1">
                        ${pendingCount + approvedCount + rejectedCount}
                    </span>
                </a>
                <a href="${pageContext.request.contextPath}/services?action=manageRequests&status=pending"
                   class="btn btn-sm ${statusFilter == 'pending' ? 'btn-warning' : 'btn-outline-warning'}">
                    <i class="bi bi-clock me-1"></i>Pending
                    <span class="badge bg-warning text-dark ms-1">${pendingCount}</span>
                </a>
                <a href="${pageContext.request.contextPath}/services?action=manageRequests&status=approved"
                   class="btn btn-sm ${statusFilter == 'approved' ? 'btn-success' : 'btn-outline-success'}">
                    <i class="bi bi-check-circle me-1"></i>Approved
                    <span class="badge bg-success ms-1">${approvedCount}</span>
                </a>
                <a href="${pageContext.request.contextPath}/services?action=manageRequests&status=rejected"
                   class="btn btn-sm ${statusFilter == 'rejected' ? 'btn-danger' : 'btn-outline-danger'}">
                    <i class="bi bi-x-circle me-1"></i>Rejected
                    <span class="badge bg-danger ms-1">${rejectedCount}</span>
                </a>
            </div>
        </div>
    </div>

    <%-- Requests table --%>
    <div class="card border-0 shadow-sm" style="border-radius:0 0 16px 16px;">
        <div class="card-body p-0">
            <c:choose>
                <c:when test="${not empty requestList}">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th class="ps-4">#</th>
                                    <th>Tenant</th>
                                    <th>Contract / Room</th>
                                    <th>Service</th>
                                    <th>Date</th>
                                    <th class="text-end">Qty</th>
                                    <th class="text-end">Est. Cost</th>
                                    <th class="text-center">Status</th>
                                    <th class="text-center pe-4">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="req" items="${requestList}" varStatus="st">
                                    <tr>
                                        <td class="ps-4 text-muted small">${st.count}</td>

                                        <%-- Tenant --%>
                                        <td>
                                            <div class="d-flex align-items-center gap-2">
                                                <div class="rounded-circle bg-primary bg-opacity-10 p-2">
                                                    <i class="bi bi-person text-primary small"></i>
                                                </div>
                                                <span class="fw-semibold small">
                                                    <c:choose>
                                                        <c:when test="${not empty req.requesterName}">${req.requesterName}</c:when>
                                                        <c:otherwise>—</c:otherwise>
                                                    </c:choose>
                                                </span>
                                            </div>
                                        </td>

                                        <%-- Contract / Room --%>
                                        <td>
                                            <div class="fw-semibold small">Contract #${req.contractId}</div>
                                            <div class="text-muted" style="font-size:.74rem">
                                                <i class="bi bi-door-closed me-1"></i>Room ${req.roomNumber}
                                            </div>
                                        </td>

                                        <%-- Service --%>
                                        <td>
                                            <span class="badge bg-info bg-opacity-10 text-info border border-info-subtle px-2 py-1">
                                                <i class="bi bi-lightning-charge me-1"></i>${req.serviceName}
                                            </span>
                                        </td>

                                        <%-- Date --%>
                                        <td class="text-muted small">${req.usageDate}</td>

                                        <%-- Qty --%>
                                        <td class="text-end small">${req.quantity}</td>

                                        <%-- Est. Cost --%>
                                        <td class="text-end fw-semibold small">
                                            <fmt:formatNumber value="${req.totalCost}"
                                                groupingUsed="true" maxFractionDigits="0"/>₫
                                        </td>

                                        <%-- Status badge --%>
                                        <td class="text-center">
                                            <c:choose>
                                                <c:when test="${req.status == 'pending'}">
                                                    <span class="badge rounded-pill bg-warning text-dark px-3 py-2">
                                                        <i class="bi bi-clock me-1"></i>Pending
                                                    </span>
                                                </c:when>
                                                <c:when test="${req.status == 'approved'}">
                                                    <span class="badge rounded-pill bg-success px-3 py-2">
                                                        <i class="bi bi-check-circle me-1"></i>Approved
                                                    </span>
                                                </c:when>
                                                <c:when test="${req.status == 'rejected'}">
                                                    <span class="badge rounded-pill bg-danger px-3 py-2">
                                                        <i class="bi bi-x-circle me-1"></i>Rejected
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge rounded-pill bg-secondary px-3 py-2">
                                                        ${req.status}
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>

                                        <%-- Actions --%>
                                        <td class="text-center pe-4">
                                            <c:choose>
                                                <c:when test="${req.status == 'pending'}">
                                                    <div class="d-flex justify-content-center gap-1">
                                                        <a href="${pageContext.request.contextPath}/services?action=approve&id=${req.usageId}&from=${statusFilter}"
                                                           class="btn btn-sm btn-success"
                                                           onclick="return confirm('Approve this service request?')"
                                                           title="Approve">
                                                            <i class="bi bi-check-lg me-1"></i>Approve
                                                        </a>
                                                        <a href="${pageContext.request.contextPath}/services?action=reject&id=${req.usageId}&from=${statusFilter}"
                                                           class="btn btn-sm btn-outline-danger"
                                                           onclick="return confirm('Reject this service request?')"
                                                           title="Reject">
                                                            <i class="bi bi-x-lg"></i>
                                                        </a>
                                                    </div>
                                                </c:when>
                                                <c:when test="${req.status == 'approved'}">
                                                    <a href="${pageContext.request.contextPath}/services?action=reject&id=${req.usageId}&from=${statusFilter}"
                                                       class="btn btn-sm btn-outline-danger"
                                                       onclick="return confirm('Revoke approval for this request?')"
                                                       title="Revoke">
                                                        <i class="bi bi-arrow-counterclockwise me-1"></i>Revoke
                                                    </a>
                                                </c:when>
                                                <c:when test="${req.status == 'rejected'}">
                                                    <a href="${pageContext.request.contextPath}/services?action=approve&id=${req.usageId}&from=${statusFilter}"
                                                       class="btn btn-sm btn-outline-success"
                                                       onclick="return confirm('Re-approve this request?')"
                                                       title="Re-approve">
                                                        <i class="bi bi-arrow-counterclockwise me-1"></i>Re-approve
                                                    </a>
                                                </c:when>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="px-4 py-3 text-muted small border-top">
                        Showing <strong>${requestList.size()}</strong> record(s)
                        <c:if test="${not empty statusFilter}"> — filtered by: <strong>${statusFilter}</strong></c:if>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="text-center py-5 text-muted">
                        <i class="bi bi-inbox fs-1 d-block mb-3 opacity-40"></i>
                        <h6 class="fw-semibold">No requests found</h6>
                        <c:if test="${not empty statusFilter}">
                            <p class="small">No <strong>${statusFilter}</strong> requests at this time.</p>
                            <a href="${pageContext.request.contextPath}/services?action=manageRequests"
                               class="btn btn-sm btn-outline-primary">View All</a>
                        </c:if>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

</t:layout>
