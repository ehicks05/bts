<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="issue" type="com.hicks.beans.Issue" scope="request"/>
<jsp:useBean id="comments" type="java.util.List<com.hicks.beans.Comment>" scope="request"/>
<jsp:useBean id="watchers" type="java.util.List<com.hicks.beans.User>" scope="request"/>
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

<table class="list">
    <tr>
        <td colspan="2">
            <h2 style="margin: 0;">${issue.title}</h2>
            <h5 style="margin: 0;">${issue.project.name} ${issue.project.prefix}-${issue.id}</h5>
            <h5 style="margin: 0;">${issue.zone.name}</h5>
            <br>
        </td>
    </tr>
    <tr>
        <td style="width: 66%;">

            <h5>Details</h5>

            <form name="frmSave" id="frmSave" method="post" action="${pageContext.request.contextPath}/view?tab1=modify&action=save">
                <table class="list">
                    <tr>
                        <td style="padding: 0 20px;">Type:</td>
                        <td style="padding: 0 20px;">${issue.issueType.name}</td>
                        <td style="padding: 0 20px;">Status:</td>
                        <td style="padding: 0 20px;">${issue.status}</td>
                    </tr>
                    <tr>
                        <td style="padding: 0 20px;">Created On:</td>
                        <td style="padding: 0 20px;"><fmt:formatDate value="${issue.createdOn}" pattern="dd/MMM/yy hh:mm a"/></td>
                        <td style="padding: 0 20px;">Last Updated:</td>
                        <td style="padding: 0 20px;"><fmt:formatDate value="${issue.lastUpdatedOn}" pattern="dd/MMM/yy hh:mm a"/></td>
                    </tr>
                    <tr>
                        <td style="padding: 0 20px;">Severity:</td>
                        <td style="padding: 0 20px;">${issue.severity.name}</td>
                        <td style="padding: 0 20px;"></td>
                        <td style="padding: 0 20px;"></td>
                    </tr>
                </table>

                <h5>Description</h5>
                <div style="padding: 0 20px;">${issue.description}</div>
            </form>

            <br>
            <h5>Activity</h5>
            <c:forEach var="comment" items="${comments}">
                <div style="padding: 0 20px;">
                    ${comment.createdBy} commented on <fmt:formatDate value="${comment.createdOn}" pattern="dd/MMM/yy hh:mm a"/>
                    <br>
                    ${comment.content}
                </div>
                <hr>
            </c:forEach>
            <br>
            <input type="button" value="Comment" class="btn btn-default" onclick="showAddComment();"/>
        </td>
        <td style="width: 33%;vertical-align: top">
            <h5>People</h5>
            <table class="list">
                <tr>
                    <td style="padding: 0 20px;">Assignee:</td>
                    <td style="padding: 0 20px;">${issue.assignee}</td>
                </tr>
                <tr>
                    <td style="padding: 0 20px;">Reporter:</td>
                    <td style="padding: 0 20px;">${issue.reporter}</td>
                </tr>
                <tr>
                    <td style="padding: 0 20px;">Watchers:</td>
                    <td style="padding: 0 20px;">
                        <c:forEach var="watcher" items="${watchers}">
                            ${watcher.logonId}<br>
                        </c:forEach>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>

<jsp:include page="footer.jsp"/>
</body>
</html>