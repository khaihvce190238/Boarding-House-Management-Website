<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<t:layout>

    <%-- Page header --%>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2 class="mb-0"><i class="bi bi-gear-wide-connected me-2 text-primary"></i>Manage Services</h2>
            <p class="text-muted mb-0 small">Add, edit, or remove services offered to tenants</p>
        </div>
        <div class="d-flex gap-2">
            <a href="${pageContext.request.contextPath}/services?action=requestList"
               class="btn btn-outline-info">
                <i class="bi bi-list-check me-1"></i>Service Request List
            </a>
            <a href="${pageContext.request.contextPath}/services?action=create"
               class="btn btn-primary">
                <i class="bi bi-plus-circle me-1"></i>Add Service
            </a>
        </div>
    </div>

    <%-- Stats bar --%>
    <div class="row g-3 mb-4">
        <div class="col-md-4">
            <div class="card border-0 shadow-sm">
                <div class="card-body d-flex align-items-center gap-3">
                    <div class="rounded-circle bg-primary bg-opacity-10 p-3">
                        <i class="bi bi-grid-3x3-gap-fill text-primary fs-4"></i>
                    </div>
                    <div>
                        <div class="fs-4 fw-bold">${services.size()}</div>
                        <div class="text-muted small">Total Services</div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <%-- Service table --%>
    <div class="card border-0 shadow-sm">
        <div class="card-header bg-white border-bottom py-3">
            <h6 class="mb-0 fw-semibold"><i class="bi bi-table me-2"></i>Service List</h6>
        </div>
        <div class="card-body p-0">
            <c:choose>
                <c:when test="${not empty services}">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th class="ps-4">#</th>
                                    <th>Service Name</th>
                                    <th>Category</th>
                                    <th>Description</th>
                                    <th class="text-center">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="svc" items="${services}" varStatus="st">
                                    <tr>
                                        <td class="ps-4 text-muted">${st.count}</td>
                                        <td>
                                            <div class="d-flex align-items-center gap-2">
                                                <div class="rounded-circle bg-primary bg-opacity-10 p-2">
                                                    <i class="bi bi-lightning-charge text-primary"></i>
                                                </div>
                                                <span class="fw-semibold">${svc.serviceName}</span>
                                            </div>
                                        </td>
                                        <td>
                                            <span class="badge bg-light text-dark border">
                                                Cat. ${svc.categoryId}
                                            </span>
                                        </td>
                                        <td class="text-muted small">
                                            <c:choose>
                                                <c:when test="${not empty svc.description}">
                                                    ${svc.description}
                                                </c:when>
                                                <c:otherwise>—</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="text-center">
                                            <div class="d-flex justify-content-center gap-1">
                                                <a href="${pageContext.request.contextPath}/services?action=edit&id=${svc.serviceId}"
                                                   class="btn btn-sm btn-outline-warning" title="Edit">
                                                    <i class="bi bi-pencil"></i>
                                                </a>
                                                <a href="${pageContext.request.contextPath}/services?action=delete&id=${svc.serviceId}"
                                                   class="btn btn-sm btn-outline-danger" title="Delete"
                                                   onclick="return confirm('Delete service: ${svc.serviceName}?')">
                                                    <i class="bi bi-trash"></i>
                                                </a>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="text-center py-5 text-muted">
                        <i class="bi bi-inbox fs-1 d-block mb-3 opacity-50"></i>
                        <p class="mb-3">No services found.</p>
                        <a href="${pageContext.request.contextPath}/services?action=create"
                           class="btn btn-primary">
                            <i class="bi bi-plus-circle me-1"></i>Add First Service
                        </a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

</t:layout>
