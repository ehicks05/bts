<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description="Paginator Tag" pageEncoding="UTF-8" %>
<%@attribute name="searchForm" type="net.ehicks.bts.model.SearchForm" fragment="false" %>
<%@attribute name="searchResult" type="net.ehicks.bts.model.SearchResult" fragment="false" %>

<nav class="pagination is-centered" role="navigation" aria-label="pagination">
    <c:set var="disabled" value="${!searchResult.hasPrevious ? 'disabled' : ''}" />
    <a class="pagination-previous" id="previous${searchForm.id}" ${disabled}
       onclick="ajaxItems(this.id, '${pageContext.request.contextPath}', '${searchForm.endpoint}', '${searchForm.id}',
               '${searchForm.page - 1}', '${searchForm.sortColumn}', '${searchForm.sortDirection}')"><i class="fas fa-chevron-left"></i></a>

    <c:set var="disabled" value="${!searchResult.hasNext ? 'disabled' : ''}" />
    <a class="pagination-next" id="next${searchForm.id}" ${disabled}
       onclick="ajaxItems(this.id, '${pageContext.request.contextPath}', '${searchForm.endpoint}', '${searchForm.id}',
               '${searchForm.page + 1}', '${searchForm.sortColumn}', '${searchForm.sortDirection}')"><i class="fas fa-chevron-right"></i></a>
    <ul class="pagination-list">
        <c:forEach var="navPage" items="${searchResult.navPages}">
            <c:set var="isCurrent" value="${navPage == searchResult.page ? 'is-current' : ''}"/>
            <li>
                <a id="page${navPage}${searchForm.id}" class="pagination-link ${isCurrent}" aria-label="Goto page ${navPage}"
                   onclick="ajaxItems(this.id, '${pageContext.request.contextPath}', '${searchForm.endpoint}',
                           '${searchForm.id}', '${navPage}', '${searchForm.sortColumn}', '${searchForm.sortDirection}')"
                >${navPage}</a>
            </li>
        </c:forEach>
    </ul>
</nav>