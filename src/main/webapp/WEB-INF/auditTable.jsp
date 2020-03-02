<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<jsp:useBean id="searchForm" type="net.ehicks.bts.beans.IssueEventForm" scope="request"/>

<table id="issueTable" class="table is-striped is-narrow is-hoverable is-fullwidth">
    <thead>
    <tr class="listheading">
        <t:sortableCell code="eventDate" label="Date" style="text-align:right;" cssClass="has-text-right hideOnSmall" searchForm="${searchForm}" />
        <t:sortableCell code="user" label="Changed By" searchForm="${searchForm}"/>
        <t:sortableCell code="issue" label="Issue" searchForm="${searchForm}" />
        <t:sortableCell code="eventType" label="Action" searchForm="${searchForm}"/>
        <t:sortableCell code="propertyName" label="Property" searchForm="${searchForm}"/>
        <th>Description</th>
    </tr>
    </thead>

    <tbody id="myTBody">
    <c:forEach var="issueEvent" items="${searchForm.searchResult.searchResults}">
        <tr>
            <td class="has-text-right" style="white-space: nowrap;">
                <javatime:format value="${issueEvent.eventDate}" style="SS"/>
            </td>
            <td>${issueEvent.user.username}</td>
            <td style="white-space: nowrap;">${issueEvent.issue.key}</td>
            <td>${issueEvent.eventType}</td>
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

    <c:if test="${empty searchForm.searchResult.searchResults}">
        <tr><td colspan="100">No Results</td></tr>
    </c:if>
</table>
<t:paginator searchForm="${searchForm}" searchResult="${searchForm.searchResult}"/>
