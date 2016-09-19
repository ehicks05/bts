<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="dashBoardIssueForms" type="java.util.List<com.hicks.beans.IssueForm>" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <title>BTS</title>
    <jsp:include page="inc_header.jsp"/>

    <script>
        function ajaxFilms(callingElementId, issueFormId, newPage, newSortColumn, newSortDirection)
        {
            var myUrl = '${pageContext.request.contextPath}/view?tab1=main&tab2=search&action=ajaxGetPageOfResults';
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
    <c:forEach var="issueForm" items="${dashBoardIssueForms}">
        <c:set var="searchResult" value="${issueForm.searchResult}"/>
        <c:if test="${fn:length(dashBoardIssueForms) < 2}"><c:set var="cols" value="12" /></c:if>
        <c:if test="${fn:length(dashBoardIssueForms) >= 2}"><c:set var="cols" value="6" /></c:if>
        <div class="mdl-card mdl-cell mdl-cell--${cols}-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
            <div class="mdl-card__title"><h5>${issueForm.formName}: ${searchResult.size} Issues
                <br>
                <a class="material-icons" href="${pageContext.request.contextPath}/view?tab1=main&tab2=search&action=form&issueFormId=${issueForm.id}">mode_edit</a>
                <a class="material-icons" href="${pageContext.request.contextPath}/view?tab1=main&tab2=dashboard&action=remove&issueFormId=${issueForm.id}">clear</a></h5></div>

            <div class="tableContainer">
                <c:set var="issueForm" value="${issueForm}" scope="request"/>
                <c:set var="searchResult" value="${searchResult}" scope="request"/>
                <jsp:include page="issueTable.jsp"/>
            </div>
        </div>  
    </c:forEach>
    <c:if test="${empty dashBoardIssueForms}">
        <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
            <div class="mdl-card__title"><h5>None Found</h5></div>

            <div class="tableContainer">
                Add issue forms to your dashboard.
            </div>
        </div>
    </c:if>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>