<%-- 
    Document   : profile
    Created on : Mar 4, 2026, 8:56:16 AM
    Author     : huuda
--%>


<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:layout>

<h2 class="mb-4">User Profile</h2>

<div class="row justify-content-center">

    <div class="col-md-6">

        <div class="card shadow">

            <div class="card-body">

                <div class="text-center mb-3">
                    <img src="${pageContext.request.contextPath}/assets/images/user/avatar.png"
                         class="rounded-circle"
                         width="120">
                </div>

                <form action="${pageContext.request.contextPath}/user" method="post">

                    <input type="hidden" name="action" value="updateProfile"/>

                    <div class="mb-3">
                        <label class="form-label">Full Name</label>
                        <input type="text"
                               name="fullName"
                               class="form-control"
                               value="${sessionScope.user.fullName}"
                               required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Username</label>
                        <input type="text"
                               class="form-control"
                               value="${sessionScope.user.username}"
                               readonly>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Email</label>
                        <input type="email"
                               name="email"
                               class="form-control"
                               value="${sessionScope.user.email}">
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Phone</label>
                        <input type="text"
                               name="phone"
                               class="form-control"
                               value="${sessionScope.user.phone}">
                    </div>

                    <div class="d-grid">
                        <button type="submit" class="btn btn-primary">
                            Update Profile
                        </button>
                    </div>

                </form>

            </div>

        </div>

    </div>

</div>

</t:layout>