<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<jsp:useBean id="auditForm" type="net.ehicks.bts.AuditForm" scope="request"/>
<jsp:useBean id="searchResult" type="net.ehicks.bts.SearchResult<org.springframework.data.history.Revision<java.lang.Integer, net.ehicks.bts.beans.Issue>>" scope="request"/>

<table id="issueTable" class="table is-striped is-narrow is-hoverable is-fullwidth">
    <thead>
    <tr class="listheading">
        <td>Revision</td>
        <td>Revision Instant</td>
        <t:sortableCell code="id" label="ID" style="text-align:right;" cssClass="has-text-right" searchForm="${auditForm}" />
        <t:sortableCell code="title" label="Title" searchForm="${auditForm}"/>
        <t:sortableCell code="severity" label="Severity" searchForm="${auditForm}"/>
        <t:sortableCell code="status" label="Status" searchForm="${auditForm}"/>
        <t:sortableCell code="group" label="Group" searchForm="${auditForm}"/>
        <t:sortableCell code="project" label="Project" searchForm="${auditForm}"/>
        <t:sortableCell code="reporter" label="Reporter" searchForm="${auditForm}"/>
        <t:sortableCell code="assignee" label="Assignee" searchForm="${auditForm}"/>
        <t:sortableCell code="issueType" label="Issue Type" searchForm="${auditForm}"/>
        <t:sortableCell code="createdOn" label="Created On" style="text-align:right;" cssClass="has-text-right hideOnSmall" searchForm="${auditForm}" />
        <t:sortableCell code="lastUpdatedOn" label="Updated On" style="text-align:right;" cssClass="has-text-right hideOnSmall" searchForm="${auditForm}" />
    </tr>
    </thead>

    <tbody id="myTBody">
    <c:forEach var="revision" items="${searchResult.searchResults}" varStatus="loop">
        <c:set var="issue" value="${revision.entity}"/>
        <tr>
            <td>${revision.revisionNumber.get()}</td>
            <td>${revision.revisionInstant.get()}</td>
            <td class="has-text-right">
                <a href="${pageContext.request.contextPath}/issue/form?issueId=${issue.id}">${issue.id}</a>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/issue/form?issueId=${issue.id}">${issue.title}</a>
            </td>
            <td>${issue.severity.name}</td>
            <td>${issue.status.name}</td>
            <td>${issue.group.name}</td>
            <td>${issue.project.name}</td>
            <td>${issue.reporter.name}</td>
            <td>${issue.assignee.name}</td>
            <td>${issue.issueType.name}</td>
            <td class="has-text-right">${issue.createdOn}</td>
            <td class="has-text-right">${issue.lastUpdatedOn}</td>
        </tr>
    </c:forEach>
    </tbody>

    <c:if test="${empty searchResult.searchResults}">
        <tr><td colspan="100">No Results</td></tr>
    </c:if>
</table>
<t:paginator searchForm="${auditForm}" searchResult="${searchResult}"/>
