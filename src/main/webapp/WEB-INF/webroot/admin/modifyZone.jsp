<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="zone" type="net.ehicks.bts.beans.Zone" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <title>BTS</title>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/webroot/inc_header.jsp"/>

    <script>

    </script>
</head>
<body>

<jsp:include page="${pageContext.request.contextPath}/WEB-INF/webroot/header.jsp"/>

<div class="mdl-grid">

    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>Modify Zone ${zone.name}</h5></div>

        <form id="frmZone" method="post" action="${pageContext.request.contextPath}/view?tab1=admin&tab2=zones&tab3=modify&action=modify&zoneId=${zone.id}">
            <div style="padding: 10px;">
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" id="zoneId" name="zoneId" type="text" value="${zone.id}" readonly/>
                    <label class="mdl-textfield__label" for="zoneId">Zone Id:</label>
                </div>
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" id="name" name="name" type="text" value="${zone.name}"/>
                    <label class="mdl-textfield__label" for="name">Name:</label>
                </div>
            </div>

            <div class="mdl-card__actions">
                <input id="saveZoneButton" type="submit" value="Save" class="mdl-button mdl-js-button mdl-button--raised" />
            </div>
        </form>
    </div>
</div>

<jsp:include page="${pageContext.request.contextPath}/WEB-INF/webroot/footer.jsp"/>
</body>
</html>