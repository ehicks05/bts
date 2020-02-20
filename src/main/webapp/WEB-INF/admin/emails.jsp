<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="emails" type="java.util.List<net.ehicks.bts.beans.EmailEvent>" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../inc_title.jsp"/>
    <jsp:include page="../inc_header.jsp"/>

    <script>
        function deleteEmail(emailId)
        {
            if (confirm('Are you sure?'))
                location.href="${pageContext.request.contextPath}/admin/email/delete?emailId=" + emailId;
        }
    </script>
</head>
<body>

<jsp:include page="../header.jsp"/>

<section class="hero is-primary is-small">
    <div class="hero-body">
        <div class="container">
            <h1 class="title">
                Manage Emails
            </h1>
        </div>
    </div>
</section>

<section class="section">
    <div class="container">
        <div class="columns is-multiline is-centered">
            <div class="column is-narrow">
                <table class="table is-striped is-narrow is-hoverable">
                    <thead>
                    <tr>
                        <th>
                            <label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect mdl-data-table__select" for="table-header">
                                <input type="checkbox" id="table-header" class="mdl-checkbox__input" />
                            </label>
                        </th>
                        <th class="has-text-right">Object Id</th>
                        <th class="has-text-centered">Status</th>
                        <th class="has-text-right">User</th>
                        <th>action</th>
                        <th class="has-text-right">issueId</th>
                        <th class="has-text-right">commentId</th>
                        <th>description</th>
                        <th>hardcoded to address</th>
                        <th></th>
                    </tr>
                    </thead>
                    <c:forEach var="email" items="${emails}" varStatus="loop">
                        <tr>
                            <td>
                                <label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect mdl-data-table__select" for="row[${loop.count}]">
                                    <input type="checkbox" id="row[${loop.count}]" class="mdl-checkbox__input" />
                                </label>
                            </td>
                            <td class="has-text-right"><a href="${pageContext.request.contextPath}/admin/email/preview/form?emailId=${email.id}">${email.id}</a></td>
                            <td class="has-text-centered" title="${email.status}">
                                <i class="fas fa-${email.statusIcon}"></i>
                            </td>
                            <td class="has-text-right">${email.user.id}</td>
                            <td>${email.emailAction.verb}</td>
                            <td class="has-text-right">${email.issue.id}</td>
                            <td class="has-text-right">${email.comment.id}</td>
                            <td>${email.description}</td>
                            <td>${email.toAddress}</td>
                            <td><a onclick="deleteEmail('${email.id}');"><i class="fas fa-trash"></i></a></td>
                        </tr>
                    </c:forEach>
                </table>

                <input id="sendTestEmailButton" type="button" value="Send Test Email" class="button" />
            </div>
        </div>
    </div>
</section>

<dialog id="sendTestEmailDialog" class="mdl-dialog">
    <h4 class="mdl-dialog__title">Send Test Email</h4>
    <div class="mdl-dialog__content">
        <form id="frmSendTestEmail" name="frmCreateEmail" method="post" action="${pageContext.request.contextPath}/admin/email/sendTest">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <table>
                <tr>
                    <td>To:</td>
                    <td>
                        <input type="text" id="fldTo" name="fldTo" size="20" maxlength="256" value="" required/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div class="mdl-dialog__actions">
        <button type="button" class="button is-primary send">Send</button>
        <button type="button" class="button close">Cancel</button>
    </div>
</dialog>
<script>
    var sendTestEmailDialog = document.querySelector('#sendTestEmailDialog');
    var sendTestEmailButton = document.querySelector('#sendTestEmailButton');
    if (!sendTestEmailDialog.showModal)
    {
        dialogPolyfill.registerDialog(sendTestEmailDialog);
    }
    sendTestEmailButton.addEventListener('click', function ()
    {
        sendTestEmailDialog.showModal();
    });
    document.querySelector('#sendTestEmailDialog .send').addEventListener('click', function ()
    {
        if (!document.querySelector('#fldTo').value)
            alert('Please enter an email recipient.');
        else
            $('#frmSendTestEmail').submit();
    });
    sendTestEmailDialog.querySelector('#sendTestEmailDialog .close').addEventListener('click', function ()
    {
        sendTestEmailDialog.close();
    });
</script>

<jsp:include page="../footer.jsp"/>
</body>
</html>