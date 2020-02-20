<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>

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
                location.href = '${pageContext.request.contextPath}/admin/cache/clearCache';
            });
        });
    </script>
</head>
<body>

<jsp:include page="../header.jsp"/>

<section class="hero is-primary is-small">
    <div class="hero-body">
        <div class="container">
            <h1 class="title">
                System Info
            </h1>
        </div>
    </div>
</section>

<section class="section">
    <div class="container">
        <div class="columns is-multiline is-centered">
            <div class="column is-one-third">
                <div class="box overflowTableContainer">
                    <h5 class="subtitle is-5">Sessions (${fn:length(sessions)})</h5>

                    <table class="table is-narrow">
                        <tr>
                            <th>Session Id</th>
                            <th>Username</th>
                            <th>Last Active</th>
                        </tr>
                        <c:forEach var="session" items="${sessions}">
                            <tr>
                                <td title="${session.sessionId}">${fn:substring(session.sessionId, 0, 4)}...</td>
                                <td>${session.principal.username}</td>
                                <td><fmt:formatDate value="${session.lastRequest}" pattern="h:mm:ss a"/></td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
            </div>
            <div class="column is-one-quarter">
                <div class="box overflowTableContainer">
                    <h5 class="subtitle is-5">DB Info</h5>

                    <table class="table is-narrow">
                        <thead>
                        <tr>
                            <th>Property</th>
                            <th class="has-text-right">Value</th>
                        </tr>
                        </thead>
                        <c:forEach var="info" items="${dbInfo}">
                            <c:if test="${fn:startsWith(info[0], 'info')}">
                                <tr>
                                    <td>${info[0]}</td>
                                    <td class="has-text-right">${info[1]}</td>
                                </tr>
                            </c:if>
                        </c:forEach>
                        <c:if test="${!empty dbInfoMap}">
                            <c:forEach var="dbInfoItem" items="${dbInfoMap}">
                                <tr>
                                    <td>${dbInfoItem.key}</td>
                                    <td class="has-text-right">${dbInfoItem.value}</td>
                                </tr>
                            </c:forEach>
                        </c:if>
                    </table>
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="../footer.jsp"/>
</body>
</html>