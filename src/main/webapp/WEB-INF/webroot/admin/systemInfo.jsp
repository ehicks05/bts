<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="userSession" type="net.ehicks.bts.UserSession" scope="session"/>
<jsp:useBean id="sessions" type="java.util.Map<java.lang.String, net.ehicks.bts.UserSession>" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <title>BTS</title>
    <jsp:include page="../inc_header.jsp"/>

    <script>
        $('document').ready(function ()
        {
            $('#clearCache').click(function ()
            {
                location.href = '${pageContext.request.contextPath}/view?tab1=admin&tab2=cache&action=clearCache';
            });
        });
    </script>
    <style>
        .flexContainer {
            display: flex;
            flex-direction: row;
            flex-wrap: wrap;
            justify-content: flex-start;
        }
        .flexItem {
            overflow-x: auto;
            margin: 10px;
        }
    </style>
</head>
<body>

<jsp:include page="../header.jsp"/>

<div class="flexContainer">

    <div class="flexItem">
        <div class="mdl-card__title"><h5>Sessions</h5></div>

        <table class="list">
            <tr class="listheading">
                <th colspan="3">Sessions</th>
            </tr>
            <tr class="listheading">
                <th class="">Session Id</th>
                <th class="">Logon Id</th>
                <th class="">Last Active</th>
            </tr>
            <c:forEach var="sessionId" items="${sessionIds}">
                <tr>
                    <td class="" title="${sessionId}">${fn:substring(sessionId, 0, 4)}...</td>
                    <td class="">${sessions[sessionId].logonId}</td>
                    <td class=""><fmt:formatDate value="${sessions[sessionId].lastActivity}" pattern="h:mm:ss a"/></td>
                </tr>
            </c:forEach>
        </table>
    </div>

    <div class="flexItem">
        <div class="mdl-card__title"><h5>System Info</h5></div>

        <table class="table">
            <thead>
            <tr>
                <th class="mdl-data-table__cell--non-numeric">Property</th>
                <th>Value</th>
            </tr>
            </thead>

            <c:forEach var="entry" items="${userSession.systemInfo.stats}">
                <tr>
                    <td class="mdl-data-table__cell--non-numeric">${entry.key}</td>
                    <td>${entry.value}</td>
                </tr>
            </c:forEach>

            <c:forEach var="cpInfoItem" items="${cpInfo}">
                <tr>
                    <td colspan="2">${cpInfoItem}</td>
                </tr>
            </c:forEach>
        </table>
    </div>

    <div class="flexItem">
        <div class="mdl-card__title"><h5>DB Info</h5></div>

        <table class="table">
            <thead>
            <tr>
                <th class="mdl-data-table__cell--non-numeric">Property</th>
                <th class="alignright">Value</th>
            </tr>
            </thead>
            <c:forEach var="info" items="${dbInfo}">
                <c:if test="${fn:startsWith(info[0], 'info')}">
                    <tr>
                        <td class="mdl-data-table__cell--non-numeric">${info[0]}</td>
                        <td class="alignright">${info[1]}</td>
                    </tr>
                </c:if>
            </c:forEach>
        </table>
    </div>

    <div class="flexItem">
        <div class="mdl-card__title"><h5>Runtime MXBean Arguments</h5></div>

        <table class="table">
            <thead>
            <tr>
                <th class="mdl-data-table__cell--non-numeric">Argument</th>
            </tr>
            </thead>
            <c:forEach var="argument" items="${userSession.systemInfo.runtimeMXBeanArguments}">
                <tr>
                    <td class="mdl-data-table__cell--non-numeric" style="break-inside: auto;">${argument}</td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>

<jsp:include page="../footer.jsp"/>
</body>
</html>