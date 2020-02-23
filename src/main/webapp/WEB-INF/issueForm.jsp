<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>

<jsp:useBean id="issue" type="net.ehicks.bts.beans.Issue" scope="request"/>
<jsp:useBean id="comments" type="java.util.Set<net.ehicks.bts.beans.Comment>" scope="request"/>
<jsp:useBean id="potentialWatchers" type="java.util.List<net.ehicks.bts.beans.User>" scope="request"/>
<jsp:useBean id="potentialAssignees" type="java.util.List<net.ehicks.bts.beans.User>" scope="request"/>
<jsp:useBean id="potentialReporters" type="java.util.List<net.ehicks.bts.beans.User>" scope="request"/>
<jsp:useBean id="groups" type="java.util.List<net.ehicks.bts.beans.Group>" scope="request"/>
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

            $('#showAddWatcher').each(function() {
                $(this).qtip({
                    content: {
                        text: $(this).next('.potentialWatchersDiv')
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
            data['${_csrf.parameterName}'] = '${_csrf.token}';
            $.post(url, data, function (data, textStatus)
            {
                if (!data)
                    return 'ok';
                
                var notification = document.querySelector('#ajax-update-notification');

                if (textStatus === 'success')
                {
                    notification.className = 'notification is-success';
                    notification.innerHTML = '<button class="delete"></button>Success: ' + data;
                }
                else
                {
                    notification.className = 'notification is-danger';
                    notification.innerHTML = 'Failed: ' + data;
                }

                setTimeout(function () {
                    notification.className = 'notification is-hidden';
                }, 3000);

                return textStatus;
            });
        }

        function addWatcher(elementId, issueId)
        {
            var userId = $('#' + elementId).val();
            location.href = "${pageContext.request.contextPath}/issue/addWatcher?issueId=${issue.id}&userId=" + userId;
        }

        function deleteAttachment(attachmentId)
        {
            if (confirm('Are you sure?'))
                location.href="${pageContext.request.contextPath}/issue/deleteAttachment?issueId=${issue.id}&attachmentId=" + attachmentId;
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

            var url = '${pageContext.request.contextPath}/issue/ajaxGetChangeLog?issueId=${issue.id}';
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

        .issueSubheading {font-weight:bold; margin-bottom: 8px;}
    </style>
</head>
<body>

<jsp:include page="header.jsp"/>

<section class="hero is-primary is-small">
    <div class="hero-body">
        <div class="container">
            <h1 class="title">
                <t:textToInputText tag="p" style="display:inline;" editableClass="editableHero" clazz="title" id="fldTitle" text="${issue.title}" submitAction="/issue/update?issueId=${issue.id}"/>
            </h1>
            <h3 class="subtitle">${issue.project.prefix}-${issue.id}</h3>
        </div>
    </div>
</section>

<div class="container">
    <div class="columns is-multiline is-centered">
        <div class="column is-one-quarter">
            <div id="ajax-update-notification" class="notification is-hidden" style="position: absolute;z-index: 10;">
                <button class="delete"></button>
            </div>
        </div>
    </div>
</div>

<section class="">
    <div class="container">
        <div class="columns is-multiline is-centered">
            <div class="column">
                <div class="box">
                    <h2 class="issueSubheading">Details</h2>

                    <form name="frmSave" id="frmSave" method="post" action="${pageContext.request.contextPath}/modify/save">
                        <div class="columns is-multiline">
                            <div class="column" style="white-space: nowrap">
                                <table class="table is-narrow">
                                    <tr>
                                        <td>Project:</td>
                                        <td>
                                            <t:textToSelect id="fldProject" tag="span" myClass="" value="${issue.project.id}" text="${issue.project.name}" items="${projects}"
                                                            submitAction="/issue/update?issueId=${issue.id}"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>Type:</td>
                                        <td>
                                            <t:textToSelect id="fldIssueType" value="${issue.issueType.id}" text="${issue.issueType.name}" items="${issueTypes}"
                                                            submitAction="/issue/update?issueId=${issue.id}"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>Group:</td>
                                        <td>
                                            <t:textToSelect id="fldGroup" value="${issue.group.id}" text="${issue.group.name}" items="${groups}"
                                                            submitAction="/issue/update?issueId=${issue.id}"/>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div class="column" style="white-space: nowrap">
                                <table class="table is-narrow">
                                    <tr>
                                        <td>Severity:</td>
                                        <td>
                                            <t:textToSelect id="fldSeverity" value="${issue.severity.id}" text="${issue.severity.name}" items="${severities}"
                                                            submitAction="/issue/update?issueId=${issue.id}"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>Status:</td>
                                        <td>
                                            <t:textToSelect id="fldStatus" value="${issue.status.id}" text="${issue.status.name}" items="${statuses}"
                                                            submitAction="/issue/update?issueId=${issue.id}"/>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </form>

                    <h2 class="issueSubheading">Attachments</h2>

                    <div class="columns is-multiline">
                        <c:forEach var="attachment" items="${issue.attachments}">
                            <c:if test="${!empty attachment.dbFile.thumbnail}">
                                <div class="column is-narrow">
                                    <div class="">
                                        <a target="_blank" href="${pageContext.request.contextPath}/attachment/${attachment.id}" title="${attachment.name}">
                                            <figure class="image is-64x64">
                                                <img src="${pageContext.request.contextPath}/attachment/${attachment.id}"
                                                     title="${attachment.name}" alt="${attachment.name}">
                                            </figure>
                                        </a>
                                    </div>
                                </div>
                            </c:if>
                        </c:forEach>
                    </div>

                    <table class="table is-narrow">
                        <c:forEach var="attachment" items="${issue.attachments}">
                            <c:if test="${empty attachment.dbFile.thumbnail}">
                                <tr>
                                    <td>
                                        <a target="_blank" href="${pageContext.request.contextPath}/attachment/${attachment.id}">
                                            <span class="icon">
                                                <i class="fas ${attachment.dbFile.previewIcon}" aria-hidden="true"></i>
                                            </span>
                                                ${attachment.name}
                                        </a>
                                    </td>
                                    <td class="has-text-right">
                                        <span>${attachment.dbFile.lengthPretty}</span>
                                    </td>
                                    <td>
                                        <span><javatime:format value="${attachment.createdOn}" style="MS" /></span>
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


                    <h2 class="issueSubheading">Description</h2>
                    <t:textToInputText id="fldDescription" text="${issue.description}" submitAction="/issue/update?issueId=${issue.id}"/>
                    <br />

                    <h2 class="issueSubheading">Activity</h2>

                    <div id="tabs" class="tabs is-small">
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
                                            <img src="${pageContext.request.contextPath}/avatar/${comment.author.avatar.id}">
                                        </p>
                                    </figure>
                                    <div class="media-content">
                                        <div class="content">
                                            <div>
                                                <strong class="hasTooltip">
                                                    <a href="${pageContext.request.contextPath}/profile/form?profileUserId=${comment.author.id}">
                                                            ${comment.author.name}
                                                    </a>
                                                </strong>
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

                                                on <javatime:format value="${comment.createdOn}" style="MS" />

                                                <br>
                                                <c:if test="${comment.author.id == pageContext.request.userPrincipal.principal.id}">
                                                    <t:textToInputText id="fldContent${comment.id}" text="${comment.content}" submitAction="/issue/updateComment?commentId=${comment.id}"/>
                                                </c:if>
                                                <c:if test="${comment.author.id != pageContext.request.userPrincipal.principal.id}">
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
                                        <c:if test="${comment.visibleToGroup.id != comment.issue.group.id}">
                                            <span class="icon is-small has-text-danger" title="Visible to ${comment.visibleToGroup.name}">
                                                <i class="fas fa-user-secret"></i>
                                            </span>
                                        </c:if>
                                        <c:if test="${comment.author.id == pageContext.request.userPrincipal.principal.id}">
                                            <button class="delete"></button>
                                        </c:if>
                                    </div>
                                </article>
                            </c:forEach>

                            <c:set var="principal" value="${pageContext.request.userPrincipal.principal}" />
                            <article class="media">
                                <figure class="media-left">
                                    <p class="image is-32x32">
                                        <img src="${pageContext.request.contextPath}/avatar/${principal.avatar.id}">
                                    </p>
                                </figure>
                                <div class="media-content">
                                    <div class="content">
                                        <form id="frmNewComment" name="frmNewComment" method="post" action="${pageContext.request.contextPath}/issue/addComment?issueId=${issue.id}">
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                            <t:textarea id="fldContent" label="Comment" placeholder="Write a comment here..." horizontal="false" labelClass="has-text-left"/>
                                            <t:basicSelect id="fldVisibility" label="Visibility" items="${groups}" blankLabel="Default" horizontal="false" labelClass="has-text-left" />

                                            <input type="button" value="Add" id="submitAddComment" class="button is-primary is-small"/>
                                        </form>
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
            <div class="column is-one-third">
                <div class="box">
                    <h2 class="issueSubheading">People</h2>

                    <table class="table is-narrow">
                        <tr>
                            <td>Assignee:</td>
                            <td>
                                <c:if test="${!empty issue.assignee}">
                                    <a href="${pageContext.request.contextPath}/profile/form?profileUserId=${issue.assignee.id}">
                                        <img src="${pageContext.request.contextPath}/avatar/${issue.assignee.avatar.id}" class="image" style="display:inline; height:24px;margin-right: 4px;border-radius: 3px;">
                                    </a>

                                    <t:textToSelect id="fldAssigneeId" value="${issue.assignee.id}" text="${issue.assignee.name}" items="${potentialAssignees}"
                                                    submitAction="/issue/update?issueId=${issue.id}"/>
                                </c:if>
                            </td>
                        </tr>
                        <tr>
                            <td>Reporter:</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/profile/form?profileUserId=${issue.reporter.id}">
                                    <img src="${pageContext.request.contextPath}/avatar/${issue.reporter.avatar.id}" style="height:24px;margin-right: 4px;border-radius: 3px;"></a>

                                <t:textToSelect id="fldReporterId" value="${issue.reporter.id}" text="${issue.reporter.name}" items="${potentialReporters}"
                                                submitAction="/issue/update?issueId=${issue.id}"/>
                            </td>
                        </tr>
                        <tr>
                            <td style="padding: 0 8px;vertical-align: top;">Watchers:</td>
                            <td>
                                <c:if test="${!empty issue.watchers}">
                                    <a id="showWatchers" style="cursor: pointer">
                                        <span class="tag is-rounded">
                                            ${issue.watchers.size()}
                                        </span>
                                    </a>

                                    <div class="watchersDiv" style="display: none;">
                                        <table class="table">
                                            <c:forEach var="watcher" items="${issue.watchers}">
                                                <tr>
                                                    <td>
                                                        <a style="" href="${pageContext.request.contextPath}/profile/form?profileUserId=${watcher.id}">
                                                            <img src="${pageContext.request.contextPath}/avatar/${watcher.avatar.id}" style="height:24px;margin-right: 4px;border-radius: 3px;">${watcher.name}
                                                        </a>
                                                    </td>
                                                    <td>
                                                        <a class="icon is-medium" href="${pageContext.request.contextPath}/issue/removeWatcher?issueId=${issue.id}&profileUserId=${watcher.id}">
                                                            <span><i class="fas fa-trash fa-lg"></i></span>
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </table>
                                    </div>
                                    <br>
                                </c:if>
                            </td>
                        </tr>
                    </table>

                    <h2 class="issueSubheading">Dates</h2>

                    <table class="table is-narrow">
                        <tr>
                            <td>Created:</td>
                            <td title="<javatime:format value="${issue.createdOn}" style="MS" />">
                                ${issue.timeSinceCreation.toLowerCase()} ago
                            </td>
                        </tr>
                        <tr>
                            <td>Updated:</td>
                            <td title="<javatime:format value="${issue.lastUpdatedOn}" style="MS" />">
                                ${issue.timeSinceUpdate.toLowerCase()} ago
                            </td>
                        </tr>
                    </table>

                    <h2 class="issueSubheading">Actions</h2>

                    <span id="showAddAttachment" class="button is-small" title="Add Attachment">
                        <span class="icon is-small">
                            <i class="fas fa-paperclip" aria-hidden="true"></i>
                        </span>
                    </span>

                    <span id="showAddWatcher" class="button is-small" title="Add Watcher">
                        <span class="icon is-small">
                            <i class="fas fa-eye" aria-hidden="true"></i>
                        </span>
                    </span>

                    <div class="potentialWatchersDiv" style="display: none;">
                        <c:if test="${!empty potentialWatchers}">
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
                    </div>

                </div>
            </div>
        </div>
    </div>
</section>
<br />

<div id="addAttachmentDialog" class="modal">
    <div class="modal-background"></div>
    <div class="modal-card">
        <header class="modal-card-head">
            <p class="modal-card-title">Add Attachment</p>
            <button class="delete" aria-label="close"></button>
        </header>
        <section class="modal-card-body">
            <form id="frmAddAttachment" name="frmAddAttachment" enctype="multipart/form-data" method="post" action="${pageContext.request.contextPath}/issue/addAttachment?issueId=${issue.id}&${_csrf.parameterName}=${_csrf.token}">
                <div class="file has-name">
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
                        <span id="uploadFilename" class="file-name">

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
        var file = document.getElementById("fldFile");
        file.onchange = function() {
            if (file.files.length > 0)
            {
                document.getElementById('uploadFilename').innerHTML = file.files[0].name;
            }
        };
    });

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