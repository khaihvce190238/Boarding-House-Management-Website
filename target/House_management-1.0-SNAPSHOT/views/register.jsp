<%-- 
    Document   : register
    Created on : Mar 4, 2026, 7:26:58 AM
    Author     : huuda
--%>


<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:layout>
    <form action="auth" method="post">

        <input type="hidden" name="action" value="register">

        Username:
        <input type="text" name="username">

        Password:
        <input type="password" name="password">

        Full Name:
        <input type="text" name="fullName">

        Email:
        <input type="email" name="email">

        Phone:
        <input type="text" name="phone">

        <button type="submit">Register</button>

    </form>


</t:layout>