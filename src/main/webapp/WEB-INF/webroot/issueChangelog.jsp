<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="issueAudits" type="java.util.List<net.ehicks.bts.beans.IssueAudit>" scope="request"/>

<table id="filmTable" class="table is-striped is-narrow is-hoverable is-fullwidth">
    <thead>
    <tr>
        <th>User</th>
        <th>Event Time</th>
        <th>Event Type</th>
        <th>Object Key</th>
        <th>Field Name</th>
        <th>Old Value</th>
        <th>New Value</th>
    </tr>
    </thead>

    <tbody id="myTBody">
    <c:forEach var="audit" items="${issueAudits}" varStatus="loop">
        <tr>
            <td>${audit.userName}</td>
            <td class="has-text-right"><fmt:formatDate value="${audit.eventTime}" pattern="dd/MMM/yy hh:mm a" /></td>
            <td>${audit.eventType}</td>
            <td>${audit.objectKey}</td>
            <td>${audit.fieldName}</td>
            <td>${audit.oldValue}</td>
            <td>${audit.newValue}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>