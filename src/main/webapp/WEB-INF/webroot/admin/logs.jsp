<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="logs" type="java.util.List<java.io.File>" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <title>BTS</title>
    <jsp:include page="../inc_header.jsp"/>

    <script>
        function deleteLog(name)
        {
            if (confirm('Are you sure?'))
                location.href="${pageContext.request.contextPath}/view?tab1=admin&tab2=logs&action=delete&logName=" + name;
        }
    </script>
</head>
<body>

<jsp:include page="../header.jsp"/>

<div class="mdl-grid">

    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>Logs</h5></div>

        <div class="tableContainer">
            <table class="mdl-data-table mdl-shadow--2dp">
                <thead>
                <tr>
                    <th>
                        <label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect mdl-data-table__select" for="table-header">
                            <input type="checkbox" id="table-header" class="mdl-checkbox__input" />
                        </label>
                    </th>
                    <th></th>
                    <th class="mdl-data-table__cell--non-numeric">Name</th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <c:forEach var="log" items="${logs}" varStatus="loop">
                    <tr>
                        <td>
                            <label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect mdl-data-table__select" for="row[${loop.count}]">
                                <input type="checkbox" id="row[${loop.count}]" class="mdl-checkbox__input" />
                            </label>
                        </td>
                        <td>${loop.count}</td>
                        <td class="mdl-data-table__cell--non-numeric">
                            ${log.name}
                        </td>
                        <td class="mdl-data-table__cell--non-numeric">
                            <a href="${pageContext.request.contextPath}/view?tab1=admin&tab2=logs&action=viewLog&logName=${log.name}">
                                <i class="material-icons">assignment</i>Raw</a>
                        </td>
                        <td class="mdl-data-table__cell--non-numeric">
                            <a href="${pageContext.request.contextPath}/view?tab1=admin&tab2=logs&action=viewLogPretty&logName=${log.name}">
                                <i class="material-icons">grid_on</i>Pretty</a>
                        </td>
                        <td><a onclick="deleteLog('${log.name}');" class="clickable material-icons">delete</a></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>

<jsp:include page="../footer.jsp"/>
</body>
</html>