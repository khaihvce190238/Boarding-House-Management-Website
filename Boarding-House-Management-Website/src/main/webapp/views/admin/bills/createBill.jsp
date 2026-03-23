<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout>

<div class="container mt-4">
    <h4 class="fw-bold text-primary mb-3">➕ Tạo hóa đơn</h4>

    <form action="${pageContext.request.contextPath}/bill" method="post">
        <input type="hidden" name="action" value="create">

        <div class="mb-3">
            <label class="form-label">Contract ID</label>
            <input type="number" name="contractId" class="form-control" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Kỳ hóa đơn</label>
            <input type="date" name="period" class="form-control" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Hạn thanh toán</label>
            <input type="date" name="dueDate" class="form-control" required>
        </div>

        <button type="submit" class="btn btn-success">Tạo</button>
        <a href="${pageContext.request.contextPath}/bill?action=list" 
           class="btn btn-secondary">Hủy</a>
    </form>
</div>

</t:layout>