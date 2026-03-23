<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:layout>

    <div class="container mt-4">

        <!-- TITLE -->
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h4 class="fw-bold text-primary">📄 Danh sách hóa đơn</h4>

            <a href="${pageContext.request.contextPath}/bill?action=create"
               class="btn btn-success">
                ➕ Tạo hóa đơn
            </a>
        </div>

        <!-- TABLE -->
        <div class="table-responsive shadow-sm">
            <table class="table table-bordered table-hover text-center align-middle">

                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Contract ID</th>
                        <th>Kỳ hóa đơn</th>
                        <th>Hạn thanh toán</th>
                        <th>Trạng thái</th>
                        <th>Hành động</th>
                    </tr>
                </thead>

                <tbody>

                    <c:choose>

                        <c:when test="${empty bills}">
                            <tr>
                                <td colspan="6" class="text-muted">
                                    Không có hóa đơn nào
                                </td>
                            </tr>
                        </c:when>

                        <c:otherwise>
                            <c:forEach var="b" items="${bills}">

                                <tr>
                                    <td>${b.billId}</td>

                                    <td>${b.contractId}</td>

                                    <!-- LocalDate -> in trực tiếp -->
                                    <td>${b.period}</td>

                                    <td>${b.dueDate}</td>

                                    <td>
                                        <c:choose>
                                            <c:when test="${b.status == 'paid'}">
                                                <span class="badge bg-success">Đã thanh toán</span>
                                            </c:when>
                                            <c:when test="${b.status == 'unpaid'}">
                                                <span class="badge bg-warning text-dark">Chưa thanh toán</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary">${b.status}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>

                                    <td>
                                        <!-- DETAIL -->
                                        <a href="${pageContext.request.contextPath}/bill?action=detail&id=${b.billId}"
                                           class="btn btn-info btn-sm">
                                            Chi tiết
                                        </a>

                                        <!-- EDIT -->
                                        <a href="${pageContext.request.contextPath}/bill?action=edit&id=${b.billId}"
                                           class="btn btn-warning btn-sm">
                                            Sửa
                                        </a>

                                        <!-- DELETE -->
                                        <a href="${pageContext.request.contextPath}/bill?action=delete&id=${b.billId}"
                                           class="btn btn-danger btn-sm"
                                           onclick="return confirm('Bạn có chắc muốn xóa?');">
                                            Xóa
                                        </a>
                                    </td>

                                </tr>

                            </c:forEach>
                        </c:otherwise>

                    </c:choose>

                </tbody>

            </table>
        </div>

    </div>

</t:layout>