<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="issue" type="com.hicks.beans.Issue" scope="request"/>
<jsp:useBean id="searchResult" type="com.hicks.SearchResult" scope="session"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>BTS</title>
    <jsp:include page="inc_header.jsp"/>
    <script>
        function initHeader()
        {

        }
    </script>

</head>
<body onload="initHeader();">

<jsp:include page="header.jsp"/>
<br>

<form name="frmSave" id="frmSave" method="post" action="${pageContext.request.contextPath}/view?tab1=modify&action=save">
    <table class="list" style="width: 66%;">
        <tr>
            <td colspan="4">
                <h2 style="margin: 0;">${issue.title}</h2>
                <h5 style="margin: 0;">${issue.project.prefix}-${issue.id}</h5>
                <br>
                <br>
            </td>
        </tr>

        <tr>
            <td colspan="4">Details:</td>
        </tr>
        <tr>
            <td>Type:</td>
            <td>${issue.issueTypeId}</td>
            <td>Status:</td>
            <td>${issue.status}</td>
        </tr>
        <tr>
            <td>Created On:</td>
            <td>${issue.createdOn}</td>
            <td>Last Updated:</td>
            <td>${issue.lastUpdatedOn}</td>
        </tr>

        <tr>
            <td colspan="4"><br>Description:</td>
        </tr>
        <tr>
            <td colspan="3">${issue.description}</td>
        </tr>

        <tr>
            <td colspan="4">
                <input type="button" value="Comment" class="btn btn-primary" onclick="showAddComment();"/>
            </td>
        </tr>
    </table>
</form>
</body>
</html>