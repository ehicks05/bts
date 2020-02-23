<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@tag description="Sortable Cell Tag" pageEncoding="UTF-8" %>
<%@attribute name="code" fragment="false" %>
<%@attribute name="label" fragment="false" %>
<%@attribute name="style" fragment="false" %>
<%@attribute name="cssClass" fragment="false" %>
<%@attribute name="searchForm" fragment="false" type="net.ehicks.bts.model.SearchForm" %>

<c:set var="sortableCellCounter" value="${requestScope.sortableCellCounter + 1}" scope="request"/>
<c:if test="${sortableCellCounter == 1}">

</c:if>

<c:set var="newDirection" value="asc"/>
<c:if test="${code == searchForm.sortColumn}">
    <c:if test="${searchForm.sortDirection == 'asc'}"><c:set var="sortIcon" value="arrow-up"/></c:if>
    <c:if test="${searchForm.sortDirection == 'desc'}"><c:set var="sortIcon" value="arrow-down"/></c:if>

    <c:if test="${searchForm.sortDirection == 'asc'}"><c:set var="newDirection" value="desc"/></c:if>
    <c:if test="${searchForm.sortDirection == 'desc'}"><c:set var="newDirection" value="asc"/></c:if>
</c:if>

<th id="${code}${searchForm.id}" class="nowrap ${cssClass}" style="cursor: pointer; ${style}"
    onclick="ajaxItems(this.id, '${pageContext.servletConfig.servletContext.contextPath}', '${searchForm.endpoint}', '${searchForm.id}', '${searchForm.page}', '${code}', '${newDirection}');">
    ${label}
    <span class="icon">
      <i class="fas fa-${sortIcon}"></i>
    </span>
</th>