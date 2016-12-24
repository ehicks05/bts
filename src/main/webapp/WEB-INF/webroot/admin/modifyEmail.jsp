<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="userSession" type="net.ehicks.bts.UserSession" scope="session"/>

<!DOCTYPE html>
<html>
<head>
    <title>BTS</title>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/webroot/inc_header.jsp"/>

    <script>
        $('document').ready(function ()
        {
            $('#emailhost').focus();
        })
    </script>
</head>
<body>

<jsp:include page="${pageContext.request.contextPath}/WEB-INF/webroot/header.jsp"/>

<div class="mdl-grid">

    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>Email Settings</h5></div>

        <form id="frmProject" method="post" action="${pageContext.request.contextPath}/view?tab1=admin&tab2=email&tab3=modify&action=modify">
            <div style="padding: 10px;">
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" id="emailhost" name="emailhost" type="text" value="${userSession.systemInfo.emailHost}"/>
                    <label class="mdl-textfield__label" for="emailhost">Host:</label>
                </div>
                <br>
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" id="Username" name="Username" type="text" value="${userSession.systemInfo.emailUser}"/>
                    <label class="mdl-textfield__label" for="Username">Username:</label>
                </div>
                <br>
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" id="emailPassword" name="emailPassword" type="text" value="${userSession.systemInfo.emailPassword}"/>
                    <label class="mdl-textfield__label" for="emailPassword">Password:</label>
                </div>
                <br>
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" id="emailPort" name="emailPort" type="text" value="${userSession.systemInfo.emailPort}"/>
                    <label class="mdl-textfield__label" for="emailPort">Port:</label>
                </div>
            </div>

            <div class="mdl-card__actions">
                <input id="saveProjectButton" type="submit" value="Save" class="mdl-button mdl-js-button mdl-button--raised" />
            </div>
        </form>
    </div>
</div>

<jsp:include page="${pageContext.request.contextPath}/WEB-INF/webroot/footer.jsp"/>
</body>
</html>