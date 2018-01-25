<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="userSession" type="net.ehicks.bts.UserSession" scope="session"/>
<jsp:useBean id="issue" type="net.ehicks.bts.beans.Issue" scope="request"/>
<jsp:useBean id="comments" type="java.util.List<net.ehicks.bts.beans.Comment>" scope="request"/>
<jsp:useBean id="watcherMaps" type="java.util.List<net.ehicks.bts.beans.WatcherMap>" scope="request"/>
<jsp:useBean id="groups" type="java.util.List<net.ehicks.bts.beans.Group>" scope="request"/>
<jsp:useBean id="potentialWatchers" type="java.util.List<net.ehicks.bts.beans.User>" scope="request"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="inc_title.jsp"/>
    <jsp:include page="inc_header.jsp"/>
    <script>
        $(function() {
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

            $(document).on('click', '.notification > button.delete', function() {
                $(this).parent().addClass('is-hidden');
                return false;
            });

            // init the tabs
            $('#changeLogNavTab').on('click', ajaxGetChangeLog);
            $('#tabs li').on('click', function() {
                var tab = $(this).data('tab');

                $('#tabs li').removeClass('is-active');
                $(this).addClass('is-active');

                $('#tab-content > div').removeClass('is-active');
                $('div[data-content="' + tab + '"]').addClass('is-active');
            });

            $('#submitAddComment').on('click', function () {
                $('#frmNewComment').submit();
            });
        });

        function update(fieldName, fieldValue, url)
        {
            url = '${pageContext.request.contextPath}' + url;
            var data = {fldFieldName: fieldName, fldFieldValue: fieldValue};
            $.post(url, data, function (data, textStatus)
            {
                if (!data)
                    return 'ok';
                
                var notification = document.querySelector('#ajax-update-notification');

                if (textStatus === 'success')
                {
                    notification.className = 'notification is-success';
                    notification.innerHTML = '<button class="delete"></button>Success: ' + data;
                    return 'success';
                }
                else
                {
                    notification.className = 'notification is-danger';
                    notification.innerHTML = 'Failed: ' + data;
                    return 'fail';
                }
            });
        }

        function addWatcher(elementId, issueId)
        {
            var userId = $('#' + elementId).val();
            location.href = "${pageContext.request.contextPath}/view?tab1=issue&action=addWatcher&issueId=${issue.id}&userId=" + userId;
        }

        function deleteAttachment(attachmentId)
        {
            if (confirm('Are you sure?'))
                location.href="${pageContext.request.contextPath}/view?tab1=issue&action=deleteAttachment&issueId=${issue.id}&attachmentId=" + attachmentId;
        }

        // todo replace with ajaxUtil.js
        function ajaxGetChangeLog()
        {
            var target = $('#changeLog'); //put your target here!
            target.css('opacity', '.50');

            var opts = {
                lines: 13, // The number of lines to draw
                length: 20, // The length of each line
                width: 10, // The line thickness
                radius: 30, // The radius of the inner circle
                corners: 1, // Corner roundness (0..1)
                rotate: 0, // The rotation offset
                direction: 1, // 1: clockwise, -1: counterclockwise
                color: '#000', // #rgb or #rrggbb or array of colors
                speed: 1, // Rounds per second
                trail: 60, // Afterglow percentage
                shadow: false, // Whether to render a shadow
                hwaccel: false, // Whether to use hardware acceleration
                className: 'spinner', // The CSS class to assign to the spinner
                zIndex: 2e9, // The z-index (defaults to 2000000000)
                top: '50%', // Top position relative to parent
                left: '50%' // Left position relative to parent
            };
            var spinner = new Spinner(opts).spin(target[0]);

            var url = '${pageContext.request.contextPath}/view?tab1=issue&action=ajaxGetChangeLog&issueId=${issue.id}';
            $.get(url, [], function (data, textStatus)
            {
                spinner.stop();
                target.css('opacity', '1');
                
                if (!data)
                    return 'ok';

                if (textStatus === 'success')
                {
                    $('#changeLog').append(data);
                    $('#changeLogNavTab').unbind('click', ajaxGetChangeLog); // we only want the data loaded once.
                    return 'success';
                }
                else
                    return 'fail';
            });
        }
    </script>
    <style>
        #tab-content > div {
            display: none;
        }

        #tab-content > div.is-active {
            display: block;
        }
    </style>
</head>
<body>

<jsp:include page="header.jsp"/>

<section class="hero is-primary is-small">
    <div class="hero-body">
        <div class="container">
            <h1 class="title">
                Issue ${issue.project.prefix}-${issue.id}
            </h1>
        </div>
    </div>
</section>
<div id="ajax-update-notification" class="notification is-hidden">
    <button class="delete"></button>
</div>

<section class="section">
    <div class="container">
        <div class="columns is-multiline is-centered">
            <div class="column">
                <div class="box">
                    <t:textToInputText tag="h1" clazz="title" id="fldTitle" text="${issue.title}" submitAction="/view?tab1=issue&action=update&issueId=${issue.id}"/>

                    <form name="frmSave" id="frmSave" method="post" action="${pageContext.request.contextPath}/view?tab1=modify&action=save">
                        <div class="">
                            <table class="table">
                                <tr>
                                    <td>Project:</td>
                                    <td>
                                        <t:textToSelect id="fldProject" tag="span" myClass="" value="${issue.projectId}" text="${issue.project.name}" items="${projects}" submitAction="/view?tab1=issue&action=update&issueId=${issue.id}"/>
                                    </td>
                                    <td></td>
                                    <td>
                                    </td>
                                </tr>
                                <tr>
                                    <td>Type:</td>
                                    <td>
                                        <t:textToSelect id="fldIssueType" value="${issue.issueType.id}" text="${issue.issueType.name}" items="${issueTypes}" submitAction="/view?tab1=issue&action=update&issueId=${issue.id}"/>
                                    </td>
                                    <td>Status:</td>
                                    <td>
                                        <t:textToSelect id="fldStatus" value="${issue.status.id}" text="${issue.status.name}" items="${statuses}" submitAction="/view?tab1=issue&action=update&issueId=${issue.id}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>Created:</td>
                                    <td><fmt:formatDate value="${issue.createdOn}" pattern="dd/MMM/yy h:mm a"/></td>
                                    <td>Updated:</td>
                                    <td><fmt:formatDate value="${issue.lastUpdatedOn}" pattern="dd/MMM/yy h:mm a"/></td>
                                </tr>
                                <tr>
                                    <td>Severity:</td>
                                    <td>
                                        <t:textToSelect id="fldSeverity" value="${issue.severity.id}" text="${issue.severity.name}" items="${severities}" submitAction="/view?tab1=issue&action=update&issueId=${issue.id}"/>
                                    </td>
                                    <td>Group:</td>
                                    <td>
                                        <t:textToSelect id="fldGroup" value="${issue.group.id}" text="${issue.group.name}" items="${groups}" submitAction="/view?tab1=issue&action=update&issueId=${issue.id}"/>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="columns is-multiline is-centered">
            <div class="column">
                <div class="box">
                    <h2 class="subtitle">Attachments</h2>

                    <div class="columns is-multiline">
                        <c:forEach var="attachment" items="${issue.attachments}">
                            <c:if test="${attachment.thumbnailDbFileId > 0}">
                                <div class="column is-one-quarter">
                                    <div class="card">
                                        <header class="card-header">
                                            <p class="card-header-title">
                                                <a target="_blank" href="${pageContext.request.contextPath}/view?tab1=issue&action=retrieveAttachment&attachmentId=${attachment.id}" title="${attachment.dbFile.name}">
                                                    ${attachment.dbFile.shortName}
                                                </a>
                                            </p>
                                            <a href="#" class="card-header-icon" aria-label="delete" onclick="deleteAttachment('${attachment.id}');">
                                                <span class="icon">
                                                    <i class="fas fa-trash" aria-hidden="true"></i>
                                                </span>
                                            </a>
                                        </header>
                                        <div class="card-image has-text-centered">
                                            <%--<figure class="image">--%>
                                                <img src="${attachment.thumbnailDbFile.base64}" title="size ${attachment.dbFile.lengthPretty}" alt="${attachment.dbFile.name}">
                                            <%--</figure>--%>
                                        </div>
                                        <div class="card-content">
                                            <div class="content">
                                                <span><fmt:formatDate value="${attachment.createdOn}" pattern="h:mm a - dd/MMM/yy"/></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                        </c:forEach>
                    </div>

                    <%--<br>--%>
                    <table class="table">
                        <c:forEach var="attachment" items="${issue.attachments}">
                            <c:if test="${attachment.thumbnailDbFileId == 0}">
                                <tr>
                                    <td>
                                        <a target="_blank" href="${pageContext.request.contextPath}/view?tab1=issue&action=retrieveAttachment&attachmentId=${attachment.id}">
                                            <span class="icon">
                                                <i class="fas fa-${attachment.dbFile.previewIcon}" aria-hidden="true"></i>
                                            </span>
                                            ${attachment.dbFile.name}
                                        </a>
                                    </td>
                                    <td class="has-text-right">
                                        <span>${attachment.dbFile.lengthPretty}</span>
                                    </td>
                                    <td>
                                        <span><fmt:formatDate value="${attachment.createdOn}" pattern="dd/MMM/yy h:mm a"/></span>
                                    </td>
                                    <td>
                                        <a href="#" aria-label="delete" onclick="deleteAttachment('${attachment.id}');">
                                            <span class="icon">
                                                <i class="fas fa-trash" aria-hidden="true"></i>
                                            </span>
                                        </a>
                                    </td>
                                </tr>
                            </c:if>
                        </c:forEach>
                    </table>

                    <div id="attachmentActions" class="">
                        <input type="button" value="Add Attachment" id="showAddAttachment" class="button is-primary"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="columns is-multiline is-centered">
            <div class="column is-three-quarters">
                <div class="box">

                    <h2 class="subtitle">Description</h2>
                    <t:textToInputText id="fldDescription" text="${issue.description}" submitAction="/view?tab1=issue&action=update&issueId=${issue.id}"/>

                    <hr>
                    <h2 class="subtitle">Activity</h2>

                    <div id="tabs" class="tabs is-centered">
                        <ul>
                            <li data-tab="1" class="is-active"><a>Comments</a></li>
                            <li data-tab="2" id="changeLogNavTab"><a>Change Log</a></li>
                        </ul>
                    </div>

                    <div id="tab-content">
                        <div class="is-active" data-content="1">
                            <c:forEach var="comment" items="${comments}" varStatus="loop">
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
                                                    <a href="${pageContext.request.contextPath}/view?tab1=profile&action=form&userId=${comment.createdByUserId}">
                                                        ${comment.createdBy.name}
                                                    </a>
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

                                                on <fmt:formatDate value="${comment.createdOn}" pattern="dd/MMM/yy h:mm a"/>

                                                <br>
                                                <c:if test="${comment.createdByUserId == userSession.userId}">
                                                    <t:textToInputText id="fldContent${comment.id}" text="${comment.content}" submitAction="/view?tab1=issue&action=updateComment&commentId=${comment.id}"/>
                                                </c:if>
                                                <c:if test="${comment.createdByUserId != userSession.userId}">
                                                    <c:out value="${comment.content}"/>
                                                </c:if>
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
                                        <c:if test="${comment.createdByUserId == userSession.userId}">
                                            <button class="delete"></button>
                                        </c:if>
                                    </div>
                                </article>
                            </c:forEach>

                            <article class="media">
                                <figure class="media-left">
                                    <p class="image is-32x32">
                                        <img src="${!empty userSession.user.avatar.base64 ? userSession.user.avatar.base64 : userSession.user.defaultAvatar.base64}">
                                    </p>
                                </figure>
                                <div class="media-content">
                                    <div class="content">
                                        <div>
                                            <strong class="hasTooltip">
                                                <a href="${pageContext.request.contextPath}/view?tab1=profile&action=form&userId=${userSession.userId}">
                                                    ${userSession.user.name}
                                                </a>
                                            </strong>
                                            <div style="display: none;">
                                                <table>
                                                    <tr>
                                                        <td rowspan="2">
                                                            <img src="${!empty userSession.user.avatar.base64 ? userSession.user.avatar.base64 : userSession.user.defaultAvatar.base64}"
                                                                 style="height:36px;margin-right: 4px;border-radius: 3px;">
                                                        </td>
                                                        <td><b>${userSession.user.name}</b></td>
                                                    </tr>
                                                    <tr>
                                                        <td>${userSession.user.logonId}</td>
                                                    </tr>
                                                </table>
                                            </div>

                                            <form class="" id="frmNewComment" name="frmNewComment" method="post" action="${pageContext.request.contextPath}/view?tab1=issue&action=addComment&issueId=${issue.id}">
                                                <div class="field is-horizontal">
                                                    <div class="field-label is-normal">
                                                        <label class="label">Comment</label>
                                                    </div>
                                                    <div class="field-body">
                                                        <div class="field">
                                                            <div class="control">
                                                                <textarea class="textarea" rows="3" id="fldContent" name="fldContent" placeholder="Write comment here..."></textarea>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="field is-horizontal">
                                                    <div class="field-label">
                                                        <label class="label">Visibility</label>
                                                    </div>
                                                    <div class="field-body">
                                                        <div class="field is-narrow">
                                                            <div class="control">
                                                                <div class="select">
                                                                    <select id="fldVisibility" name="fldVisibility">
                                                                        <option value="">Default</option>
                                                                        <c:forEach var="group" items="${groups}">
                                                                            <option value="${group.id}">${group.name}</option>
                                                                        </c:forEach>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div id="activityActions" class="">
                                                    <input type="button" value="Add" id="submitAddComment" class="button is-primary"/>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </article>
                        </div>

                        <div class="" data-content="2">
                            <div id="changeLog">

                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="column">
                <div class="box">
                    <h2 class="subtitle">People</h2>

                    <div class="">
                        <table class="table">
                            <tr>
                                <td>Assignee:</td>
                                <td>
                                    <c:if test="${!empty issue.assignee}">
                                        <a href="${pageContext.request.contextPath}/view?tab1=profile&action=form&userId=${issue.assigneeUserId}">
                                            <img src="${issue.assignee.avatar.base64}" style="height:24px;margin-right: 4px;border-radius: 3px;"></a>

                                        <t:textToSelect id="fldAssigneeId" value="${issue.assigneeUserId}" text="${issue.assignee.name}" items="${potentialAssignees}" submitAction="/view?tab1=issue&action=update&issueId=${issue.id}"/>
                                    </c:if>
                                </td>
                            </tr>
                            <tr>
                                <td>Reporter:</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/view?tab1=profile&action=form&userId=${issue.reporterUserId}">
                                        <img src="${issue.reporter.avatar.base64}" style="height:24px;margin-right: 4px;border-radius: 3px;"></a>

                                    <t:textToSelect id="fldReporterId" value="${issue.reporterUserId}" text="${issue.reporter.name}" items="${potentialReporters}" submitAction="/view?tab1=issue&action=update&issueId=${issue.id}"/>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <hr>
                    <div class="">
                        <table>
                            <tr>
                                <td style="padding: 0 8px;vertical-align: top;">Watchers:</td>
                                <td>
                                    <c:if test="${!empty watcherMaps}">
                                        <a id="showWatchers" style="cursor: pointer">
                                            View</a>

                                        <div class="watchersDiv" style="display: none;">
                                            <table class="table">
                                                <c:forEach var="watcherMap" items="${watcherMaps}">
                                                    <tr>
                                                        <td>
                                                            <a style="" href="${pageContext.request.contextPath}/view?tab1=profile&action=form&userId=${watcherMap.watcher.id}">
                                                                <img src="${watcherMap.watcher.avatar.base64}" style="height:24px;margin-right: 4px;border-radius: 3px;">${watcherMap.watcher.name}
                                                            </a>
                                                        </td>
                                                        <td>
                                                            <a class="icon is-medium" href="${pageContext.request.contextPath}/view?tab1=issue&action=removeWatcher&issueId=${issue.id}&watcherMapId=${watcherMap.id}">
                                                                <span><i class="fas fa-trash fa-lg"></i></span>
                                                            </a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </table>
                                        </div>
                                        <br>
                                    </c:if>

                                    <c:if test="${!empty potentialWatchers}">
                                        Add:

                                        <div class="field has-addons">
                                            <div class="control">
                                                <select id="fldWatcher" style="z-index: 100" class="js-example-basic-single">
                                                    <c:forEach var="potentialWatchers" items="${potentialWatchers}">
                                                        <option value="${potentialWatchers.id}">${potentialWatchers.name}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                            <div class="control">
                                                <a class="button is-primary is-small" onclick="addWatcher('fldWatcher', '${issue.id}')">
                                                    <span class="icon is-small">
                                                        <i class="fas fa-plus"></i>
                                                    </span>
                                                </a>
                                            </div>
                                        </div>
                                    </c:if>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>


<div id="addAttachmentDialog" class="modal">
    <div class="modal-background"></div>
    <div class="modal-card">
        <header class="modal-card-head">
            <p class="modal-card-title">Add Attachment</p>
            <button class="delete" aria-label="close"></button>
        </header>
        <section class="modal-card-body">
            <form id="frmAddAttachment" name="frmAddAttachment" enctype="multipart/form-data" method="post" action="${pageContext.request.contextPath}/view?tab1=issue&action=addAttachment&issueId=${issue.id}">
                <div class="file">
                    <label class="file-label">
                        <input class="file-input" type="file" id="fldFile" name="fldFile" required>
                        <span class="file-cta">
                        <span class="file-icon">
                            <i class="fas fa-upload"></i>
                        </span>
                        <span class="file-label">
                            Choose a file…
                        </span>
                    </span>
                    </label>
                </div>
            </form>
        </section>
        <footer class="modal-card-foot">
            <button class="button is-success add">Add</button>
            <button class="button close">Cancel</button>
        </footer>
    </div>
</div>

<script>
    $(function () {
        var addAttachmentDialog = $('#addAttachmentDialog');
        var showDialogButton = $('#showAddAttachment');

        showDialogButton.on('click', function ()
        {
            addAttachmentDialog.toggleClass('is-active');
        });

        addAttachmentDialog.find('.add').on('click', function ()
        {
            if ($('#fldFile').val())
                $('#frmAddAttachment').submit()
        });

        addAttachmentDialog.find('.close').on('click', function ()
        {
            addAttachmentDialog.toggleClass('is-active');
        });

        addAttachmentDialog.find('button[class="delete"]').on('click', function () {
            addAttachmentDialog.toggleClass('is-active');
        })
    });
</script>

<jsp:include page="footer.jsp"/>
</body>
</html>