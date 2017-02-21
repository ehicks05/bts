<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="userSession" type="net.ehicks.bts.UserSession" scope="session"/>
<jsp:useBean id="auditForm" type="net.ehicks.bts.AuditForm" scope="request"/>
<jsp:useBean id="searchResult" type="net.ehicks.bts.SearchResult" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../inc_title.jsp"/>
    <jsp:include page="../inc_header.jsp"/>
</head>
<body>

<jsp:include page="../header.jsp"/>

<div class="mdl-grid">

    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>Audit Records</h5></div>

        <form name="frmFilter" id="frmFilter" method="post" action="${pageContext.request.contextPath}/view?tab1=admin&tab2=audit&action=search">
            <input type="hidden" name="sortColumn" id="sortColumn" value="${auditForm.sortColumn}"/>
            <input type="hidden" name="sortDirection" id="sortDirection" value="${auditForm.sortDirection}"/>
            <input type="hidden" name="page" id="page" value="${auditForm.page}"/>

            <div style="padding: 10px;">

                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" type="text" size="20" maxlength="32" id="objectKey" name="objectKey" value="${auditForm.objectKey}">
                    <label class="mdl-textfield__label" for="objectKey">Object Key:</label>
                </div>
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" type="text" size="20" maxlength="32" id="fieldName" name="fieldName" value="${auditForm.fieldName}">
                    <label class="mdl-textfield__label" for="fieldName">Field Name:</label>
                </div>
                <br>
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <fmt:formatDate var="fromEvent" value="${auditForm.fromEventTime}" pattern="MM/dd/yyyy"/>
                    <input class="mdl-textfield__input" type="text" size="20" maxlength="32" id="fromEventTime" name="fromEventTime" value="${fromEvent}">
                    <label class="mdl-textfield__label" for="fromEventTime">From Event Time:</label>
                </div>
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <fmt:formatDate var="toEvent" value="${auditForm.toEventTime}" pattern="MM/dd/yyyy"/>
                    <input class="mdl-textfield__input" type="text" size="20" maxlength="32" id="toEventTime" name="toEventTime" value="${toEvent}">
                    <label class="mdl-textfield__label"  for="toEventTime">To Event Time:</label>
                </div>

                <br>
                <t:multiSelect id="eventType" selectedValues="${auditForm.eventType}" items="${ct:stringToISelectTag('INSERT,UPDATE,DELETE')}" placeHolder="Event Type:"/>
                <br>
            </div>
            <div class="mdl-card__actions">
                <input type="submit" value="Search" class="mdl-button mdl-js-button mdl-button--raised" />
            </div>
        </form>
    </div>

    <br>
    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>Found ${searchResult.size} Audit Records</h5></div>

        <div class="tableContainer">
            <jsp:include page="../auditTable.jsp"/>
        </div>
    </div>
</div>

<jsp:include page="../footer.jsp"/>
</body>
</html>