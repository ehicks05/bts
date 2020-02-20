<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<jsp:useBean id="issueForm" type="net.ehicks.bts.beans.IssueForm" scope="request"/>
<%--<jsp:useBean id="searchResult" type="net.ehicks.bts.model.SearchResult" scope="request"/>--%>

<table id="issueTable" class="table is-striped is-narrow is-hoverable is-fullwidth">
    <thead>
        <tr class="listheading">
            <t:sortableCell code="id" label="ID" style="text-align:right;" cssClass="has-text-right" searchForm="${issueForm}" />
            <t:sortableCell code="title" label="Title" searchForm="${issueForm}"/>
            <t:sortableCell code="severity" label="Severity" searchForm="${issueForm}"/>
            <t:sortableCell code="createdOn" label="Age" style="text-align:right;" cssClass="has-text-right hideOnSmall" searchForm="${issueForm}" />
            <t:sortableCell code="lastUpdatedOn" label="Updated" style="text-align:right;" cssClass="has-text-right hideOnSmall" searchForm="${issueForm}" />
        </tr>
    </thead>

    <tbody id="myTBody">
    <c:forEach var="issue" items="${issueForm.searchResult.searchResults}" varStatus="loop">
        <tr>
            <td class="has-text-right">
                <a href="${pageContext.request.contextPath}/issue/form?issueId=${issue.id}">${issue.id}</a>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/issue/form?issueId=${issue.id}">${issue.title}</a>
            </td>
            <td>${issue.severity.name}</td>
            <td class="has-text-right hideOnSmall" title="<javatime:format value="${issue.createdOn}" style="MS" />">${issue.timeSinceCreation}</td>
            <td class="has-text-right hideOnSmall" title="<javatime:format value="${issue.lastUpdatedOn}" style="MS" />">${issue.timeSinceUpdate}</td>
        </tr>
    </c:forEach>
    </tbody>

    <c:if test="${empty issueForm.searchResult.searchResults}">
        <tr><td colspan="100">No Results</td></tr>
    </c:if>
</table>
<t:paginator searchForm="${issueForm}" searchResult="${issueForm.searchResult}"/>
