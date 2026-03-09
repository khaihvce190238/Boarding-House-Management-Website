<%-- 
    Document   : service
    Created on : Mar 4, 2026, 8:55:54 AM
    Author     : huudanh
--%>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:layout>

    <h2 class="mb-4">Services</h2>

    <div class="row">

        <c:forEach var="service" items="${services}">

            <div class="col-md-4 mb-4">

                <div class="card shadow-sm">

                    <img class="card-img-top"
                         src="${pageContext.request.contextPath}/assets/images/service/default.jpg"
                         alt="Service">

                    <div class="card-body">

                        <h5 class="card-title">
                            ${service.serviceName}
                        </h5>

                        <p class="card-text">
                            ${service.description}
                        </p>

                        <p class="card-text">
                            <strong>Category:</strong> ${service.categoryId}
                        </p>

                        <a href="${pageContext.request.contextPath}/services?action=detail&id=${service.serviceId}"
                           class="btn btn-primary">
                            View Detail
                        </a>

                    </div>

                </div>

            </div>

        </c:forEach>

    </div>

</t:layout>