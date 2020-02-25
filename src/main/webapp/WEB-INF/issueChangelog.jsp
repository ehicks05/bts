<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>

<table id="issueAuditTable" class="table is-striped is-narrow is-hoverable is-fullwidth">
    <thead>
    <tr>
        <th>Changed By</th>
        <th class="has-text-right">Date</th>
        <th>Issue</th>
        <th>Fields Changed</th>
        <th>Description</th>
    </tr>
    </thead>

    <tbody id="myTBody">
    <c:forEach var="issueAudit" items="${issueAudits}" varStatus="loop">
        <tr>
            <td>${issueAudit.myRevisionEntity.username}</td>
            <td class="has-text-right"><fmt:formatDate value="${issueAudit.myRevisionEntity.revisionDate}" pattern="dd/MMM/yy hh:mm a" /></td>
            <td>${issueAudit.after.project.prefix}-${issueAudit.after.id}</td>
            <td>${issueAudit.changedProperties}</td>
            <td>${issueAudit.description}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>