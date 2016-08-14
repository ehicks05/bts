<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="searchResult" type="com.hicks.SearchResult" scope="request"/>
<jsp:useBean id="issueForm" type="com.hicks.beans.IssueForm" scope="request"/>
<tr>
    <td colspan="100" style="text-align: center;">
        <c:if test="${searchResult.hasPrevious}">
            <span onclick="ajaxFilms(this, '${issueForm.id}', '1', '', '')" style="vertical-align: middle;" class="clickable material-icons">first_page</span>
            <span onclick="ajaxFilms(this, '${issueForm.id}', '${issueForm.page - 1}', '', '')" style="vertical-align: middle;" class="clickable material-icons">chevron_left</span>
        </c:if>

        <c:forEach var="navPage" items="${searchResult.navPages}">
            <c:if test="${navPage == searchResult.page}">
                <c:set var="fontWeight" value="bold"/>
            </c:if>
            <c:if test="${navPage != searchResult.page}">
                <c:set var="fontWeight" value="normal"/>
            </c:if>

            <span class="clickable" onclick="ajaxFilms(this, '${issueForm.id}', '${navPage}', '', '')" style="vertical-align: middle; font-weight:${fontWeight};">
                ${navPage}
            </span>
        </c:forEach>

    <%--<fmt:formatNumber value="${issueForm.page}" var="formattedPage" pattern="#,###"/>--%>
        <%--<fmt:formatNumber value="${searchResult.pages}" var="formattedPages" pattern="#,###"/>--%>
        <%--<span class="currentPageSpan" style="vertical-align: middle;">${formattedPage} of ${formattedPages}</span>--%>

        <c:if test="${searchResult.hasNext}">
            <span onclick="ajaxFilms(this, '${issueForm.id}', '${issueForm.page + 1}', '', '')" style="vertical-align: middle;" class="clickable material-icons">chevron_right</span>
            <span onclick="ajaxFilms(this, '${issueForm.id}', '${searchResult.pages}', '', '')" style="vertical-align: middle;" class="clickable material-icons">last_page</span>
        </c:if>
    </td>
</tr>