<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@tag description="Sortable Cell Tag" pageEncoding="UTF-8" %>
<%@attribute name="code" fragment="false" %>
<%@attribute name="label" fragment="false" %>
<%@attribute name="style" fragment="false" %>
<%@attribute name="cssClass" fragment="false" %>
<%@attribute name="issueForm" fragment="false" type="com.hicks.beans.IssueForm" %>

<c:set var="sortableCellCounter" value="${requestScope.sortableCellCounter + 1}" scope="request"/>
<c:if test="${sortableCellCounter == 1}">

</c:if>

<c:if test="${code == issueForm.sortColumn}">
    <c:if test="${issueForm.sortDirection == 'asc'}"><c:set var="sortIcon" value="&#9650;"/></c:if>
    <c:if test="${issueForm.sortDirection == 'desc'}"><c:set var="sortIcon" value="&#9660;"/></c:if>
</c:if>

<td id="${code}${issueForm.id}" class="sortableHeader ${cssClass}" style="${style}"
    onclick="ajaxItems(this.id, '${pageContext.servletConfig.servletContext.contextPath}', '${issueForm.id}', 0, '${code}', '${issueForm.sortDirection}');">
    ${label}
    <span>${sortIcon}</span>
</td>