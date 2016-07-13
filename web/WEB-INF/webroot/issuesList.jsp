<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="issuesForm" type="com.hicks.IssuesForm" scope="session"/>
<jsp:useBean id="searchResult" type="com.hicks.SearchResult" scope="session"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>BTS</title>
    <jsp:include page="inc_header.jsp"/>
    <script>
        var page = ${searchResult.page};
        var pages = ${searchResult.pages};
        var sortColumn = '${searchResult.sortColumn}';
        var sortDirection = '${searchResult.sortDirection}';

        function initHeader()
        {

        }

        function sortFilms(element, column)
        {
            var previousColumn = sortColumn;
            var previousDirection = sortDirection;
            var direction = 'desc';
            if (column === previousColumn)
            {
                if (previousDirection === 'asc') direction = 'desc';
                if (previousDirection === 'desc') direction = 'asc';
            }
            $('#sortColumn').val(column);
            $('#sortDirection').val(direction);

            $('#frmFilter').submit();
        }

        function goToPage(pageNumber)
        {
            var parsedPage = '';
            if (pageNumber == 'first') parsedPage = 1;
            if (pageNumber == 'last') parsedPage = pages;
            if (pageNumber == 'next') parsedPage = (page + 1);
            if (pageNumber == 'previous') parsedPage = (page - 1);

            $('#page').val(parsedPage);

            $('#frmFilter').submit();
        }

        function resetPagination()
        {
            $('#resetPage').val('yes');
        }

    </script>

</head>
<body onload="initHeader();">

<jsp:include page="header.jsp"/>
<br>

<form name="frmFilter" id="frmFilter" method="post" action="${pageContext.request.contextPath}/view?tab1=home&action=search">
    <input type="hidden" id="fldRating" name="fldRating">
    <input type="hidden" name="sortColumn" id="sortColumn" value="${searchResult.sortColumn}"/>
    <input type="hidden" name="sortDirection" id="sortDirection" value="${searchResult.sortDirection}"/>
    <input type="hidden" name="page" id="page" value="${searchResult.page}"/>
    <input type="hidden" name="resetPage" id="resetPage"/>

    <table style="margin: 0 auto" class="list">
        <tr>
            <td colspan="4">
                <h2 style="text-align: center;margin: 0;">Search for Issues</h2>
            </td>
        </tr>
        <tr>
            <td class="alignright"><label for="id">ID:</label></td>
            <td colspan="3"><input id="id" name="id" type="text" size="20" maxlength="32" value="${issuesForm.id}"></td>
        </tr>
        <tr>
            <td class="alignright"><label for="title">Title:</label></td>
            <td colspan="3"><input id="title" name="title" type="text" size="20" maxlength="32" value="${issuesForm.title}"></td>
        </tr>
        <tr>
            <td class="alignright"><label for="description">Description:</label></td>
            <td colspan="3"><input id="description" name="description" type="text" size="20" maxlength="32" value="${issuesForm.description}"></td>
        </tr>

        <tr><td colspan="4" style="text-align: center"><input type="submit" value="Search" class="btn btn-primary" onclick="resetPagination();"/></td></tr>
        <tr><td colspan="4" style="text-align: center"><span>${searchResult.searchResultsSize} Results</span></td></tr>
    </table>
</form>
<br>
<table id="filmTable" style="margin: 0 auto" class="list">
    <thead>
    <jsp:include page="inc_paginate.jsp"/>
    <tr class="listheading">
        <td></td>
        <ct:sortableCell code="id" label="ID" sortColumn="${searchResult.sortColumn}" sortDirection="${searchResult.sortDirection}"/>
        <ct:sortableCell code="title" label="Title" sortColumn="${searchResult.sortColumn}" sortDirection="${searchResult.sortDirection}"/>
        <ct:sortableCell code="description" label="Description" sortColumn="${searchResult.sortColumn}" sortDirection="${searchResult.sortDirection}"/>
        <ct:sortableCell code="createdOn" label="Created" sortColumn="${searchResult.sortColumn}" sortDirection="${searchResult.sortDirection}"/>
        <ct:sortableCell code="lastUpdatedOn" label="Updated" sortColumn="${searchResult.sortColumn}" sortDirection="${searchResult.sortDirection}"/>
    </tr>
    </thead>

    <tbody id="myTBody">
    <c:set var="count" value="${1 + ((searchResult.page - 1) * 100)}"/>
    <c:set var="rowStyle" value="listrowodd"/>
    <c:set var="rowToggle" value="${true}"/>
    <c:forEach var="issue" items="${searchResult.pageOfResults}">

        <tr class="${rowStyle}">
            <td class="alignright"><fmt:formatNumber value="${count}" pattern="#,###"/></td>
            <td>
                ${issue.id}
            </td>
            <td>
                ${issue.title}
            </td>
            <td class="alignright">${issue.description}</td>
            <td class="alignright"><fmt:formatDate value="${issue.createdOn}" pattern="dd/MMM/yyyy" /></td>
            <td class="alignright"><fmt:formatDate value="${issue.lastUpdatedOn}" pattern="dd/MMM/yyyy" /></td>
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
</body>
</html>