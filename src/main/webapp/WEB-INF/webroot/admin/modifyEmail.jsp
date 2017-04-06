<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="btsSystem" type="net.ehicks.bts.beans.BtsSystem" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../inc_title.jsp"/>
    <jsp:include page="../inc_header.jsp"/>

    <script>
        $('document').ready(function ()
        {
            $('#emailhost').focus();
        })
    </script>
</head>
<body>

<jsp:include page="../header.jsp"/>

<div class="mdl-grid">

    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>Email Settings</h5></div>

        <form id="frmProject" method="post" action="${pageContext.request.contextPath}/view?tab1=admin&tab2=email&tab3=modify&action=modify">
            <div style="padding: 10px;">
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" id="emailHost" name="emailHost" type="text" value="${btsSystem.emailHost}"/>
                    <label class="mdl-textfield__label" for="emailHost">Host:</label>
                </div>
                <br>
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" id="emailUser" name="emailUser" type="text" value="${btsSystem.emailUser}"/>
                    <label class="mdl-textfield__label" for="emailUser">Username:</label>
                </div>
                <br>
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" id="emailPassword" name="emailPassword" type="text" value="${btsSystem.emailPassword}"/>
                    <label class="mdl-textfield__label" for="emailPassword">Password:</label>
                </div>
                <br>
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" id="emailPort" name="emailPort" type="text" value="${btsSystem.emailPort}"/>
                    <label class="mdl-textfield__label" for="emailPort">Port:</label>
                </div>
                <br>
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" id="emailFromAddress" name="emailFromAddress" type="text" value="${btsSystem.emailFromAddress}"/>
                    <label class="mdl-textfield__label" for="emailFromAddress">From Address:</label>
                </div>
                <br>
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" id="emailFromName" name="emailFromName" type="text" value="${btsSystem.emailFromName}"/>
                    <label class="mdl-textfield__label" for="emailFromName">From Name:</label>
                </div>
            </div>

            <div class="mdl-card__actions">
                <input id="saveProjectButton" type="submit" value="Save" class="mdl-button mdl-js-button mdl-button--raised" />
            </div>
        </form>
    </div>
</div>

<jsp:include page="../footer.jsp"/>
</body>
</html>