<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="userSession" type="com.hicks.UserSession" scope="session"/>

<!DOCTYPE html>
<html>
<head>
    <title>BTS</title>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/webroot/inc_header.jsp"/>

    <script>
        $('document').ready(function ()
        {
            $('#clearCache').click(function ()
            {
                location.href = '${pageContext.request.contextPath}/view?tab1=admin&tab2=cache&action=clearCache';
            });
        });
    </script>
</head>
<body>

<jsp:include page="${pageContext.request.contextPath}/WEB-INF/webroot/header.jsp"/>

<div class="mdl-grid">

    <div class="mdl-card mdl-cell mdl-cell--6-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>System Info</h5></div>

        <div class=".mdl-card__supporting-text" style="overflow: auto;">
            <div class="tableContainer">
                <table class="mdl-data-table mdl-shadow--2dp">
                    <thead>
                    <tr>
                        <th class="mdl-data-table__cell--non-numeric">Property</th>
                        <th>Value</th>
                    </tr>
                    </thead>
                    <tr>
                        <td class="mdl-data-table__cell--non-numeric">Start Time</td>
                        <td>${userSession.systemInfo.systemStartTime}</td>
                    </tr>
                    <tr>
                        <td class="mdl-data-table__cell--non-numeric">DB Cache</td>
                        <td>${userSession.systemInfo.databaseCache}</td>
                    </tr>
                    <tr>
                        <td class="mdl-data-table__cell--non-numeric">Used RAM</td>
                        <td>${userSession.systemInfo.usedRam}</td>
                    </tr>
                    <tr>
                        <td class="mdl-data-table__cell--non-numeric">Free RAM</td>
                        <td>${userSession.systemInfo.freeRam}</td>
                    </tr>
                    <tr>
                        <td class="mdl-data-table__cell--non-numeric">Max RAM</td>
                        <td>${userSession.systemInfo.maxRam}</td>
                    </tr>
                </table>
            </div>
        </div>

        <%--<div class="mdl-card__actions">--%>

        <%--</div>--%>
    </div>

    <div class="mdl-card mdl-cell mdl-cell--6-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>DB Info</h5></div>

        <div class=".mdl-card__supporting-text" style="overflow: auto;">
            <div class="tableContainer">
                <table class="mdl-data-table mdl-shadow--2dp">
                    <thead>
                    <tr>
                        <th class="mdl-data-table__cell--non-numeric">Property</th>
                        <th>Value</th>
                    </tr>
                    </thead>
                    <c:forEach var="info" items="${dbInfo}">
                        <c:if test="${fn:startsWith(info[0], 'info')}">
                            <tr>
                                <td class="mdl-data-table__cell--non-numeric">${info[0]}</td>
                                <td>${info[1]}</td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </table>
            </div>
        </div>
    </div>

    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>Runtime MXBean Arguments</h5></div>

        <div class=".mdl-card__supporting-text" style="overflow: auto;">
            <div class="tableContainer">
                <table class="mdl-data-table mdl-shadow--2dp">
                    <thead>
                    <tr>
                        <th class="mdl-data-table__cell--non-numeric">Argument</th>
                    </tr>
                    </thead>
                    <c:forEach var="argument" items="${userSession.systemInfo.runtimeMXBeanArguments}">
                        <tr>
                            <td class="mdl-data-table__cell--non-numeric">${argument}</td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </div>
    </div>
</div>

<jsp:include page="${pageContext.request.contextPath}/WEB-INF/webroot/footer.jsp"/>
</body>
</html>