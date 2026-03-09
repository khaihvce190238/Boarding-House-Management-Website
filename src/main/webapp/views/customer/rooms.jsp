<%-- 
    Document   : roomList
    Created on : Mar 4, 2026, 8:52:31 AM
    Author     : huudanh
--%>


<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:layout>

    <h2 class="mb-4">Room List</h2>

    <div class="row">

        <c:forEach var="room" items="${rooms}">

            <div class="col-md-4 mb-4">

                <div class="card shadow-sm">

                    <img class="card-img-top"
                         src="${pageContext.request.contextPath}/assets/images/room/default.jpg"
                         alt="Room">

                    <div class="card-body">

                        <h5 class="card-title">
                            ${room.roomNumber}
                        </h5>

                        <p class="card-text">
                            Capacity: ${room.capacity}
                        </p>

                        <p class="card-text">
                            Price: ${room.price} VND
                        </p>

                        <a href="${pageContext.request.contextPath}/room?action=detail&id=${room.roomId}"
                           class="btn btn-primary">
                            View Detail
                        </a>

                    </div>

                </div>

            </div>

        </c:forEach>

    </div>

</t:layout>