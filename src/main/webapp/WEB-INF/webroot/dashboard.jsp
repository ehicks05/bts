<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="issueForms" type="java.util.List<com.hicks.beans.IssueForm>" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <title>BTS</title>
    <jsp:include page="inc_header.jsp"/>
</head>
<body onload="initHeader();">

<jsp:include page="header.jsp"/>

<div class="mdl-grid">
    <c:forEach var="issueForm" items="${issueForms}">
        <c:set var="searchResult" value="${issueForm.searchResult}"/>
        <br>
        <div class="mdl-card mdl-cell mdl-cell--6-col mdl-cell--4-col-tablet mdl-shadow--2dp">
            <div class="mdl-card__title"><h5>Found ${searchResult.size} Issues</h5></div>

            <table id="filmTable" style="width:100%;margin: 0 auto" class="list">
                <thead>
                <tr class="listheading">
                    <ct:sortableCell code="id" label="ID" style="text-align:right;" sortColumn="${issueForm.sortColumn}" sortDirection="${issueForm.sortDirection}"/>
                    <ct:sortableCell code="title" label="Title" sortColumn="${issueForm.sortColumn}" sortDirection="${issueForm.sortDirection}"/>
                    <ct:sortableCell code="created_on" label="Created" style="text-align:right;" sortColumn="${issueForm.sortColumn}" sortDirection="${issueForm.sortDirection}"/>
                    <ct:sortableCell code="last_updated_on" label="Updated" style="text-align:right;" sortColumn="${issueForm.sortColumn}" sortDirection="${issueForm.sortDirection}"/>
                </tr>
                </thead>

                <tbody id="myTBody">
                <c:set var="count" value="${1 + ((issueForm.page - 1) * 100)}"/>
                <c:set var="rowStyle" value="listrowodd"/>
                <c:set var="rowToggle" value="${true}"/>
                <c:forEach var="issue" items="${searchResult.searchResults}">

                    <tr class="${rowStyle}">
                        <td class="alignright">
                            <a href="${pageContext.request.contextPath}/view?tab2=issue&action=form&issueId=${issue.id}">
                                    ${issue.id}
                            </a>
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/view?tab2=issue&action=form&issueId=${issue.id}">
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
                    <tr><td colspan="100">-</td></tr>
                </c:if>
                <jsp:include page="inc_paginate.jsp"/>
            </table>
        </div>  
    </c:forEach>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>