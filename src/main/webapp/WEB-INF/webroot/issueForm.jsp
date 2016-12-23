<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="issue" type="com.hicks.beans.Issue" scope="request"/>
<jsp:useBean id="comments" type="java.util.List<com.hicks.beans.Comment>" scope="request"/>
<jsp:useBean id="watcherMaps" type="java.util.List<com.hicks.beans.WatcherMap>" scope="request"/>
<jsp:useBean id="zones" type="java.util.List<com.hicks.beans.Zone>" scope="request"/>
<jsp:useBean id="potentialWatchers" type="java.util.List<com.hicks.beans.User>" scope="request"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>BTS</title>
    <jsp:include page="inc_header.jsp"/>
    <script>
        function initHeader()
        {
            $('#fldWatcher').select2();

            // QTIP
            $('#showWatchers').each(function() {
                $(this).qtip({
                    content: {
                        text: $(this).next('.watchersDiv')
                    },
                    style: {
                        classes: 'qtip-bootstrap'
                    },
                    show: 'click',
                    hide: 'unfocus'
                });
            });

            // Grab all elements with the class "hasTooltip"
            $('.hasTooltip').each(function() { // Notice the .each() loop, discussed below
                $(this).qtip({
                    content: {
                        text: $(this).next('div') // Use the "div" element next to this for the content
                    },
                    style: {
                        classes: 'qtip-bootstrap'
                    }
                });
            });
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
                    return 'success';
                }
                else
                {
                    notification.MaterialSnackbar.showSnackbar(
                            {
                                message: 'Failed: ' + data,
                                timeout: 2500
                            }
                    );
                    return 'fail';
                }
            });
        }

        function addWatcher(elementId, issueId)
        {
            var userId = $('#' + elementId).val();
            location.href = "${pageContext.request.contextPath}/view?tab1=main&tab2=issue&action=addWatcher&issueId=${issue.id}&userId=" + userId;
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
                    <t:textToInputText tag="h5" id="fldTitle" text="${issue.title}" submitAction="/view?tab1=main&tab2=issue&action=update&issueId=${issue.id}"/>
                </div>
                <div class="mdl-card__subtitle-text">
                    <t:textToSelect id="fldProject" value="${issue.projectId}" text="${issue.project.name}" items="${projects}" submitAction="/view?tab1=main&tab2=issue&action=update&issueId=${issue.id}"/>
                    ${issue.project.prefix}-${issue.id}
                </div>
            </h5>
        </div>

        <div class="mdl-card__title"><h5>Details</h5></div>

        <form name="frmSave" id="frmSave" method="post" action="${pageContext.request.contextPath}/view?tab1=modify&action=save">
            <div class="mdl-card__supporting-text">
                <table>
                    <tr>
                        <td style="padding: 0 8px;">Type:</td>
                        <td style="padding: 0 8px;">
                            <t:textToSelect id="fldIssueType" value="${issue.issueType.id}" text="${issue.issueType.name}" items="${issueTypes}" submitAction="/view?tab1=main&tab2=issue&action=update&issueId=${issue.id}"/>
                        </td>
                        <td style="padding: 0 8px;">Status:</td>
                        <td style="padding: 0 8px;">
                            <t:textToSelect id="fldStatus" value="${issue.status.id}" text="${issue.status.name}" items="${statuses}" submitAction="/view?tab1=main&tab2=issue&action=update&issueId=${issue.id}"/>
                        </td>
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
                            <t:textToSelect id="fldSeverity" value="${issue.severity.id}" text="${issue.severity.name}" items="${severities}" submitAction="/view?tab1=main&tab2=issue&action=update&issueId=${issue.id}"/>
                        </td>
                        <td style="padding: 0 8px;">Zone:</td>
                        <td style="padding: 0 8px;">
                            <t:textToSelect id="fldZone" value="${issue.zone.id}" text="${issue.zone.name}" items="${zones}" submitAction="/view?tab1=main&tab2=issue&action=update&issueId=${issue.id}"/>
                        </td>
                    </tr>
                </table>
            </div>
        </form>
    </div>

    <div class="mdl-card mdl-cell mdl-cell--8-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>Description</h5></div>

        <div class="mdl-card__supporting-text">
            <t:textToInputText id="fldDescription" text="${issue.description}" submitAction="/view?tab1=main&tab2=issue&action=update&issueId=${issue.id}"/>
        </div>

        <div class="mdl-card__title"><h5>Activity</h5></div>

        <div class="mdl-tabs mdl-js-tabs mdl-js-ripple-effect">
            <div class="mdl-tabs__tab-bar">
                <a href="#comments-panel" class="mdl-tabs__tab is-active">Comments</a>
                <a href="#changeLog-panel" class="mdl-tabs__tab">Change Log</a>
            </div>
            <div class="mdl-tabs__panel is-active" id="comments-panel">
                <c:set var="commentIndex" value="${0}"/>
                <c:forEach var="comment" items="${comments}">
                    <c:if test="${commentIndex == 0}">
                        <div style="height: 16px;"></div>
                    </c:if>
                    <c:if test="${commentIndex != 0}">
                        <hr>
                    </c:if>
                    <c:set var="commentIndex" value="${commentIndex + 1}"/>

                    <div style="padding: 0 8px;">
                        <a href="${pageContext.request.contextPath}/view?tab1=main&tab2=profile&action=form&userId=${comment.createdByUserId}">
                            <img src="${!empty comment.createdBy.avatar.base64 ? comment.createdBy.avatar.base64 : comment.defaultAvatar.base64}" style="height:24px;margin-right: 4px;border-radius: 3px;">
                            <span class="hasTooltip">${comment.createdBy.name}</span>
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
                        </a>

                        commented on
                        <fmt:formatDate value="${comment.createdOn}" pattern="dd/MMM/yy h:mm a"/>
                        <c:if test="${!empty comment.lastUpdatedOn && comment.createdOn != comment.lastUpdatedOn}">
                            <span title="Edited ${comment.lastUpdatedOn}">*</span>
                        </c:if>
                        <c:if test="${comment.visibleToGroupId != 0}">
                            <span style="font-size: 14px;color:red;">
                                <i class="material-icons" style="font-size: 14px">lock</i> Visible to ${comment.visibleToGroup.name}
                            </span>
                        </c:if>
                        <br>
                        <div class="mdl-card__supporting-text">
                            <t:textToInputText id="fldContent${comment.id}" text="${comment.content}" submitAction="/view?tab1=main&tab2=issue&action=updateComment&commentId=${comment.id}"/>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="mdl-tabs__panel" id="changeLog-panel">
                <ul>
                    <li>Eddard</li>
                    <li>Catelyn</li>
                    <li>Robb</li>
                    <li>Sansa</li>
                    <li>Brandon</li>
                    <li>Arya</li>
                    <li>Rickon</li>
                </ul>
            </div>
        </div>

        <script>
            function showAddComment()
            {
                $('#showAddComment').hide();
                $('#submitAddComment').show();
                $('#cancelAddComment').show();

                $('#newCommentFormContainer').show();
                $('#fldContent').focus();
            }
            function cancelAddComment()
            {
                $('#newCommentFormContainer').hide();
                $('#submitAddComment').hide();
                $('#cancelAddComment').hide();
                $('#showAddComment').show();
            }
            function submitAddComment()
            {
                $('#frmNewComment').submit();
            }
        </script>

        <div id="newCommentFormContainer" style="display: none;">
            <hr>
            <form class="mdl-card__supporting-text" style="width: 100%" id="frmNewComment" name="frmNewComment" method="post" action="${pageContext.request.contextPath}/view?tab1=main&tab2=issue&action=addComment&issueId=${issue.id}">
                <div class="mdl-textfield mdl-js-textfield" style="width: 98%">
                    <textarea class="mdl-textfield__input" type="text" rows="3" maxrows="6" id="fldContent" name="fldContent"></textarea>
                    <label class="mdl-textfield__label" for="fldContent">Comment:</label>
                </div>
                <br>
                Visibility Level:
                <select id="fldVisibility" name="fldVisibility">
                    <option value="">Default</option>
                    <c:forEach var="group" items="${groups}">
                        <option value="${group.id}">${group.name}</option>
                    </c:forEach>
                </select>
            </form>
        </div>

        <div id="activityActions" class="mdl-card__actions">
            <input type="button" value="Comment" id="showAddComment" class="mdl-button mdl-js-button mdl-button--raised" onclick="showAddComment();" />
            <input type="button" value="Cancel" id="cancelAddComment" class="mdl-button mdl-js-button mdl-button--raised" onclick="cancelAddComment();" style="display:none;"/>
            <input type="button" value="Add" id="submitAddComment" class="mdl-button mdl-js-button mdl-button--raised" onclick="submitAddComment();" style="display:none;"/>
        </div>
    </div>

    <div class="mdl-card mdl-cell mdl-cell--4-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-cell--top mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>People</h5></div>

        <div class="mdl-card__supporting-text">
            <table>
                <tr>
                    <td style="padding: 0 8px;">Assignee:</td>
                    <td style="padding: 0 8px;">
                        <c:if test="${!empty issue.assignee}">
                            <a href="${pageContext.request.contextPath}/view?tab1=main&tab2=profile&action=form&userId=${issue.assigneeUserId}">
                                <img src="${issue.assignee.avatar.base64}" style="height:24px;margin-right: 4px;border-radius: 3px;"></a>

                            <t:textToSelect id="fldAssigneeId" value="${issue.assigneeUserId}" text="${issue.assignee.name}" items="${potentialAssignees}" submitAction="/view?tab1=main&tab2=issue&action=update&issueId=${issue.id}"/>
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td style="padding: 0 8px;">Reporter:</td>
                    <td style="padding: 0 8px;">
                        <a href="${pageContext.request.contextPath}/view?tab1=main&tab2=profile&action=form&userId=${issue.reporterUserId}">
                            <img src="${issue.reporter.avatar.base64}" style="height:24px;margin-right: 4px;border-radius: 3px;"></a>

                        <t:textToSelect id="fldReporterId" value="${issue.reporterUserId}" text="${issue.reporter.name}" items="${potentialReporters}" submitAction="/view?tab1=main&tab2=issue&action=update&issueId=${issue.id}"/>
                    </td>
                </tr>
            </table>
        </div>
        <hr>
        <div class="mdl-card__supporting-text">
            <table>
                <tr>
                    <td style="padding: 0 8px;vertical-align: top;">Watchers:</td>
                    <td style="padding: 0 8px;">
                        <c:if test="${!empty watcherMaps}">
                            <a id="showWatchers" style="cursor: pointer">
                                View</a>

                            <div class="watchersDiv" style="display: none;">
                                <table>
                                    <c:forEach var="watcherMap" items="${watcherMaps}">
                                        <tr>
                                            <td>
                                                <a style="" href="${pageContext.request.contextPath}/view?tab1=main&tab2=profile&action=form&userId=${watcherMap.watcher.id}">
                                                    <img src="${watcherMap.watcher.avatar.base64}" style="height:24px;margin-right: 4px;border-radius: 3px;">${watcherMap.watcher.name}
                                                </a>
                                            </td>
                                            <td>
                                                <a style="vertical-align: sub" href="${pageContext.request.contextPath}/view?tab1=main&tab2=issue&action=removeWatcher&issueId=${issue.id}&watcherMapId=${watcherMap.id}"><i style="color:gray;" class="material-icons">delete</i></a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </div>
                            <br>
                        </c:if>

                        <c:if test="${!empty potentialWatchers}">
                            Add:

                            <select id="fldWatcher" style="z-index: 100" class="js-example-basic-single">
                                <c:forEach var="potentialWatchers" items="${potentialWatchers}">
                                    <option value="${potentialWatchers.id}">${potentialWatchers.name}</option>
                                </c:forEach>
                            </select>

                            <button class="mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab mdl-js-ripple-effect" onclick="addWatcher('fldWatcher', '${issue.id}')">
                                <i class="material-icons">add</i>
                            </button>
                        </c:if>
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