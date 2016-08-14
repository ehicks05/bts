<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="searchResult" type="com.hicks.SearchResult" scope="request"/>
<jsp:useBean id="issueForm" type="com.hicks.beans.IssueForm" scope="request"/>
<tr>
    <td colspan="100" style="text-align: center;">
        <c:if test="${searchResult.hasPrevious}">
            <span onclick="goToPage('first')" style="vertical-align: middle;" class="material-icons">first_page</span>
            <span onclick="goToPage('previous')" style="vertical-align: middle;" class="material-icons">chevron_left</span>
        </c:if>

        <fmt:formatNumber value="${issueForm.page}" var="formattedPage" pattern="#,###"/>
        <fmt:formatNumber value="${searchResult.pages}" var="formattedPages" pattern="#,###"/>
        <span class="currentPageSpan" style="vertical-align: middle;">${formattedPage} of ${formattedPages}</span>

        <c:if test="${searchResult.hasNext}">
            <span onclick="goToPage('next')" style="vertical-align: middle;" class="material-icons">chevron_right</span>
            <span onclick="goToPage('last')" style="vertical-align: middle;" class="material-icons">last_page</span>
        </c:if>
    </td>
</tr>