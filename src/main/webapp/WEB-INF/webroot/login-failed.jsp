<%--
  Created by IntelliJ IDEA.
  User: eric
  Date: 7/7/2016
  Time: 11:00 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="inc_title.jsp"/>
    <jsp:include page="inc_header.jsp"/>

    <script>
        $(document).ready(function ()
        {
          $('#backButton').focus();
        });
    </script>
</head>
<body>
<jsp:include page="header.jsp"/>

<div class="mdl-grid">
    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-shadow--2dp">

        <div class="mdl-card__title"><h5>Sorry, login failed!</h5></div>

        <div class="mdl-card__actions">
            <input type="submit" id="backButton" value="Back" class="mdl-button mdl-js-button mdl-button--raised" onclick="location.href='${pageContext.request.contextPath}/view?tab1=dashboard&action=form';"/>
        </div>
    </div>
</div>

<script>
    $(document).ready(showResponseMessage);
</script>
</body>
</html>
