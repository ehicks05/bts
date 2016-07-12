<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="issuesForm" type="com.hicks.IssuesForm" scope="session"/>
<jsp:useBean id="searchResult" type="com.hicks.SearchResult" scope="session"/>

<c:if test="${!empty requestScope.uniqueLanguages}">
    <jsp:useBean id="uniqueLanguages" type="java.util.List<java.lang.String>" scope="request"/>
    <jsp:useBean id="uniqueGenres" type="java.util.List<java.lang.String>" scope="request"/>
</c:if>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>BTS</title>

    <jsp:include page="inc_header.jsp"/>

    <style>#ratingSlider { margin: 10px; }	</style>
    <script>
        var page = ${searchResult.page};
        var pages = ${searchResult.pages};
        var sortColumn = '${searchResult.sortColumn}';
        var sortDirection = '${searchResult.sortDirection}';

        function initHeader()
        {
            $("#fromReleaseDateDatepicker").datepicker({
                showOn: 'button',
                buttonImage: '../images/calendar.gif',
                buttonImageOnly: true,
                changeYear: true,
                yearRange: '1888:2016'
            });

            $("#toReleaseDateDatepicker").datepicker({
                showOn: 'button',
                buttonImage: '../images/calendar.gif',
                buttonImageOnly: true,
                changeYear: true,
                yearRange: '1888:2016'
            });
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
            sortColumn = column;
            sortDirection = direction;

            // remove sort indicator from all .sortableHeader
            $('.sortableHeader span').each(function(index) {
                if (this.textContent.indexOf('▼') > -1 || this.textContent.indexOf('▲') > -1)
                {
                    $(this).closest('span')[0].textContent = $(this).closest('span')[0].textContent.replace('▼', '');
                    $(this).closest('span')[0].textContent = $(this).closest('span')[0].textContent.replace('▲', '');
                }
            });
            // add sort indicator to the one that was clicked
            var sortIcon = direction == 'asc' ? '▲' : '▼';
            $(element).find('span')[0].innerHTML += ' ' + sortIcon;

            ajaxFilms('', column, direction);
        }

        function goToPage(pageNumber)
        {
            var parsedPage = '';
            if (pageNumber == 'first') parsedPage = 1;
            if (pageNumber == 'last') parsedPage = pages;
            if (pageNumber == 'next') parsedPage = (page + 1);
            if (pageNumber == 'previous') parsedPage = (page - 1);

            page = parsedPage;
            $('#page').val(parsedPage);

            $('.currentPageSpan').html(parsedPage);

            if (parsedPage == 1)
            {
                $('.firstButton').prop( "disabled", true );
                $('.previousButton').prop( "disabled", true );
            }
            else
            {
                $('.firstButton').prop( "disabled", false );
                $('.previousButton').prop( "disabled", false );
            }
            if (parsedPage == ${searchResult.pages})
            {
                $('.nextButton').prop( "disabled", true );
                $('.lastButton').prop( "disabled", true );
            }
            else
            {
                $('.nextButton').prop( "disabled", false );
                $('.lastButton').prop( "disabled", false );
            }

            ajaxFilms(parsedPage, '', '');
        }

        function resetPagination()
        {
            $('#resetPage').val('yes');
        }

        // todo: find a way to avoid having to keep this in sync
        function ajaxFilms(newPage, newSortColumn, newSortDirection)
        {
            var myUrl = '${pageContext.request.contextPath}/view?tab1=home&action=ajaxGetNewPage';
            var params = {};
            if (newPage) params.page = newPage;
            if (newSortColumn) params.sortColumn = newSortColumn;
            if (newSortDirection) params.sortDirection = newSortDirection;

            $.getJSON(myUrl, params,
                    function(data, textStatus, xhr)
                    {
                        if(textStatus == "success")
                        {
                            var rows = [];
                            $.each(data, function(key, value){
                                var indexOnCurrentPage = key + 1;
                                var resultIndex = (page - 1) * 100 + indexOnCurrentPage;
                                var rowClass = indexOnCurrentPage % 2 == 0 ? 'listroweven' : 'listrowodd';
                                var row = "<tr class=" + rowClass + ">";
                                row += "  <td class='alignright'>" + resultIndex + "</td>";

                                var onclickValue = "\"toggleRow('" + value.imdbId + "'" + ")\"";

                                var styleValue = "'color: blue; text-decoration: underline; cursor: pointer'";

                                var freshImage = "";
                                if (value.tomatoImage == 'fresh')
                                    freshImage = "<img src='../../images/certified_logo.png' style='vertical-align: middle' height='16px'/>";

                                row += "  <td><span onclick=" + onclickValue + " style=" + styleValue + ">" + value.title + freshImage + "</span></td>";

                                row += "  <td class='alignright'>" + value.cinemangRating + "</td>";
                                row += "  <td class='mediumPriority alignright'>" + value.tomatoMeter + "</td>";
                                row += "  <td class='mediumPriority alignright'>" + value.tomatoUserMeter + "</td>";

                                onclickValue = "'window.open(&quot;" + "http://www.imdb.com/title/" + value.imdbId + "&quot;, &quot;_blank&quot);'";
                                row += "  <td class='mediumPriority alignright' onclick=" + onclickValue + " style=" + styleValue + ">" + value.imdbRating + "</td>";

                                row += "  <td class='alignright'>" + value.released + "</td>";
                                row += "  <td class='lowPriority alignright'>" + value.imdbVotes + "</td>";
                                row += "  <td class='lowPriority'>" + value.language + "</td>";
                                row += "  <td class='lowPriority'>" + value.genre + "</td>";

                                row += "</tr>";
                                rows.push(row);

                                row  = "<tr id='" + value.imdbId + "_secondRow' style='display: none'>";
                                row += "\n  <td colspan='100' class='aligncenter' style='height: 200px; padding: 1px 3px;'>";
                                row += "\n    <div id='" + value.imdbId + "_animatedDiv' style='display: none; max-width: 700px; margin: 0 auto;'>";
                                row += "\n      <div style='float: left; padding-right: 10pt'>";
                                row += "\n        <img id='" + value.imdbId + "_posterUrl' src='' style='margin: 0 auto;'/>";
                                row += "\n      </div>";
                                row += "\n      <div style='width: 100%; text-align: left;'>";
                                row += "\n        <div><b>Running Time: </b></div>";
                                row += "\n        <div>" + value.runtime + "</div>";
                                row += "\n        <div><b>Director: </b></div>";
                                row += "\n        <div>" + value.director + "</div>";
                                row += "\n        <div><b>Actors: </b></div>";
                                row += "\n        <div>" + value.actors + "</div>";
                                row += "\n        <div><b>Tomato Critic Consensus: </b></div>";
                                row += "\n        <div>" + value.tomatoConsensus + "</div>";
                                row += "\n        <div><b>Plot: </b></div>";
                                row += "\n        <div>" + value.prettyPlot + "</div>";
                                row += "\n      </div>";
                                row += "\n    </div>";
                                row += "\n  </td>";
                                row += "\n</tr>";

                                rows.push(row);
                            });

                            var oldTBody = document.getElementById('myTBody');
                            oldTBody.innerHTML = rows.join("");
                        }
                        if (textStatus == "error")
                            alert("Error: " + xhr.status + ": " + xhr.statusText);
                    });
        }

    </script>

</head>
<body onload="initHeader();">

<jsp:include page="header.jsp"/>
<br>

<form name="frmFilter" id="frmFilter" method="post" action="${pageContext.request.contextPath}/view?tab1=home&action=search">
    <input type="hidden" id="fldRating" name="fldRating">
    <input type="hidden" name="sortColumn" id="sortColumn" value="${searchResult.sortColumn}"/>
    <input type="hidden" name="sortDirection" id="sortDirection" value="${searchResult.sortDirection}"/>
    <input type="hidden" name="page" id="page" value="${searchResult.page}"/>
    <input type="hidden" name="resetPage" id="resetPage"/>

    <table style="margin: 0 auto" class="list">
        <tr>
            <td colspan="4">
                <h2 style="text-align: center;margin: 0;">Search for Issues</h2>
            </td>
        </tr>
        <tr>
            <td class="alignright"><label for="id">ID:</label></td>
            <td colspan="3"><input id="id" name="id" type="text" size="20" maxlength="32" value="${issuesForm.id}"></td>
        </tr>
        <tr>
            <td class="alignright"><label for="title">Title:</label></td>
            <td colspan="3"><input id="title" name="title" type="text" size="20" maxlength="32" value="${issuesForm.title}"></td>
        </tr>
        <tr>
            <td class="alignright"><label for="description">Description:</label></td>
            <td colspan="3"><input id="description" name="description" type="text" size="20" maxlength="32" value="${issuesForm.description}"></td>
        </tr>

        <tr><td colspan="4" style="text-align: center"><input type="submit" value="Search" class="btn btn-primary" onclick="resetPagination();"/></td></tr>
        <tr><td colspan="4" style="text-align: center"><span>${searchResult.searchResultsSize} Results</span></td></tr>
    </table>
</form>
<br>
<table id="filmTable" style="margin: 0 auto" class="list">
    <thead>
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
    <tr class="listheading">
        <td></td>
        <td class="sortableHeader" onclick="sortFilms(this, 'id')">ID
            <span>
                <c:if test="${searchResult.sortColumn eq 'id' and searchResult.sortDirection eq 'asc'}">&#9650;</c:if>
                <c:if test="${searchResult.sortColumn eq 'id' and searchResult.sortDirection eq 'desc'}">&#9660;</c:if>
            </span>
        </td>
        <td class="sortableHeader" onclick="sortFilms(this, 'title')">Title
            <span>
                <c:if test="${searchResult.sortColumn eq 'title' and searchResult.sortDirection eq 'asc'}">&#9650;</c:if>
                <c:if test="${searchResult.sortColumn eq 'title' and searchResult.sortDirection eq 'desc'}">&#9660;</c:if>
            </span>
        </td>
        <td class="sortableHeader" onclick="sortFilms(this, 'description')">Description
            <span>
                <c:if test="${searchResult.sortColumn eq 'description' and searchResult.sortDirection eq 'asc'}">&#9650;</c:if>
                <c:if test="${searchResult.sortColumn eq 'description' and searchResult.sortDirection eq 'desc'}">&#9660;</c:if>
            </span>
        </td>
    </tr>
    </thead>

    <tbody id="myTBody">
    <c:set var="count" value="${1 + ((searchResult.page - 1) * 100)}"/>
    <c:set var="rowStyle" value="listrowodd"/>
    <c:set var="rowToggle" value="${true}"/>
    <c:forEach var="issue" items="${searchResult.pageOfResults}">

        <tr class="${rowStyle}">
            <td class="alignright"><fmt:formatNumber value="${count}" pattern="#,###"/></td>
            <td>
                ${issue.id}
            </td>
            <td>
                ${issue.title}
            </td>
            <td class="alignright">${issue.description}</td>
        </tr>

        <%--<tr id="${issue.id}_secondRow" style="display: none">--%>
            <%--<td colspan="100" class="aligncenter" style="height: 200px; padding: 1px 3px;">--%>
                <%--<div id="${issue.id}_animatedDiv" style="display: none;max-width: 700px; margin: 0 auto;">--%>
                    <%--<div style="float: left; padding-right: 10pt">--%>
                        <%--<img id="${issue.id}_posterUrl" src="" style="margin: 0 auto;"/>--%>
                    <%--</div>--%>
                    <%--<div style="width: 100%; text-align: left;">--%>
                        <%--<div><b>Running Time: </b></div>--%>
                        <%--<div>${issue.runtime}</div>--%>
                        <%--<div><b>Director: </b></div>--%>
                        <%--<div>${issue.director}</div>--%>
                        <%--<div><b>Actors: </b></div>--%>
                        <%--<div>${issue.actors}</div>--%>
                        <%--<div><b>Tomato Critic Consensus: </b></div>--%>
                        <%--<div>${issue.tomatoConsensus}</div>--%>
                        <%--<div><b>Plot: </b></div>--%>
                        <%--<div>${issue.plot}</div>--%>
                    <%--</div>--%>
                <%--</div>--%>
            <%--</td>--%>
        <%--</tr>--%>

        <c:if test="${rowToggle}"><c:set var="rowStyle" value="listroweven"/></c:if>
        <c:if test="${!rowToggle}"><c:set var="rowStyle" value="listrowodd"/></c:if>
        <c:set var="rowToggle" value="${!rowToggle}"/>
        <c:set var="count" value="${count + 1}"/>
    </c:forEach>
    </tbody>

    <c:if test="${empty searchResult.searchResults}">
        <tr><td colspan="100">-</td></tr>
    </c:if>
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
</table>
</body>
</html>