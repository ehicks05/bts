<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="userSession" type="net.ehicks.bts.UserSession" scope="session"/>
<jsp:useBean id="userSessions" type="java.util.List<net.ehicks.bts.UserSession>" scope="request"/>
<jsp:useBean id="connectionInfo" type="net.ehicks.eoi.ConnectionInfo" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../inc_title.jsp"/>
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
                <th colspan="3">Sessions: ${fn:length(userSessions)}</th>
            </tr>
            <tr class="listheading">
                <th class="">Session Id</th>
                <th class="">Logon Id</th>
                <th class="">Last Active</th>
            </tr>
            <c:forEach var="userSesh" items="${userSessions}">
                <tr>
                    <td class="" title="${userSesh.sessionId}">${fn:substring(userSesh.sessionId, 0, 4)}...</td>
                    <td class="">${userSesh.logonId}</td>
                    <td class=""><fmt:formatDate value="${userSesh.lastActivity}" pattern="h:mm:ss a"/></td>
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
                    <td align="right">${entry.value}</td>
                </tr>
            </c:forEach>
        </table>
    </div>

    <div class="flexItem">
        <div class="mdl-card__title"><h5>DB Pool Info</h5></div>

        <table class="table">
            <thead>
            <tr>
                <th class="mdl-data-table__cell--non-numeric">Property</th>
                <th>Value</th>
            </tr>
            </thead>

            <c:forEach var="cpInfoEntry" items="${cpInfo}">
                <tr>
                    <td>${cpInfoEntry.key}</td>
                    <td align="right">${cpInfoEntry.value}</td>
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
            <c:if test="${!empty dbInfoMap}">
                <c:forEach var="dbInfoItem" items="${dbInfoMap}">
                    <tr>
                        <td class="mdl-data-table__cell--non-numeric">${dbInfoItem.key}</td>
                        <td class="alignright">${dbInfoItem.value}</td>
                    </tr>
                </c:forEach>
            </c:if>
        </table>
    </div>

    <div class="flexItem">
        <div class="mdl-card__title"><h5>Connection Info</h5></div>

        <table class="table">
            <thead>
            <tr>
                <th class="mdl-data-table__cell--non-numeric">Property</th>
                <th class="alignright">Value</th>
            </tr>
            </thead>
            <tr>
                <td class="mdl-data-table__cell--non-numeric">Mode</td>
                <td class="alignright">${connectionInfo.dbMode}</td>
            </tr>
            <tr>
                <td class="mdl-data-table__cell--non-numeric">Host</td>
                <td class="alignright">${connectionInfo.dbHost}</td>
            </tr>
            <tr>
                <td class="mdl-data-table__cell--non-numeric">DB Name</td>
                <td class="alignright">${connectionInfo.dbName}</td>
            </tr>
            <tr>
                <td class="mdl-data-table__cell--non-numeric">Port</td>
                <td class="alignright">${connectionInfo.dbPort}</td>
            </tr>
            <tr>
                <td class="mdl-data-table__cell--non-numeric">User</td>
                <td class="alignright">${connectionInfo.dbUser}</td>
            </tr>
            <tr>
                <td class="mdl-data-table__cell--non-numeric">Pass</td>
                <td class="alignright">****</td>
            </tr>
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