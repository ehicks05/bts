<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="issue" type="com.hicks.beans.Issue" scope="request"/>
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
                <h5 style="margin: 0;">${issue.zone.name}</h5>
                <br>
                <br>
            </td>
        </tr>

        <tr>
            <td colspan="4">Details:</td>
        </tr>
        <tr>
            <td style="padding: 0 20px;">Type:</td>
            <td style="padding: 0 20px;">${issue.issueTypeId}</td>
            <td style="padding: 0 20px;">Status:</td>
            <td style="padding: 0 20px;">${issue.status}</td>
        </tr>
        <tr>
            <td style="padding: 0 20px;">Created On:</td>
            <td style="padding: 0 20px;"><fmt:formatDate value="${issue.createdOn}" pattern="dd/MMM/yy hh:mm a"/></td>
            <td style="padding: 0 20px;">Last Updated:</td>
            <td style="padding: 0 20px;">${issue.lastUpdatedOn}</td>
        </tr>

        <tr>
            <td colspan="4"><br>Description:</td>
        </tr>
        <tr>
            <td colspan="4" style="padding: 0 20px;">${issue.description}</td>
        </tr>

        <tr>
            <td colspan="4">
                <input type="button" value="Comment" class="btn btn-primary" onclick="showAddComment();"/>
            </td>
        </tr>
    </table>
</form>

<jsp:include page="footer.jsp"/>
</body>
</html>