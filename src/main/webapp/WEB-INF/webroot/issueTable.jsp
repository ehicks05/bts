<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="issueForm" type="com.hicks.beans.IssueForm" scope="request"/>
<jsp:useBean id="searchResult" type="com.hicks.SearchResult" scope="request"/>


<table id="filmTable" style="width:100%;margin: 0 auto" class="list">
    <thead>
    <tr class="listheading">
        <t:sortableCell code="id" label="ID" style="text-align:right;" issueForm="${issueForm}" />
        <t:sortableCell code="title" label="Title" issueForm="${issueForm}"/>
        <t:sortableCell code="created_on" label="Created" style="text-align:right;" issueForm="${issueForm}" />
        <t:sortableCell code="last_updated_on" label="Updated" style="text-align:right;" issueForm="${issueForm}" />
    </tr>
    </thead>

    <tbody id="myTBody">
    <c:set var="count" value="${1 + ((issueForm.page - 1) * 100)}"/>
    <c:set var="rowStyle" value="listrowodd"/>
    <c:set var="rowToggle" value="${true}"/>
    <c:forEach var="issue" items="${searchResult.searchResults}">

        <tr class="${rowStyle}">
            <td class="alignright">
                <a href="${pageContext.request.contextPath}/view?tab1=main&tab2=issue&action=form&issueId=${issue.id}">
                        ${issue.id}
                </a>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/view?tab1=main&tab2=issue&action=form&issueId=${issue.id}">
                        ${issue.title}
                </a>
            </td>
            <td class="alignright"><fmt:formatDate value="${issue.createdOn}" pattern="dd/MMM/yy" /></td>
            <td class="alignright"><fmt:formatDate value="${issue.lastUpdatedOn}" pattern="dd/MMM/yy" /></td>
        </tr>

        <c:if test="${rowToggle}"><c:set var="rowStyle" value="listroweven"/></c:if>
        <c:if test="${!rowToggle}"><c:set var="rowStyle" value="listrowodd"/></c:if>
        <c:set var="rowToggle" value="${!rowToggle}"/>
        <c:set var="count" value="${count + 1}"/>
    </c:forEach>
    </tbody>

    <c:if test="${empty searchResult.searchResults}">
        <tr><td colspan="100">No Results</td></tr>
    </c:if>
    <jsp:include page="inc_paginate.jsp"/>
</table>