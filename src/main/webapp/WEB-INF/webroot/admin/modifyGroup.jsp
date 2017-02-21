<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="group" type="net.ehicks.bts.beans.Group" scope="request"/>

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

<div class="mdl-grid">

    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>Modify Group ${group.name}</h5></div>

        <form id="frmGroup" method="post" action="${pageContext.request.contextPath}/view?tab1=admin&tab2=groups&tab3=modify&action=modify&groupId=${group.id}">
            <div style="padding: 10px;">
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" id="groupId" name="groupId" type="text" value="${group.id}" readonly/>
                    <label class="mdl-textfield__label" for="groupId">Group Id:</label>
                </div>
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" id="name" name="name" type="text" value="${group.name}"/>
                    <label class="mdl-textfield__label" for="name">Name:</label>
                </div>
            </div>

            <div class="mdl-card__actions">
                <input id="saveGroupButton" type="submit" value="Save" class="mdl-button mdl-js-button mdl-button--raised" />
            </div>
        </form>
    </div>
</div>

<jsp:include page="../footer.jsp"/>
</body>
</html>