<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="issue" type="com.hicks.beans.Issue" scope="request"/>
<jsp:useBean id="comments" type="java.util.List<com.hicks.beans.Comment>" scope="request"/>
<jsp:useBean id="watchers" type="java.util.List<com.hicks.beans.User>" scope="request"/>
<jsp:useBean id="zones" type="java.util.List<com.hicks.beans.Zone>" scope="request"/>
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

        function update(fieldName, fieldValue, url)
        {
            url = '${pageContext.request.contextPath}' + url;
            var data = {fldFieldName: fieldName, fldFieldValue: fieldValue};
            $.post(url, data, function (data, textStatus)
            {

                var notification = document.querySelector('.mdl-js-snackbar');

                if (textStatus === 'success')
                {
                    notification.MaterialSnackbar.showSnackbar(
                            {
                                message: 'Success: ' + data,
                                timeout: 2500
                            }
                    );
                }
                else
                {
                    notification.MaterialSnackbar.showSnackbar(
                            {
                                message: 'Failed: ' + data,
                                timeout: 2500
                            }
                    );
                }
            });
        }

//        function enableEditMode(element)
//        {
//            $(document).on('click', '#' + element.id + 'SaveButton', function(){updateIssue(element.id, element.innerHTML)});
//        }
//        function disableEditMode(element)
//        {
//
//        }
    </script>

</head>
<body onload="initHeader();">

<jsp:include page="header.jsp"/>

<div class="mdl-grid">
    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
        <div class="mdl-card__title">
            <h5>
                <div class="mdl-card__title-text">${issue.title}</div>
                <div class="mdl-card__subtitle-text">${issue.project.name} ${issue.project.prefix}-${issue.id}</div>
            </h5>
        </div>

        <div class="mdl-card__title"><h5>Details</h5></div>

        <form name="frmSave" id="frmSave" method="post" action="${pageContext.request.contextPath}/view?tab1=modify&action=save">
            <div class="mdl-card__supporting-text">
                <table>
                    <tr>
                        <td style="padding: 0 8px;">Type:</td>
                        <td style="padding: 0 8px;">
                            <ct:textToSelect id="fldIssueType" value="${issue.issueType.id}" text="${issue.issueType.name}" items="${issueTypes}" submitAction="/view?tab1=main&tab2=issue&action=update&issueId=${issue.id}"/>
                        </td>
                        <td style="padding: 0 8px;">Status:</td>
                        <td style="padding: 0 8px;">${issue.status}</td>
                    </tr>
                    <tr>
                        <td style="padding: 0 8px;">Created:</td>
                        <td style="padding: 0 8px;"><fmt:formatDate value="${issue.createdOn}" pattern="dd/MMM/yy h:mm a"/></td>
                        <td style="padding: 0 8px;">Updated:</td>
                        <td style="padding: 0 8px;"><fmt:formatDate value="${issue.lastUpdatedOn}" pattern="dd/MMM/yy h:mm a"/></td>
                    </tr>
                    <tr>
                        <td style="padding: 0 8px;">Severity:</td>
                        <td style="padding: 0 8px;">
                            <ct:textToSelect id="fldSeverity" value="${issue.severity.id}" text="${issue.severity.name}" items="${severities}" submitAction="/view?tab1=main&tab2=issue&action=update&issueId=${issue.id}"/>
                        </td>
                        <td style="padding: 0 8px;">Zone:</td>
                        <td style="padding: 0 8px;">
                            <ct:textToSelect id="fldZone" value="${issue.zone.id}" text="${issue.zone.name}" items="${zones}" submitAction="/view?tab1=main&tab2=issue&action=update&issueId=${issue.id}"/>
                        </td>
                    </tr>
                </table>
            </div>
        </form>
    </div>

    <div class="mdl-card mdl-cell mdl-cell--8-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>Description</h5></div>

        <ct:textToInputText id="fldDescription" text="${issue.description}" submitAction="/view?tab1=main&tab2=issue&action=update&issueId=${issue.id}"/>

        <div class="mdl-card__title"><h5>Activity</h5></div>
        <c:forEach var="comment" items="${comments}">
            <div style="padding: 0 8px;">
                <a href="${pageContext.request.contextPath}/view?tab1=main&tab2=user&action=form&userId=${comment.createdByUserId}">
                    ${comment.createdBy}</a>
                commented on
                <fmt:formatDate value="${comment.createdOn}" pattern="dd/MMM/yy h:mm a"/>
                <c:if test="${!empty comment.lastUpdatedOn && comment.createdOn != comment.lastUpdatedOn}">
                    <span title="Edited ${comment.lastUpdatedOn}">*</span>
                </c:if>
                <br>
                <div class="mdl-card__supporting-text">
                    <%--${comment.content}--%>
                    <ct:textToInputText id="fldContent${comment.id}" text="${comment.content}" submitAction="/view?tab1=main&tab2=issue&action=updateComment&commentId=${comment.id}"/>
                </div>
            </div>
            <hr>
        </c:forEach>

        <script>
            function showAddComment()
            {
                $('#showAddComment').hide();
                $('#submitAddComment').show();
                $('#cancelAddComment').show();

                var inject = <jsp:include page="inc_newComment.jsp"/>;
                $('#activityActions').before(inject);
                $('#fldContent').focus();
            }
            function cancelAddComment()
            {

                $('#activityActions').prev('#frmNewComment').remove();
                $('#submitAddComment').hide();
                $('#cancelAddComment').hide();
                $('#showAddComment').show();
            }
            function submitAddComment()
            {
                $('#frmNewComment').submit();
            }
        </script>

        <div id="activityActions" class="mdl-card__actions">
            <input type="button" value="Comment" id="showAddComment" class="mdl-button mdl-js-button mdl-button--raised" onclick="showAddComment();" />
            <input type="button" value="Cancel" id="cancelAddComment" class="mdl-button mdl-js-button mdl-button--raised" onclick="cancelAddComment();" style="display:none;"/>
            <input type="button" value="Add" id="submitAddComment" class="mdl-button mdl-js-button mdl-button--raised" onclick="submitAddComment();" style="display:none;"/>
        </div>
    </div>

    <div class="mdl-card mdl-cell mdl-cell--4-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>People</h5></div>

        <div class="mdl-card__supporting-text">
            <table>
                <tr>
                    <td style="padding: 0 8px;">Assignee:</td>
                    <td style="padding: 0 8px;">
                        <a href="${pageContext.request.contextPath}/view?tab1=main&tab2=user&action=form&userId=${issue.assigneeUserId}">
                            ${issue.assignee}</a>
                    </td>
                </tr>
                <tr>
                    <td style="padding: 0 8px;">Reporter:</td>
                    <td style="padding: 0 8px;">
                        <a href="${pageContext.request.contextPath}/view?tab1=main&tab2=user&action=form&userId=${issue.reporterUserId}">
                            ${issue.reporter}</a>
                    </td>
                </tr>
                <tr>
                    <td style="padding: 0 8px;">Watchers:</td>
                    <td style="padding: 0 8px;">
                        <c:forEach var="watcher" items="${watchers}">
                            <a href="${pageContext.request.contextPath}/view?tab1=main&tab2=user&action=form&userId=${watcher.id}">
                                ${watcher.logonId}</a>
                            <br>
                        </c:forEach>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</div>

<div aria-live="assertive" aria-atomic="true" aria-relevant="text" class="mdl-snackbar mdl-js-snackbar">
    <div class="mdl-snackbar__text"></div>
    <button type="button" class="mdl-snackbar__action"></button>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>