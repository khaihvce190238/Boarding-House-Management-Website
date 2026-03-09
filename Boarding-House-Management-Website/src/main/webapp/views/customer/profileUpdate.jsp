<%-- 
    Document   : upprofileUpdate
    Created on : Mar 9, 2026, 2:22:07 AM
    Author     : huuda
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<form action="customer" method="post">

    <input type="hidden" name="action" value="update">

    Full Name:
    <input type="text" name="fullName" value="${user.fullName}">

    Email:
    <input type="email" name="email" value="${user.email}">

    Phone:
    <input type="text" name="phone" value="${user.phone}">

    Image:
    <input type="text" name="image" value="${user.image}">

    <button type="submit">Update</button>

</form>
