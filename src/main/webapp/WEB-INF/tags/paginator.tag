<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@tag description="Paginator Tag" pageEncoding="UTF-8" %>
<%@attribute name="searchForm" type="net.ehicks.bts.SearchForm" fragment="false" %>
<%@attribute name="searchResult" type="net.ehicks.bts.SearchResult" fragment="false" %>

<c:set var="paginatorCounter" value="${requestScope.paginatorCounter + 1}" scope="request"/>
<c:if test="${paginatorCounter == 1}">
    <script>

    </script>
</c:if>

<%--<tr>--%>
    <%--<td colspan="100" style="text-align: center;">--%>
        <%--<c:if test="${searchResult.hasPrevious}">--%>
            <%--<span id="first${searchForm.id}" onclick="ajaxItems(this.id, '${pageContext.request.contextPath}', '${searchForm.endpoint}', '${searchForm.id}', '1', '', '')" style="vertical-align: middle;" class="clickable material-icons">first_page</span>--%>
            <%--<span id="previous${searchForm.id}" onclick="ajaxItems(this.id, '${pageContext.request.contextPath}', '${searchForm.endpoint}', '${searchForm.id}', '${searchForm.page - 1}', '', '')" style="vertical-align: middle;" class="clickable material-icons">chevron_left</span>--%>
        <%--</c:if>--%>

        <%--<c:forEach var="navPage" items="${searchResult.navPages}">--%>
            <%--<c:set var="fontWeight" value="${navPage == searchResult.page ? 'bold' : 'normal'}"/>--%>

            <%--&nbsp;--%>
            <%--<span id="page${navPage}${searchForm.id}" class="clickable" style="font-family: 'Droid Sans Mono', monospace;vertical-align: middle; font-weight:${fontWeight};"--%>
                  <%--onclick="ajaxItems(this.id, '${pageContext.request.contextPath}', '${searchForm.endpoint}', '${searchForm.id}', '${navPage}', '', '')">${navPage}</span>--%>
        <%--</c:forEach>--%>

        <%--<c:if test="${searchResult.hasNext}">--%>
            <%--<span id="next${searchForm.id}" onclick="ajaxItems(this.id, '${pageContext.request.contextPath}', '${searchForm.endpoint}', '${searchForm.id}', '${searchForm.page + 1}', '', '')" style="vertical-align: middle;" class="clickable material-icons">chevron_right</span>--%>
            <%--<span id="last${searchForm.id}" onclick="ajaxItems(this.id, '${pageContext.request.contextPath}', '${searchForm.endpoint}', '${searchForm.id}', '${searchResult.pages}', '', '')" style="vertical-align: middle;" class="clickable material-icons">last_page</span>--%>
        <%--</c:if>--%>
    <%--</td>--%>
<%--</tr>--%>

<nav class="pagination is-centered" role="navigation" aria-label="pagination">
    <c:set var="disabled" value="${!searchResult.hasPrevious ? 'disabled' : ''}" />
    <a class="pagination-previous" id="previous${searchForm.id}" ${disabled}
       onclick="ajaxItems(this.id, '${pageContext.request.contextPath}', '${searchForm.endpoint}', '${searchForm.id}', '${searchForm.page - 1}', '', '')"><i class="fas fa-chevron-left"></i></a>

    <c:set var="disabled" value="${!searchResult.hasNext ? 'disabled' : ''}" />
    <a class="pagination-next" id="next${searchForm.id}" ${disabled}
       onclick="ajaxItems(this.id, '${pageContext.request.contextPath}', '${searchForm.endpoint}', '${searchForm.id}', '${searchForm.page + 1}', '', '')"><i class="fas fa-chevron-right"></i></a>
    <ul class="pagination-list">
        <c:forEach var="navPage" items="${searchResult.navPages}">
            <c:set var="isCurrent" value="${navPage == searchResult.page ? 'is-current' : ''}"/>
            <li>
                <a id="page${navPage}${searchForm.id}" class="pagination-link ${isCurrent}" aria-label="Goto page ${navPage}"
                   onclick="ajaxItems(this.id, '${pageContext.request.contextPath}', '${searchForm.endpoint}', '${searchForm.id}', '${navPage}', '', '')"
                >${navPage}</a>
            </li>
        </c:forEach>
    </ul>
</nav>