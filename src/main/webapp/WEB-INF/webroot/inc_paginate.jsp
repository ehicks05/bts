<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="searchResult" type="com.hicks.SearchResult" scope="request"/>
<jsp:useBean id="issueForm" type="com.hicks.beans.IssueForm" scope="request"/>
<tr>
    <td colspan="100" style="text-align: center;">
        <c:if test="${searchResult.hasPrevious}">
            <c:set var="visibility" value="normal"/>
        </c:if>
        <c:if test="${searchResult.hasPrevious == false}">
            <c:set var="visibility" value="hidden"/>
        </c:if>
        <span id="first${issueForm.id}" onclick="ajaxItems(this.id, '${pageContext.request.contextPath}', '${issueForm.id}', '1', '', '')" style="vertical-align: middle;visibility: ${visibility}" class="clickable material-icons">first_page</span>
        <span id="previous${issueForm.id}" onclick="ajaxItems(this.id, '${pageContext.request.contextPath}', '${issueForm.id}', '${issueForm.page - 1}', '', '')" style="vertical-align: middle;visibility: ${visibility}" class="clickable material-icons">chevron_left</span>

        <c:forEach var="navPage" items="${searchResult.navPages}">
            <c:if test="${navPage == searchResult.page}">
                <c:set var="fontWeight" value="bold"/>
            </c:if>
            <c:if test="${navPage != searchResult.page}">
                <c:set var="fontWeight" value="normal"/>
            </c:if>

            &nbsp;
            <span id="page${navPage}${issueForm.id}" class="clickable" onclick="ajaxItems(this.id, '${pageContext.request.contextPath}', '${issueForm.id}', '${navPage}', '', '')" style="font-family: 'Droid Sans Mono', monospace;vertical-align: middle; font-weight:${fontWeight};">${navPage}</span>
        </c:forEach>

        <c:if test="${searchResult.hasNext}">
            <span id="next${issueForm.id}" onclick="ajaxItems(this.id, '${pageContext.request.contextPath}', '${issueForm.id}', '${issueForm.page + 1}', '', '')" style="vertical-align: middle;" class="clickable material-icons">chevron_right</span>
            <span id="last${issueForm.id}" onclick="ajaxItems(this.id, '${pageContext.request.contextPath}', '${issueForm.id}', '${searchResult.pages}', '', '')" style="vertical-align: middle;" class="clickable material-icons">last_page</span>
        </c:if>
    </td>
</tr>