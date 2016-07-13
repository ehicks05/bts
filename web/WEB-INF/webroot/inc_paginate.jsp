<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="searchResult" type="com.hicks.SearchResult" scope="session"/>
<tr>
    <td colspan="100" style="text-align: center;">
        <input class="firstButton" type="button" value="First" onclick="goToPage('first')" <c:if test="${!searchResult.hasPrevious}">disabled</c:if> />
        <input class="previousButton" type="button" value="Previous" onclick="goToPage('previous')" <c:if test="${!searchResult.hasPrevious}">disabled</c:if> />

        <fmt:formatNumber value="${searchResult.page}" var="formattedPage" pattern="#,###"/>
        <fmt:formatNumber value="${searchResult.pages}" var="formattedPages" pattern="#,###"/>
        <span class="currentPageSpan">${formattedPage}</span> of ${formattedPages}

        <input class="nextButton" type="button" value="Next" onclick="goToPage('next')" <c:if test="${!searchResult.hasNext}">disabled</c:if> />
        <input class="lastButton" type="button" value="Last" onclick="goToPage('last')" <c:if test="${!searchResult.hasNext}">disabled</c:if> />
    </td>
</tr>