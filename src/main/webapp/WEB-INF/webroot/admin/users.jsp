<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="users" type="java.util.List<com.hicks.beans.User>" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <title>BTS</title>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/webroot/inc_header.jsp"/>
</head>
<body>

<jsp:include page="${pageContext.request.contextPath}/WEB-INF/webroot/header.jsp"/>

<div class="mdl-grid">

    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>Manage Users</h5></div>

        <div class="tableContainer">
            <table class="mdl-data-table mdl-js-data-table mdl-data-table--selectable mdl-shadow--2dp">
                <thead>
                    <tr>
                        <th>Object Id</th>
                        <th>Logon Id</th>
                    </tr>
                </thead>
                <c:forEach var="user" items="${users}">
                    <tr>
                        <td><a href="${pageContext.request.contextPath}/view?tab1=main&tab2=profile&action=form&userId=${user.id}">${user.id}</a></td>
                        <td><a href="${pageContext.request.contextPath}/view?tab1=main&tab2=profile&action=form&userId=${user.id}">${user.logonId}</a></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>

<jsp:include page="${pageContext.request.contextPath}/WEB-INF/webroot/footer.jsp"/>
</body>
</html>