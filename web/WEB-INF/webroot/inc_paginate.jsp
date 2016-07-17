<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="searchResult" type="com.hicks.SearchResult" scope="session"/>
<tr>
    <td colspan="100" style="text-align: center;">
        <button class="firstButton" value="First" onclick="goToPage('first')" <c:if test="${!searchResult.hasPrevious}">disabled</c:if>>
            <span style="font-size: 1.7em;vertical-align: middle;" class="material-icons">first_page</span>
        </button>
        <button class="previousButton" value="Previous" onclick="goToPage('previous')" <c:if test="${!searchResult.hasPrevious}">disabled</c:if>>
            <span style="font-size: 1.7em;vertical-align: middle;" class="material-icons">chevron_left</span>
        </button>

        <fmt:formatNumber value="${searchResult.page}" var="formattedPage" pattern="#,###"/>
        <fmt:formatNumber value="${searchResult.pages}" var="formattedPages" pattern="#,###"/>
        <span class="currentPageSpan" style="vertical-align: middle;">${formattedPage} of ${formattedPages}</span>

        <button class="nextButton" value="Next" onclick="goToPage('next')" <c:if test="${!searchResult.hasNext}">disabled</c:if>>
            <span style="font-size: 1.7em;vertical-align: middle;" class="material-icons">chevron_right</span>
        </button>
        <button class="lastButton" value="Last" onclick="goToPage('last')" <c:if test="${!searchResult.hasNext}">disabled</c:if>>
            <span style="font-size: 1.7em;vertical-align: middle;" class="material-icons">last_page</span>
        </button>
    </td>
</tr>