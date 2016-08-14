<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="issueForm" type="com.hicks.beans.IssueForm" scope="request"/>
<jsp:useBean id="searchResult" type="com.hicks.SearchResult" scope="request"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@taglib prefix="tagFile" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
<head>
    <title>BTS</title>
    <jsp:include page="inc_header.jsp"/>
    <script>
        var page = ${issueForm.page};
        var pages = ${searchResult.pages};
        var sortColumn = '${issueForm.sortColumn}';
        var sortDirection = '${issueForm.sortDirection}';

        function initHeader()
        {

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

            $('#frmFilter').submit();
        }

        function goToPage(pageNumber)
        {
            var parsedPage = '';
            if (pageNumber == 'first') parsedPage = 1;
            if (pageNumber == 'last') parsedPage = pages;
            if (pageNumber == 'next') parsedPage = (page + 1);
            if (pageNumber == 'previous') parsedPage = (page - 1);

            $('#page').val(parsedPage);

            $('#frmFilter').submit();
        }

        function resetPagination()
        {
            $('#resetPage').val('yes');
        }

        function saveIssueForm()
        {
            $('#frmFilter').attr('action', '${pageContext.request.contextPath}/view?tab1=main&tab2=issue&action=saveIssueForm');
        }

        function ajaxFilms(callingElementId, issueFormId, newPage, newSortColumn, newSortDirection)
        {
            var myUrl = '${pageContext.request.contextPath}/view?tab2=issue&action=ajaxGetPageOfResults';
            var params = {};
            if (issueFormId) params.issueFormId = issueFormId;
            if (newPage) params.page = newPage;
            if (newSortColumn) params.sortColumn = newSortColumn;
            if (newSortDirection) params.sortDirection = newSortDirection;

            $.get(myUrl, params,
                    function(data, textStatus, xhr)
                    {
                        if(textStatus == "success")
                        {
                            var rows = [];

                            $(callingElementId).closest('.tableContainer').html(data);
                        }
                        if (textStatus == "error")
                            alert("Error: " + xhr.status + ": " + xhr.statusText);
                    });
        }
    </script>

</head>
<body onload="initHeader();">

<jsp:include page="header.jsp"/>

<div class="mdl-grid">
    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-shadow--2dp">
        <form name="frmFilter" id="frmFilter" method="post" action="${pageContext.request.contextPath}/view?tab1=main&tab2=issue&action=search">
            <input type="hidden" id="fldRating" name="fldRating">
            <input type="hidden" name="sortColumn" id="sortColumn" value="${issueForm.sortColumn}"/>
            <input type="hidden" name="sortDirection" id="sortDirection" value="${issueForm.sortDirection}"/>
            <input type="hidden" name="page" id="page" value="${issueForm.page}"/>
            <input type="hidden" name="resetPage" id="resetPage"/>

            <div class="mdl-card__supporting-text">

                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" type="text" size="20" maxlength="32" id="containsText" name="containsText" value="${issueForm.containsText}">
                    <label class="mdl-textfield__label" for="containsText">Contains Text:</label>
                </div>

                <br>
                <div>
                    <label for="severity">Severity: </label>
                    <ct:select id="severity" value="${issueForm.severityId}" items="${severities}" required="${false}"/>
                </div>
            </div>
            <div class="mdl-card__actions">
                <input type="submit" value="Search" class="mdl-button mdl-js-button mdl-button--raised" onclick="resetPagination();" />
                <input type="submit" value="Save" class="mdl-button mdl-js-button mdl-button--raised" onclick="saveIssueForm();" />
            </div>
        </form>
    </div>

    <br>
    <div class="mdl-card mdl-cell mdl-cell--8-col mdl-cell--4-col-tablet mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>Found ${searchResult.size} Issues</h5></div>

        <div class="tableContainer">
            <jsp:include page="issueTable.jsp"/>
            <%--<tagFile:issueTable issueForm="${issueForm}" searchResult="${searchResult}"/>--%>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>