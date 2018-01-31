<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="user" type="net.ehicks.bts.beans.User" scope="request"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="inc_title.jsp"/>
    <jsp:include page="inc_header.jsp"/>
    <script>

    </script>

</head>
<body>

<jsp:include page="header.jsp"/>

<section class="hero is-primary is-small">
    <div class="hero-body">
        <div class="container">
            <span class="title">
                <span>${user.name}</span>
            </span>
        </div>
    </div>
</section>

<section class="section">
    <div class="container">
        <div class="columns is-multiline is-centered">
            <div class="column is-one-third">
                <h3 class="subtitle is-3">Details</h3>

                <table class="table">
                    <tr>
                        <td>Logon Id:</td>
                        <td>${user.logonId}</td>
                    </tr>
                    <tr>
                        <td>First Name:</td>
                        <td>${user.firstName}</td>
                    </tr>
                    <tr>
                        <td>Last Name:</td>
                        <td>${user.lastName}</td>
                    </tr>
                    <tr>
                        <td>Created:</td>
                        <td><fmt:formatDate value="${user.createdOn}" pattern="dd/MMM/yy h:mm a"/></td>
                    </tr>
                    <tr>
                        <td>Updated:</td>
                        <td><fmt:formatDate value="${user.updatedOn}" pattern="dd/MMM/yy h:mm a"/></td>
                    </tr>
                    <tr>
                        <td style="vertical-align: top">Groups:</td>
                        <td>
                            <c:forEach var="group" items="${user.allGroups}">
                                ${group.name}
                                <br>
                            </c:forEach>
                        </td>
                    </tr>
                    <tr>
                        <td style="vertical-align: top">Projects:</td>
                        <td>
                            <c:forEach var="project" items="${user.allProjects}">
                                ${project.name}
                                <br>
                            </c:forEach>
                        </td>
                    </tr>
                    <tr>
                        <td>Avatar:</td>
                        <td>
                            <figure class="image is-64x64">
                                <img src="${user.avatar.base64}">
                            </figure>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="column">
                <h3 class="subtitle is-3">Activity</h3>

                <c:forEach var="comment" items="${user.recentComments}">
                    <article class="media">
                        <figure class="media-left">
                            <p class="image is-32x32">
                                <img src="${!empty comment.createdBy.avatar.base64 ? comment.createdBy.avatar.base64 : comment.defaultAvatar.base64}">
                            </p>
                        </figure>
                        <div class="media-content">
                            <div class="content">
                                <div>
                                    <strong class="hasTooltip">
                                        ${comment.createdBy.name}
                                    </strong>
                                    <div style="display: none;">
                                        <table>
                                            <tr>
                                                <td rowspan="2">
                                                    <img src="${!empty comment.createdBy.avatar.base64 ? comment.createdBy.avatar.base64 : comment.defaultAvatar.base64}" style="height:36px;margin-right: 4px;border-radius: 3px;">
                                                </td>
                                                <td><b>${comment.createdBy.name}</b></td>
                                            </tr>
                                            <tr>
                                                <td>${comment.createdBy.logonId}</td>
                                            </tr>
                                        </table>
                                    </div>

                                    <a href="${pageContext.request.contextPath}/view?tab1=issue&action=form&issueId=${comment.issue.id}">
                                        ${comment.issue.project.prefix}-${comment.issue.id} -  ${comment.issue.title} -
                                    </a>

                                    on <fmt:formatDate value="${comment.createdOn}" pattern="dd/MMM/yy h:mm a"/>

                                    <br>
                                    ${comment.content}
                                </div>
                            </div>
                        </div>

                        <div class="media-right">
                            <c:if test="${!empty comment.lastUpdatedOn && comment.createdOn != comment.lastUpdatedOn}">
                                <span class="icon is-small" title="Edited ${comment.lastUpdatedOn}">
                                    <i class="fas fa-edit"></i>
                                </span>
                            </c:if>
                            <c:if test="${comment.visibleToGroupId != 0}">
                                <span class="icon is-small has-text-danger" title="Visible to ${comment.visibleToGroup.name}">
                                    <i class="fas fa-user-secret"></i>
                                </span>
                            </c:if>
                        </div>
                    </article>
                </c:forEach>
            </div>
        </div>
    </div>
</section>

<jsp:include page="footer.jsp"/>
</body>
</html>