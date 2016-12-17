<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="emails" type="java.util.List<com.hicks.beans.EmailMessage>" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <title>BTS</title>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/webroot/inc_header.jsp"/>

    <script>
        function deleteEmail(emailId)
        {
            if (confirm('Are you sure?'))
                location.href="${pageContext.request.contextPath}/view?tab1=admin&tab2=email&action=delete&emailId=" + emailId;
        }

        function goToEmailSettings()
        {
            location.href="${pageContext.request.contextPath}/view?tab1=admin&tab2=email&tab3=modify&action=form";
        }
    </script>
</head>
<body>

<jsp:include page="${pageContext.request.contextPath}/WEB-INF/webroot/header.jsp"/>

<div class="mdl-grid">

    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>Manage Emails</h5></div>

        <div class="tableContainer">
            <table class="mdl-data-table mdl-shadow--2dp">
                <thead>
                <tr>
                    <th>
                        <label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect mdl-data-table__select" for="table-header">
                            <input type="checkbox" id="table-header" class="mdl-checkbox__input" />
                        </label>
                    </th>
                    <th>Object Id</th>
                    <th class="mdl-data-table__cell--non-numeric">Status</th>
                    <th class="mdl-data-table__cell--non-numeric">User</th>
                    <th class="mdl-data-table__cell--non-numeric">action</th>
                    <th class="mdl-data-table__cell--non-numeric">actionSourceId</th>
                    <th class="mdl-data-table__cell--non-numeric">description</th>
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
                        <td>${email.id}</td>
                        <td class="mdl-data-table__cell--non-numeric" title="${email.status}">
                            <c:if test="${email.status == 'CREATED'}">
                                <i class="material-icons" style="color: blueviolet;">mail</i>
                            </c:if>
                            <c:if test="${email.status == 'WAITING'}">
                                <i class="material-icons" style="color: saddlebrown;">send</i>
                            </c:if>
                            <c:if test="${email.status == 'SENT'}">
                                <i class="material-icons" style="color: green;">done</i>
                            </c:if>
                            <c:if test="${email.status == 'FAILED'}">
                                <i class="material-icons" style="color: red;">error</i>
                            </c:if>
                        </td>
                        <td class="mdl-data-table__cell--non-numeric">${email.userId}</td>
                        <td class="mdl-data-table__cell--non-numeric">${email.action}</td>
                        <td class="mdl-data-table__cell--non-numeric">${email.actionSourceId}</td>
                        <td class="mdl-data-table__cell--non-numeric">${email.description}</td>
                        <td><a onclick="deleteEmail('${email.id}');" class="clickable material-icons">delete</a></td>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <div class="mdl-card__actions">
            <input id="sendTestEmailButton" type="button" value="Send Test Email" class="mdl-button mdl-js-button mdl-button--raised" />
            <input id="emailSettings" type="button" value="Email Settings" onclick="goToEmailSettings();" class="mdl-button mdl-js-button mdl-button--raised" />
        </div>
    </div>
</div>

<dialog id="sendTestEmailDialog" class="mdl-dialog">
    <h4 class="mdl-dialog__title">Send Test Email</h4>
    <div class="mdl-dialog__content">
        <form id="frmSendTestEmail" name="frmCreateEmail" method="post" action="${pageContext.request.contextPath}/view?tab1=admin&tab2=email&action=sendTest">
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
        <button type="button" class="mdl-button send">Send</button>
        <button type="button" class="mdl-button close">Cancel</button>
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

<jsp:include page="${pageContext.request.contextPath}/WEB-INF/webroot/footer.jsp"/>
</body>
</html>