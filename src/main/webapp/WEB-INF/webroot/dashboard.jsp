<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="issueForms" type="java.util.List<com.hicks.beans.IssueForm>" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <title>BTS</title>
    <jsp:include page="inc_header.jsp"/>

    <script>
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
<body>

<jsp:include page="header.jsp"/>

<div class="mdl-grid">
    <c:forEach var="issueForm" items="${issueForms}">
        <c:set var="searchResult" value="${issueForm.searchResult}"/>

        <div class="mdl-card mdl-cell mdl-cell--6-col mdl-cell--4-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
            <div class="mdl-card__title"><h5>${issueForm.formName}: ${searchResult.size} Issues<i class="material-icons">mode_edit</i></h5></div>

            <div class="tableContainer">
                <c:set var="issueForm" value="${issueForm}" scope="request"/>
                <c:set var="searchResult" value="${searchResult}" scope="request"/>
                <jsp:include page="issueTable.jsp"/>
            </div>
        </div>  
    </c:forEach>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>