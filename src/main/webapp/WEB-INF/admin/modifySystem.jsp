<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../inc_title.jsp"/>
    <jsp:include page="../inc_header.jsp"/>

    <script>

    </script>
</head>
<body>

<jsp:include page="../header.jsp"/>

<section class="hero is-primary is-small">
    <div class="hero-body">
        <div class="container">
            <h1 class="title">
                Modify System
            </h1>
        </div>
    </div>
</section>

<section class="section">
    <div class="container">
        <div class="columns is-multiline is-centered">
            <div class="column">
                <form id="frmProject" method="post" action="${pageContext.request.contextPath}/admin/system/modify/modify">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <t:text id="siteName" label="Site Name" value="${btsSystem.siteName}" />
                    <t:textarea id="logonMessage" label="Logon Message" value="${btsSystem.logonMessage}" horizontal="true" labelClass=""/>
                    <t:text id="defaultAvatar" label="Default Avatar" value="${btsSystem.defaultAvatar.id}"/>
                    <t:basicSelect id="theme" label="Theme" items="${themes}" value="${btsSystem.theme}" horizontal="true" required="true"/>
                    <t:text id="emailFromAddress" label="Email 'From' Address" value="${btsSystem.emailFromAddress}"/>
                    <t:text id="emailFromName" label="Email 'From' Name" value="${btsSystem.emailFromName}"/>

                    <div class="field is-horizontal">
                        <div class="field-label"></div>
                        <div class="field-body">
                            <div class="control">
                                <input id="saveSystemButton" type="submit" value="Save" class="button is-primary" />
                                <input id="sendTestEmailButton" type="button" value="Send Test Email" class="button" />
                            </div>
                        </div>
                    </div>
                </form>
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