<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<jsp:useBean id="searchForm" type="net.ehicks.bts.beans.IssueEventForm" scope="request"/>

<table id="issueTable" class="table is-striped is-narrow is-hoverable is-fullwidth">
    <thead>
    <tr class="listheading">
        <t:sortableCell code="eventDate" label="Date" style="text-align:right;" cssClass="has-text-right hideOnSmall" searchForm="${searchForm}" />
        <t:sortableCell code="user" label="Changed By" searchForm="${searchForm}" style="" cssClass=""/>
        <t:sortableCell code="issue" label="Issue" searchForm="${searchForm}" style="" cssClass=""/>
        <t:sortableCell code="eventType" label="" searchForm="${searchForm}" style="" cssClass=""/>
        <t:sortableCell code="propertyName" label="Property" searchForm="${searchForm}" style="" cssClass=""/>
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
            <td>
                <c:if test="${issueEvent.eventType.toString() == 'ADD'}"><i title="ADD" class="fas fa-plus has-text-success"></i></c:if>
                <c:if test="${issueEvent.eventType.toString() == 'UPDATE'}"><i title="UPDATE" class="fas fa-edit has-text-info"></i></c:if>
                <c:if test="${issueEvent.eventType.toString() == 'REMOVE'}"><i title="REMOVE" class="fas fa-minus has-text-danger"></i></c:if>
            </td>
            <td>${issueEvent.propertyName}</td>
            <td>
                <c:forEach var="diff" items="${issueEvent.renderMap.diffs}">
                    <c:if test="${diff.operation == 'INSERT'}"><span class="diff-add">${diff.text}</span></c:if>
                    <c:if test="${diff.operation == 'DELETE'}"><span class="diff-remove">${diff.text}</span></c:if>
                    <c:if test="${diff.operation == 'EQUAL'}"><span>${diff.text}</span></c:if>
                </c:forEach>
            </td>
        </tr>
    </c:forEach>
    </tbody>

    <c:if test="${searchForm.searchResult.searchResults.size() == 0}">
        <tr><td colspan="100">No Results</td></tr>
    </c:if>
</table>
<t:paginator searchForm="${searchForm}" searchResult="${searchForm.searchResult}"/>
