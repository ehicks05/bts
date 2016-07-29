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

<div class="mdl-grid">
    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-shadow--2dp">
        <form name="frmFilter" id="frmFilter" method="post" action="${pageContext.request.contextPath}/view?tab1=main&tab2=issue&action=search">
            <input type="hidden" id="fldRating" name="fldRating">
            <input type="hidden" name="sortColumn" id="sortColumn" value="${searchResult.sortColumn}"/>
            <input type="hidden" name="sortDirection" id="sortDirection" value="${searchResult.sortDirection}"/>
            <input type="hidden" name="page" id="page" value="${searchResult.page}"/>
            <input type="hidden" name="resetPage" id="resetPage"/>

            <div class="mdl-card__title"><h5>Search for Issues</h5></div>

            <div class="mdl-card__supporting-text">

                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" type="text" size="20" maxlength="32" id="id" name="id" value="${issuesForm.id}">
                    <label class="mdl-textfield__label" for="id">Contains Text:</label>
                </div>

                <br>
                <div>
                    <label for="severity">Severity: </label>
                    <ct:select id="severity" value="${issuesForm.severity}" items="${severities}" required="${false}"/>
                </div>
            </div>
            <div class="mdl-card__actions">
                <input type="submit" value="Search" class="mdl-button mdl-js-button mdl-button--raised" onclick="resetPagination();" />
            </div>
        </form>
    </div>

    <br>
    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>Found ${searchResult.size} Issues</h5></div>

        <table id="filmTable" style="width:100%;margin: 0 auto" class="list">
            <thead>
            <tr class="listheading">
                <%--<td></td>--%>
                <ct:sortableCell code="id" label="ID" style="text-align:right;" sortColumn="${searchResult.sortColumn}" sortDirection="${searchResult.sortDirection}"/>
                <ct:sortableCell code="title" label="Title" sortColumn="${searchResult.sortColumn}" sortDirection="${searchResult.sortDirection}"/>
                <ct:sortableCell code="createdOn" label="Created" style="text-align:right;" sortColumn="${searchResult.sortColumn}" sortDirection="${searchResult.sortDirection}"/>
                <ct:sortableCell code="lastUpdatedOn" label="Updated" style="text-align:right;" sortColumn="${searchResult.sortColumn}" sortDirection="${searchResult.sortDirection}"/>
            </tr>
            </thead>

            <tbody id="myTBody">
            <c:set var="count" value="${1 + ((searchResult.page - 1) * 100)}"/>
            <c:set var="rowStyle" value="listrowodd"/>
            <c:set var="rowToggle" value="${true}"/>
            <c:forEach var="issue" items="${searchResult.searchResults}">

                <tr class="${rowStyle}">
                    <%--<td class="alignright"><fmt:formatNumber value="${count}" pattern="#,###"/></td>--%>
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
                        <%--<td class="alignright">${fn:substring(issue.description, 0, 160)}</td>--%>
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
</div>


<jsp:include page="footer.jsp"/>
</body>
</html>