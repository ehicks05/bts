<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="user" type="com.hicks.beans.User" scope="request"/>
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

<div class="mdl-grid">
    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
        <div class="mdl-card__title">
            <h5>
                <div class="mdl-card__title-text">
                    <img src="${user.avatar.base64}" style="padding-right: 8px; height: 48px;"/>
                    ${user.logonId}
                </div>
            </h5>
        </div>
    </div>
    <div class="mdl-card mdl-cell mdl-cell--6-col mdl-cell--4-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>Details</h5></div>

        <form name="frmSave" id="frmSave" method="post" action="${pageContext.request.contextPath}/view?tab1=modify&action=save">
            <div class="mdl-card__supporting-text">
                <table>
                    <tr>
                        <td style="padding: 0 8px;">Created:</td>
                        <td style="padding: 0 8px;"><fmt:formatDate value="${user.createdOn}" pattern="dd/MMM/yy h:mm a"/></td>
                        <td style="padding: 0 8px;">Updated:</td>
                        <td style="padding: 0 8px;"><fmt:formatDate value="${user.updatedOn}" pattern="dd/MMM/yy h:mm a"/></td>
                    </tr>
                    <tr>
                        <td style="padding: 0 8px;">Roles:</td>
                        <td style="padding: 0 8px;">
                            <c:forEach var="role" items="${user.allRoles}">
                                ${role.roleName}
                                <br>
                            </c:forEach>
                        </td>
                    </tr>
                </table>
            </div>
        </form>
    </div>

    <div class="mdl-card mdl-cell mdl-cell--6-col mdl-cell--4-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>Activity</h5></div>

        <div class="mdl-card__supporting-text">
            <c:forEach var="comment" items="${user.allComments}">
                <div style="padding: 0 8px;">
                    <a href="${pageContext.request.contextPath}/view?tab1=main&tab2=profile&action=form&userId=${user.id}">
                        <img src="${user.avatar.base64}" style="padding-right: 4px; height: 24px;"/>${user.logonId}
                    </a>
                    commented on
                    <a href="${pageContext.request.contextPath}/view?tab1=main&tab2=issue&action=form&issueId=${comment.issue.id}">
                        ${comment.issue.project.prefix}-${comment.issue.id} -  ${comment.issue.title} <fmt:formatDate value="${comment.createdOn}" pattern="dd/MMM/yy h:mm a"/>
                    </a>

                </div>
                <br>
                <div style="padding: 0 8px;">${comment.content}</div>
                <hr>
            </c:forEach>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>