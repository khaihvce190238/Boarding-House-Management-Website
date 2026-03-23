<%-- 
    Document   : facility.jsp
    Created on : Mar 8, 2026, 9:27:36 PM
    Author     : huuda
--%>


<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:layout>

<h2 class="mb-4">Room Facilities</h2>

<div class="row">

<c:forEach var="facility" items="${facilities}">

    <div class="col-md-4 mb-4">

        <div class="card shadow-sm">

            <img class="card-img-top"
                 src="${pageContext.request.contextPath}/assets/images/facility/default.png"
                 alt="Facility">

            <div class="card-body">

                <h5 class="card-title">
                    ${facility.facilityName}
                </h5>

                <p class="card-text">
                    ${facility.description}
                </p>

                <div class="d-flex justify-content-between">

                    <a href="${pageContext.request.contextPath}/facility?action=edit&id=${facility.facilityId}"
                       class="btn btn-warning btn-sm">
                        Edit
                    </a>

                    <a href="${pageContext.request.contextPath}/facility?action=delete&id=${facility.facilityId}"
                       class="btn btn-danger btn-sm"
                       onclick="return confirm('Are you sure?')">
                        Delete
                    </a>

                </div>

            </div>

        </div>

    </div>

</c:forEach>

</div>

<div class="mt-3">
    <a href="${pageContext.request.contextPath}/facility?action=create"
       class="btn btn-primary">
        Add New Facility
    </a>
</div>

</t:layout>