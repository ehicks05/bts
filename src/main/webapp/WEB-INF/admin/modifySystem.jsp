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
                    <t:text id="siteName" label="Site Name" value="${btsSystem.siteName}" isSpring="false" isStatic="false" required="false" horizontal="false" />
                    <t:textarea id="logonMessage" label="Logon Message" placeholder="Logon Message" value="${btsSystem.logonMessage}" horizontal="false" labelClass=""/>
                    <t:text id="defaultAvatar" label="Default Avatar" value="${btsSystem.defaultAvatar.id}" isSpring="false" isStatic="false" required="false" horizontal="false"/>
                    <t:basicSelect id="theme" label="Theme" items="${themes}" value="${btsSystem.theme}" horizontal="false" required="true"/>
                    <t:text id="emailFromAddress" label="Email 'From' Address" value="${btsSystem.emailFromAddress}" isSpring="false" isStatic="false" required="false" horizontal="false"/>
                    <t:text id="emailFromName" label="Email 'From' Name" value="${btsSystem.emailFromName}" isSpring="false" isStatic="false" required="false" horizontal="false"/>

                    <div class="buttons">
                        <input id="saveSystemButton" type="submit" value="Save" class="button is-primary" />
                        <input id="sendTestEmailButton" type="button" value="Send Test Email" class="button" />
                    </div>
                </form>
            </div>
        </div>
    </div>
</section>

<div id="sendTestEmailDialog" class="modal">
    <div class="modal-background"></div>
    <div class="modal-card">
        <header class="modal-card-head">
            <p class="modal-card-title">Send Test Email</p>
            <button class="delete close" aria-label="close"></button>
        </header>
        <section class="modal-card-body">
            <div>
                <form id="frmSendTestEmail" name="frmCreateEmail" method="post" action="${pageContext.request.contextPath}/admin/email/sendTest">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <table>
                        <tr>
                            <t:text id="fldTo" label="Recipient" horizontal="true" value="" required="true" isSpring="false" isStatic="false" />
                        </tr>
                    </table>
                </form>
            </div>
        </section>
        <footer class="modal-card-foot">
            <button class="button is-success send">Send</button>
            <button class="button close">Cancel</button>
        </footer>
    </div>
</div>
<script>
    const dialog = document.querySelector('#sendTestEmailDialog');
    const button = document.querySelector('#sendTestEmailButton');

    button.addEventListener('click', toggleDialog);
    document.querySelector('#sendTestEmailDialog .send').addEventListener('click', function ()
    {
        if (!document.querySelector('#fldTo').value)
            alert('Please enter an email recipient.');
        else
            $('#frmSendTestEmail').submit();
    });
    dialog.querySelectorAll('#sendTestEmailDialog .close').forEach(el => el.addEventListener('click', toggleDialog));

    function toggleDialog() {
        document.querySelector('#sendTestEmailDialog').classList.toggle('is-active');
    }
</script>

<jsp:include page="../footer.jsp"/>
</body>
</html>