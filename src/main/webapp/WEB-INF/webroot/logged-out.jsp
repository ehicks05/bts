<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: eric
  Date: 7/7/2016
  Time: 11:00 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>BTS</title>
    <jsp:include page="inc_header.jsp"/>
</head>
<body>
<jsp:include page="header.jsp"/>

<div class="mdl-grid">
    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-shadow--2dp">

        <c:if test="${empty clientMessage}">
            <c:set var="clientMessage" value="You are logged out!"/>
        </c:if>
        <div class="mdl-card__title"><h5>${clientMessage}</h5></div>

        <div class="mdl-card__actions">
            <input type="submit" value="Back" class="mdl-button mdl-js-button mdl-button--raised" onclick="location.href='${pageContext.request.contextPath}/view?tab1=main&action=form';"/>
        </div>
    </div>
</div>

</body>
</html>
