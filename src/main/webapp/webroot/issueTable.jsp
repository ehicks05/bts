<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="issueForm" type="net.ehicks.bts.beans.IssueForm" scope="request"/>
<jsp:useBean id="searchResult" type="net.ehicks.bts.SearchResult" scope="request"/>

<table id="filmTable" class="table is-striped is-narrow is-hoverable is-fullwidth">
    <thead>
        <tr class="listheading">
            <t:sortableCell code="id" label="ID" style="text-align:right;" searchForm="${issueForm}" />
            <t:sortableCell code="title" label="Title" searchForm="${issueForm}"/>
            <t:sortableCell code="created_on" label="Created" style="text-align:right;" cssClass="hideOnSmall" searchForm="${issueForm}" />
            <t:sortableCell code="last_updated_on" label="Updated" style="text-align:right;" cssClass="hideOnSmall" searchForm="${issueForm}" />
        </tr>
    </thead>

    <tbody id="myTBody">
    <c:forEach var="issue" items="${searchResult.searchResults}" varStatus="loop">
        <tr class="${loop.index % 2 == 0 ? 'listrowodd' : 'listroweven'}">
            <td class="alignright">
                <a href="${pageContext.request.contextPath}/view?tab1=issue&action=form&issueId=${issue.id}">${issue.id}</a>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/view?tab1=issue&action=form&issueId=${issue.id}">${issue.title}</a>
            </td>
            <td class="alignright hideOnSmall"><fmt:formatDate value="${issue.createdOn}" pattern="dd/MMM/yy" /></td>
            <td class="alignright hideOnSmall"><fmt:formatDate value="${issue.lastUpdatedOn}" pattern="dd/MMM/yy" /></td>
        </tr>
    </c:forEach>
    </tbody>

    <c:if test="${empty searchResult.searchResults}">
        <tr><td colspan="100">No Results</td></tr>
    </c:if>
</table>
<t:paginator searchForm="${issueForm}" searchResult="${searchResult}"/>
