<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../inc_title.jsp"/>
    <jsp:include page="../inc_header.jsp"/>
</head>
<body>

<jsp:include page="../header.jsp"/>

<section class="hero is-primary is-small">
    <div class="hero-body">
        <div class="container">
            <h1 class="title">
                Audit Records
            </h1>
        </div>
    </div>
</section>

<section class="section">
    <div class="container">
        <div class="columns is-multiline is-centered">
            <div class="column is-one-fifth">
                <form:form name="frmFilter" id="frmFilter" method="post" modelAttribute="searchForm"
                           action="${pageContext.request.contextPath}/admin/audit/search">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <form:hidden path="user" />

                    <input type="hidden" name="id" id="id" value="${searchForm.id}"/>
                    <input type="hidden" name="sortColumn" id="sortColumn" value="${searchForm.sortColumn}"/>
                    <input type="hidden" name="sortDirection" id="sortDirection" value="${searchForm.sortDirection}"/>
                    <input type="hidden" name="page" id="page" value="${searchForm.page}"/>

                    <nav class="panel">
                        <p class="panel-heading">
                            Search for Audit Records
                        </p>
                        <div class="panel-block">
                            <t:text isSpring="true" id="issueId" label="Issue" value="${searchForm.issueId}" horizontal="false" />
                        </div>
                        <div class="panel-block">
                            <t:text isSpring="true" id="propertyName" label="Property Name" value="${searchForm.propertyName}" horizontal="false" />
                        </div>
                        <div class="panel-block">
                            <javatime:format value="${searchForm.fromEventDate}" style="MS" var="fromEvent"/>
                            <t:text isSpring="true" id="fromEventDate" label="From Event Time" value="${fromEvent}" horizontal="false" />
                        </div>
                        <div class="panel-block">
                            <javatime:format value="${searchForm.toEventDate}" style="MS" var="toEvent"/>
                            <t:text isSpring="true" id="toEventDate" label="To Event Time" value="${toEvent}" horizontal="false" />
                        </div>

                        <div class="panel-block">
                            <input type="submit" value="Search" class="button is-link is-outlined is-fullwidth" />
                        </div>
                    </nav>
                </form:form>
            </div>
            <div class="column has-text-centered">
                <div class="card">
                    <header class="card-header">
                        <p class="card-header-title">
                            Found ${searchForm.searchResult.size} Issues
                        </p>
                    </header>

                    <div class="card-content">
                        <div class="ajaxTableContainer">
                            <jsp:include page="../auditTable.jsp"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="../footer.jsp"/>
</body>
</html>