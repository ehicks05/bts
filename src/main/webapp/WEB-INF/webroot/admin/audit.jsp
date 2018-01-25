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

<section class="hero is-primary is-small">
    <div class="hero-body">
        <div class="container">
            <h1 class="title">
                Audit Records
            </h1>
        </div>
    </div>
</section>

<section class="section">
    <div class="container">
        <div class="columns is-multiline is-centered">
            <div class="column is-one-fifth">
                <form name="frmFilter" id="frmFilter" method="post" action="${pageContext.request.contextPath}/view?tab1=admin&tab2=audit&action=search">
                    <input type="hidden" name="sortColumn" id="sortColumn" value="${auditForm.sortColumn}"/>
                    <input type="hidden" name="sortDirection" id="sortDirection" value="${auditForm.sortDirection}"/>
                    <input type="hidden" name="page" id="page" value="${auditForm.page}"/>

                    <nav class="panel">
                        <p class="panel-heading">
                            Search for Audit Records
                        </p>
                        <div class="panel-block">
                            <div class="field">
                                <label class="label" for="objectKey">Object Key</label>
                                <div class="control">
                                    <input class="input" type="text" placeholder="Object Key" id="objectKey" name="objectKey" value="${auditForm.objectKey}">
                                </div>
                            </div>
                        </div>
                        <div class="panel-block">
                            <div class="field">
                                <label class="label" for="fieldName">Field Name</label>
                                <div class="control">
                                    <input class="input" type="text" placeholder="Field Name" id="fieldName" name="fieldName" value="${auditForm.fieldName}">
                                </div>
                            </div>
                        </div>
                        <div class="panel-block">
                            <div class="field">
                                <label class="label" for="fromEventTime">From Event Time</label>
                                <div class="control">
                                    <fmt:formatDate var="fromEvent" value="${auditForm.fromEventTime}" pattern="MM/dd/yyyy"/>
                                    <input class="input" type="text" placeholder="From Event Time" id="fromEventTime" name="fromEventTime" value="${fromEvent}">
                                </div>
                            </div>
                        </div>
                        <div class="panel-block">
                            <div class="field">
                                <label class="label" for="toEventTime">To Event Time</label>
                                <div class="control">
                                    <fmt:formatDate var="toEvent" value="${auditForm.toEventTime}" pattern="MM/dd/yyyy"/>
                                    <input class="input" type="text" placeholder="To Event Time" id="toEventTime" name="toEventTime" value="${toEvent}">
                                </div>
                            </div>
                        </div>

                        <div class="panel-block">
                            <div class="field">
                                <label class="label" for="eventType">Event Type</label>
                                <div class="control">
                                    <t:multiSelect id="eventType" selectedValues="${auditForm.eventType}" items="${ct:stringToISelectTag('INSERT,UPDATE,DELETE')}" placeHolder=""/>
                                </div>
                            </div>
                        </div>

                        <div class="panel-block">
                            <input type="submit" value="Search" class="button is-link is-outlined is-fullwidth" />
                        </div>
                    </nav>
                </form>
            </div>
            <div class="column">
                <p class="panel-heading">
                    Found ${searchResult.size} Audit Records
                </p>

                <div class="tableContainer">
                    <jsp:include page="../auditTable.jsp"/>
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="../footer.jsp"/>
</body>
</html>