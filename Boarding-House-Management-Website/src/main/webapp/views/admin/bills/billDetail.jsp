<%-- 
    Document   : billDetail
    Created on : Mar 4, 2026, 8:45:33 AM
    Author     : huuda
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<t:layout>

<div class="container mt-4">
    <h4 class="fw-bold text-info mb-3">📄 Chi tiết hóa đơn</h4>

    <div class="card shadow-sm">
        <div class="card-body">

            <p><strong>ID:</strong> ${bill.billId}</p>

            <p><strong>Contract ID:</strong> ${bill.contractId}</p>

            <p>
                <strong>Kỳ hóa đơn:</strong>
                <fmt:formatDate value="${bill.period}" pattern="yyyy-MM-dd"/>
            </p>

            <p>
                <strong>Hạn thanh toán:</strong>
                <fmt:formatDate value="${bill.dueDate}" pattern="yyyy-MM-dd"/>
            </p>

            <p>
                <strong>Trạng thái:</strong>
                <c:choose>
                    <c:when test="${bill.status == 'paid'}">
                        <span class="badge bg-success">Đã thanh toán</span>
                    </c:when>
                    <c:otherwise>
                        <span class="badge bg-warning text-dark">Chưa thanh toán</span>
                    </c:otherwise>
                </c:choose>
            </p>

        </div>
    </div>

    <div class="mt-3">
        <a href="${pageContext.request.contextPath}/bill?action=list" 
           class="btn btn-secondary">⬅ Quay lại</a>
    </div>

</div>

</t:layout>
