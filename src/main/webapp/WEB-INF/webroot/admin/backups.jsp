<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="backups" type="java.util.List<java.io.File>" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <title>BTS</title>
    <jsp:include page="../inc_header.jsp"/>

    <script>
        function createBackup()
        {
            if (confirm('Are you sure?'))
                location.href="${pageContext.request.contextPath}/view?tab1=admin&tab2=backups&action=create";
        }
        function deleteBackup(name)
        {
            if (confirm('Are you sure?'))
                location.href="${pageContext.request.contextPath}/view?tab1=admin&tab2=backups&action=delete&backupName=" + name;
        }
    </script>
</head>
<body>

<jsp:include page="../header.jsp"/>

<div class="mdl-grid">

    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>Backups</h5></div>

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
                    <th>Size</th>
                    <th>Last Modified</th>
                    <th></th>
                </tr>
                </thead>
                <c:forEach var="backup" items="${backups}" varStatus="loop">
                    <tr>
                        <td>
                            <label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect mdl-data-table__select" for="row[${loop.count}]">
                                <input type="checkbox" id="row[${loop.count}]" class="mdl-checkbox__input" />
                            </label>
                        </td>
                        <td>${loop.count}</td>
                        <td class="mdl-data-table__cell--non-numeric">
                            <a href="${pageContext.request.contextPath}/view?tab1=admin&tab2=backups&action=viewBackup&backupName=${backup.name}">
                                ${backup.name}</a>
                        </td>
                        <td>${ct:fileSize(backup.length())}</td>
                        <td><fmt:formatDate value="${ct:longToDate(backup.lastModified())}" pattern="MM-dd-yyyy hh:mm:ss a"/> </td>
                        <td><a onclick="deleteBackup('${backup.name}');" class="clickable material-icons">delete</a></td>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <div class="mdl-card__actions">
            <input id="create" type="button" value="Create Backup" class="mdl-button mdl-js-button mdl-button--raised" onclick="createBackup();"/>
        </div>
    </div>
</div>

<jsp:include page="../footer.jsp"/>
</body>
</html>