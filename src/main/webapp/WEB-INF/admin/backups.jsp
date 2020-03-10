<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="backups" type="java.util.List<java.io.File>" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../inc_title.jsp"/>
    <jsp:include page="../inc_header.jsp"/>

    <script>
        function createBackup()
        {
            if (confirm('Are you sure?'))
                location.href="${pageContext.request.contextPath}/admin/backups/create";
        }
        function deleteBackup(name)
        {
            if (confirm('Are you sure?'))
                location.href="${pageContext.request.contextPath}/admin/backups/delete?backupName=" + name;
        }

        var checkingInterval;
        if (document.readyState === "loading") {
            document.addEventListener("DOMContentLoaded", checkStatusWrapper);
        } else {  // `DOMContentLoaded` already fired
            checkStatusWrapper();
        }

        function checkStatusWrapper()
        {
            if (!checkingInterval)
                checkingInterval = setInterval(checkStatus, 1000);
        }

        function checkStatus()
        {
            fetch("${pageContext.request.contextPath}/admin/backups/checkStatus")
                .then(function(response) {
                    return response.json();
                })
                .then(function(myJson) {
                    var isRunning = JSON.stringify(myJson);
                    console.log(isRunning);

                    var button = document.getElementById('create');
                    if (isRunning === 'false')
                    {
                        button.value = 'Create Backup';
                        button.disabled = false;
                        clearInterval(checkingInterval);
                    }
                    else
                    {
                        button.value = 'Backup in Progress';
                        button.disabled = true;
                        checkStatusWrapper();
                    }
                });
        }
    </script>
</head>
<body>

<jsp:include page="../header.jsp"/>

<section class="hero is-primary is-small">
    <div class="hero-body">
        <div class="container">
            <h1 class="title">
                Backups - ${backupPath.toAbsolutePath()}
            </h1>
        </div>
    </div>
</section>

<section class="section">
    <div class="container">
        <div class="columns is-multiline is-centered">
            <div class="column is-narrow">
                <div class="box">
                    <table class="table is-striped is-narrow is-hoverable">
                        <thead>
                        <tr>
                            <th class="has-text-right"></th>
                            <th>Name</th>
                            <th class="has-text-right">Size</th>
                            <th>Last Modified</th>
                            <th></th>
                        </tr>
                        </thead>
                        <c:forEach var="backup" items="${backups}" varStatus="loop">
                            <tr>
                                <td class="has-text-right">${loop.count}</td>
                                <td><a href="${pageContext.request.contextPath}/admin/backups/viewBackup?backupName=${backup.name}">${backup.name}</a></td>
                                <td class="has-text-right">${ct:fileSize(backup.length())}</td>
                                <td><fmt:formatDate value="${ct:longToDate(backup.lastModified())}" pattern="MM-dd-yyyy hh:mm:ss a"/> </td>
                                <td><a onclick="deleteBackup('${backup.name}');"><i class="fas fa-trash"></i></a></td>
                            </tr>
                        </c:forEach>
                    </table>

                    <c:set var="createBackupDisabled" value="${isRunning ? 'disabled' : ''}"/>
                    <c:set var="createBackupText" value="${isRunning ? 'Backup in Progress' : 'Create Backup'}"/>
                    <input id="create" type="button" value="${createBackupText}" ${createBackupDisabled} class="button is-primary" onclick="createBackup();"/>
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="../footer.jsp"/>
</body>
</html>