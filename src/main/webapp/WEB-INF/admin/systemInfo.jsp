<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>

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
            <div class="column is-narrow is-one-quarter">
                <div class="box overflowTableContainer">
                    <h5 class="subtitle is-5">Sessions (${fn:length(sessions)})</h5>

                    <table class="table is-narrow is-fullwidth">
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
            <div class="column is-narrow">
                <div class="box overflowTableContainer">
                    <h5 class="subtitle is-5">Recent Requests</h5>

                    <table class="table is-narrow is-fullwidth">
                        <thead>
                        <tr>
                            <th colspan="4"></th>
                            <th colspan="4" class="has-text-centered is-warning">Timings</th>
                            <th></th>
                        </tr>
                        <tr>
                            <th></th>
                            <th>Id</th>
                            <th class="has-text-right">Request Start</th>
                            <th>username</th>
                            <th class="has-text-right">Request</th>
                            <th class="has-text-right">Handle</th>
                            <th class="has-text-right">PostHandle</th>
                            <th class="has-text-right">Template</th>
                            <th>Handler</th>
                        </tr>
                        </thead>
                        <c:forEach var="request" items="${requests}" varStatus="loop">
                                <tr>
                                    <td>${loop.count}</td>
                                    <td>${request.requestId.substring(0, 5)}</td>
                                    <td class="has-text-right"><fmt:formatDate value="${request.requestStart}" pattern="h:mm:ss a" /> </td>
                                    <td>${request.username}</td>
                                    <td class="has-text-right">${request.requestTime} ms</td>
                                    <td class="has-text-right">${request.handleTime} ms</td>
                                    <td class="has-text-right">${request.postHandleTime} ms</td>
                                    <td class="has-text-right">${request.templateTime} ms</td>
                                    <td>${request.handlerName.substring(request.handlerName.lastIndexOf('#') + 1)}</td>
                                </tr>
                        </c:forEach>
                    </table>
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="../footer.jsp"/>
</body>
</html>