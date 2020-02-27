<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>

<table id="issueAuditTable" class="table is-striped is-narrow is-hoverable is-fullwidth">
    <thead>
    <tr>
        <th class="has-text-right">Date</th>
        <th>Changed By</th>
        <th>Issue</th>
        <th>Fields Changed</th>
        <th>Description</th>
    </tr>
    </thead>

    <tbody id="myTBody">
    <c:forEach var="issueEvent" items="${issueEvents}" varStatus="loop">
        <tr>
            <td class="has-text-right"><javatime:format value="${issueEvent.createdOn}" style="MS"/></td>
            <td>${issueEvent.user.username}</td>
            <td>${issueEvent.issue.key}</td>
            <td>${issueEvent.propertyName}</td>
            <td>
                <c:forEach var="diff" items="${issueEvent.renderMap.diffs}">
                    <c:if test="${diff.operation == 'INSERT'}"><ins style="background:#e6ffe6;">${diff.text}</ins></c:if>
                    <c:if test="${diff.operation == 'DELETE'}"><del style="background:#ffe6e6;">${diff.text}</del></c:if>
                    <c:if test="${diff.operation == 'EQUAL'}"><span>${diff.text}</span></c:if>
                </c:forEach>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>