<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<jsp:useBean id="user" type="net.ehicks.bts.beans.User" scope="request"/>
<jsp:useBean id="recentComments" type="java.util.List<net.ehicks.bts.beans.Comment>" scope="request"/>
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
                <h2 class="issueSubheading">Details</h2>

                <table class="table is-narrow">
                    <tr>
                        <td>Username:</td>
                        <td>${user.username}</td>
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
                        <td><javatime:format value="${user.createdOn}" style="MS" /></td>
                    </tr>
                    <tr>
                        <td>Updated:</td>
                        <td><javatime:format value="${user.lastUpdatedOn}" style="MS" /></td>
                    </tr>
                    <tr>
                        <td style="vertical-align: top">Groups:</td>
                        <td>
                            <c:forEach var="group" items="${user.groups}">
                                ${group.name}
                                <br>
                            </c:forEach>
                        </td>
                    </tr>
                    <tr>
                        <td style="vertical-align: top">Projects:</td>
                        <td>
                            <c:forEach var="project" items="${user.projects}">
                                ${project.name}
                                <br>
                            </c:forEach>
                        </td>
                    </tr>
                    <tr>
                        <td>Avatar:</td>
                        <td>
                            <figure class="image is-64x64">
                                <img src="${pageContext.request.contextPath}/avatar/${user.avatar.id}">
                            </figure>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="column">
                <h2 class="issueSubheading">Activity</h2>

                <c:forEach var="comment" items="${recentComments}">
                    <article class="media">
                        <figure class="media-left">
                            <p class="image is-32x32">
                                <img src="${pageContext.request.contextPath}/avatar/${comment.author.avatar.id}">
                            </p>
                        </figure>
                        <div class="media-content">
                            <div class="content">
                                <div>
                                    <span class="hasTooltip">
                                        ${comment.author.name}
                                    </span>
                                    <div style="display: none;">
                                        <table>
                                            <tr>
                                                <td rowspan="2">
                                                    <img src="${pageContext.request.contextPath}/avatar/${comment.author.avatar.id}"
                                                         style="height:36px;margin-right: 4px;border-radius: 3px;">
                                                </td>
                                                <td><b>${comment.author.name}</b></td>
                                            </tr>
                                            <tr>
                                                <td>${comment.author.username}</td>
                                            </tr>
                                        </table>
                                    </div>

                                    <a href="${pageContext.request.contextPath}/issue/form?issueId=${comment.issue.id}">
                                        ${comment.issue.project.prefix}-${comment.issue.id} -  ${comment.issue.title} -
                                    </a>

                                    on <javatime:format value="${comment.createdOn}" style="MS" />

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
                            <c:if test="${comment.visibleToGroup.id != comment.issue.group.id}">
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