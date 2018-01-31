<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="dashBoardIssueForms" type="java.util.List<net.ehicks.bts.beans.IssueForm>" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="inc_title.jsp"/>
    <jsp:include page="inc_header.jsp"/>

    <script>
        function confirmDelete(id)
        {
            if (confirm('Are you sure you want to remove this saved search from your dashboard?'))
            {
                location.href = '${pageContext.request.contextPath}/view?tab1=dashboard&action=remove&issueFormId=' + id;
            }
        }
        function modifySavedSearch(id)
        {
            location.href = '${pageContext.request.contextPath}/view?tab1=search&action=form&issueFormId=' + id;
        }
    </script>
</head>
<body>

<jsp:include page="header.jsp"/>

<section class="hero is-primary is-small">
    <div class="hero-body">
        <div class="container">
            <h1 class="title">
                Dashboard
            </h1>
        </div>
    </div>
</section>

<section class="section">
    <div class="container">
        <div class="columns is-multiline is-centered">

            <c:if test="${fn:length(dashBoardIssueForms) < 2}"><c:set var="cols" value="" /></c:if>
            <c:if test="${fn:length(dashBoardIssueForms) == 2}"><c:set var="cols" value="is-half" /></c:if>
            <c:if test="${fn:length(dashBoardIssueForms) > 2}"><c:set var="cols" value="is-half" /></c:if>

            <c:forEach var="issueForm" items="${dashBoardIssueForms}">
                <c:set var="searchResult" value="${issueForm.searchResult}"/>

                <div class="column ${cols} has-text-centered">
                    <div class="card">
                        <header class="card-header">
                            <p class="card-header-title">
                                ${issueForm.formName}: ${searchResult.size} Issues
                            </p>
                            <a href="#" class="card-header-icon" aria-label="edit" onclick="modifySavedSearch(${issueForm.id})">
                                <span class="icon">
                                    <i class="fas fa-edit"></i>
                                </span>
                            </a>
                            <a href="#" class="card-header-icon" aria-label="remove" onclick="confirmDelete(${issueForm.id})">
                                <span class="icon">
                                    <i class="fas fa-trash"></i>
                                </span>
                            </a>
                        </header>

                        <div class="card-content">
                            <div class="ajaxTableContainer">
                                <c:set var="issueForm" value="${issueForm}" scope="request"/>
                                <c:set var="searchResult" value="${searchResult}" scope="request"/>
                                <jsp:include page="issueTable.jsp"/>
                            </div>
                        </div>
                    </div>

                </div>
            </c:forEach>
            <c:if test="${empty dashBoardIssueForms}">
                <div class="column is-one-third has-text-centered">
                    <h1 class="title">No saved filters found</h1>
                    <h2 class="subtitle">Add issue forms to your dashboard.</h2>
                </div>
            </c:if>
        </div>
    </div>
</section>

<jsp:include page="footer.jsp"/>
</body>
</html>