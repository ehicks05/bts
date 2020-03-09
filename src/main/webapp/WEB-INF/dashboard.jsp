<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="dashBoardIssueForms" type="java.util.List<net.ehicks.bts.beans.IssueForm>" scope="request"/>
<%@page import="org.slf4j.LoggerFactory"%>
<%@ page import="org.slf4j.Logger" %>
<%! static Logger log = LoggerFactory.getLogger("dashboard"); %>

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
                location.href = '${pageContext.request.contextPath}/dashboard/remove?issueFormId=' + id;
            }
        }
        function move(id, direction)
        {
            location.href = '${pageContext.request.contextPath}/dashboard/move?issueFormId=' + id + '&direction=' + direction;
        }
        function modifySavedSearch(id)
        {
            location.href = '${pageContext.request.contextPath}/search/form?issueFormId=' + id;
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
    <div class="columns is-multiline is-centered">

        <c:if test="${fn:length(dashBoardIssueForms) < 2}"><c:set var="cols" value="" /></c:if>
        <c:if test="${fn:length(dashBoardIssueForms) == 2}"><c:set var="cols" value="is-half" /></c:if>
        <c:if test="${fn:length(dashBoardIssueForms) > 2}"><c:set var="cols" value="is-half" /></c:if>

        <c:forEach var="issueForm" items="${dashBoardIssueForms}">
            <% log.debug("    Start issueform iteration"); %>
            <% long startDash = System.nanoTime(); %>
            <c:set var="searchResult" value="${issueForm.searchResult}"/>

            <div class="column is-half-desktop is-half-widescreen is-half-fullhd has-text-centered">
                <div class="card" id="issueForm${issueForm.id}">
                    <header class="card-header">
                        <p class="card-header-title">
                            ${issueForm.formName}: ${searchResult.size} Issues
                        </p>
                        <a href="#" class="card-header-icon" aria-label="edit" title="Edit" onclick="modifySavedSearch(${issueForm.id})">
                            <span class="icon">
                                <i class="fas fa-edit"></i>
                            </span>
                        </a>

                        <c:if test="${issueForm.index == minIndex}">
                            <span class="card-header-icon" style="cursor: default" aria-label="move up" title="move up">
                                <span class="icon">
                                    <i class="fas fa-arrow-up"></i>
                                </span>
                            </span>
                        </c:if>
                        <c:if test="${issueForm.index != minIndex}">
                            <a href="#" class="card-header-icon" aria-label="move up" title="move up" onclick="move(${issueForm.id}, 'up')">
                                <span class="icon">
                                    <i class="fas fa-arrow-up"></i>
                                </span>
                            </a>
                        </c:if>
                        <c:if test="${issueForm.index == maxIndex}">
                            <span class="card-header-icon" style="cursor: default" aria-label="move down" title="move down">
                                <span class="icon">
                                    <i class="fas fa-arrow-down"></i>
                                </span>
                            </span>
                        </c:if>
                        <c:if test="${issueForm.index != maxIndex}">
                            <a href="#" class="card-header-icon" aria-label="move down" title="move down" onclick="move(${issueForm.id}, 'down')">
                                <span class="icon">
                                    <i class="fas fa-arrow-down"></i>
                                </span>
                            </a>
                        </c:if>

                        <a href="#" class="card-header-icon" aria-label="remove from dashboard" title="remove from dashboard" onclick="confirmDelete(${issueForm.id})">
                            <span class="icon">
                                <i class="fas fa-thumbtack"></i>
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
            <% long dur = (System.nanoTime() - startDash) / 1_000_000; %>
            <% log.debug("    End issueform iteration " + dur + " ms"); %>

        </c:forEach>

        <c:if test="${empty dashBoardIssueForms}">
            <div class="column is-one-third has-text-centered">
                <h1 class="title">No saved filters found</h1>
                <h2 class="subtitle">Add issue forms to your dashboard.</h2>
            </div>
        </c:if>
    </div>
</section>

<% long startFooter = System.nanoTime(); %>
<jsp:include page="footer.jsp"/>
<% long footduration = (System.nanoTime() - startFooter) / 1_000_000; %>
<% log.debug("    footer jsp " + footduration + " ms"); %>

</body>
</html>