<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:layout>

<div class="container mt-4">
    <h4 class="fw-bold text-warning mb-3">✏️ Cập nhật hóa đơn</h4>

    <form action="${pageContext.request.contextPath}/bill" method="post">

        <input type="hidden" name="action" value="update">
        <input type="hidden" name="billId" value="${bill.billId}">

        <div class="mb-3">
            <label>Contract ID</label>
            <input type="number" name="contractId"
                   class="form-control"
                   value="${bill.contractId}" required>
        </div>

        <div class="mb-3">
            <label>Kỳ hóa đơn</label>
            <input type="date" name="period"
                   class="form-control"
                   value="${bill.period}" required>
        </div>

        <div class="mb-3">
            <label>Hạn thanh toán</label>
            <input type="date" name="dueDate"
                   class="form-control"
                   value="${bill.dueDate}" required>
        </div>

        <div class="mb-3">
            <label>Trạng thái</label>
            <select name="status" class="form-select">

                <option value="unpaid"
                    <c:if test="${bill.status == 'unpaid'}">selected</c:if>>
                    Chưa thanh toán
                </option>

                <option value="paid"
                    <c:if test="${bill.status == 'paid'}">selected</c:if>>
                    Đã thanh toán
                </option>

            </select>
        </div>

        <button class="btn btn-warning">Cập nhật</button>
        <a href="${pageContext.request.contextPath}/bill?action=list"
           class="btn btn-secondary">Hủy</a>

    </form>
</div>

</t:layout>