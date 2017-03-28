<%--
  Created by IntelliJ IDEA.
  User: eric
  Date: 7/7/2016
  Time: 10:59 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="inc_title.jsp"/>
    <jsp:include page="inc_header.jsp"/>

    <script>

        function initForm()
        {
            $('#j_username').focus();
        }

        window.onload = initForm;
    </script>
</head>
<body>
<jsp:include page="header.jsp"/>

<div class="mdl-grid">
    <div style="margin: 8px auto" class="mdl-card mdl-cell mdl-cell--4-col mdl-cell--4-col-tablet mdl-shadow--2dp">
        <form method="POST" action="j_security_check">

            <div class="mdl-card__title"><h5>Log in to ${applicationScope['systemInfo'].appName}</h5></div>

            <div class="mdl-card__supporting-text">

                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" type="emailMessage" size="20" maxlength="32" id="j_username" name="j_username">
                    <label class="mdl-textfield__label" for="j_username">Email</label>
                </div>
                <br>
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" type="password" size="20" maxlength="32" id="j_password" name="j_password">
                    <label class="mdl-textfield__label" for="j_password">Password</label>
                </div>

            </div>

            <div class="mdl-card__actions">
                <input type="submit" value="Log In" class="mdl-button mdl-js-button mdl-button--raised"/>
            </div>
        </form>
    </div>

    <div style="margin: 8px auto" class="mdl-card mdl-cell mdl-cell--8-col mdl-cell--4-col-tablet mdl-shadow--2dp">

        <div class="mdl-card__title"><h5>Welcome Message</h5></div>
        <div class="mdl-card__supporting-text">
            ${applicationScope['btsSystem'].logonMessage}
    </div>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>
