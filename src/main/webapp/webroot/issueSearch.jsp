<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="issueForm" type="net.ehicks.bts.beans.IssueForm" scope="request"/>
<jsp:useBean id="searchResult" type="net.ehicks.bts.SearchResult" scope="request"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="inc_title.jsp"/>
    <jsp:include page="inc_header.jsp"/>
    <script>
        $(function (){
            $('#filterName').on('keyup', enableSaveFilterButton);
            enableSaveFilterButton();

            $('#saveFilterButton').on('click', saveIssueForm);
        });

        function saveIssueForm()
        {
            $('#frmFilter').attr('action', '${pageContext.request.contextPath}/view?tab1=search&action=saveIssueForm').submit();
        }

        function addToDashboard(issueFormId)
        {
            location.href = '${pageContext.request.contextPath}/view?tab1=search&action=addToDashboard&issueFormId=' + issueFormId;
        }

        function enableSaveFilterButton(e)
        {
            if ($('#filterName').val())
            {
                $('#saveFilterButton').prop('disabled', false);
                $('#saveFilterButton').html('Save');
            }
            else
            {
                $('#saveFilterButton').prop('disabled', true);
                $('#saveFilterButton').html('Save (Enter a filter name)');
            }
        }
    </script>

</head>
<body>

<jsp:include page="header.jsp"/>

<section class="hero is-primary is-small">
    <div class="hero-body">
        <div class="container">
            <h1 class="title">
                Search
            </h1>
        </div>
    </div>
</section>

<section class="section">
    <div class="container">
        <div class="columns is-multiline is-centered">
            <div class="column is-one-fifth">
                <form name="frmFilter" id="frmFilter" method="post" action="${pageContext.request.contextPath}/view?tab1=search&action=search">
                <nav class="panel">
                    <p class="panel-heading">
                        <c:set var="formName" value="${fn:length(issueForm.formName) == 0 ? 'New Filter' : issueForm.formName}"/>
                        Issue Filter: ${formName}
                    </p>
                    <div class="panel-block">
                        <input type="hidden" name="sortColumn" id="sortColumn" value="${issueForm.sortColumn}"/>
                        <input type="hidden" name="sortDirection" id="sortDirection" value="${issueForm.sortDirection}"/>
                        <input type="hidden" name="page" id="page" value="${issueForm.page}"/>
                        <%--<input type="hidden" name="filterName" id="filterName"/>--%>
                        <input type="hidden" name="filterId" id="filterId" value="${issueForm.id}"/>

                        <t:text id="filterName" label="Filter Name" value="${issueForm.formName}" horizontal="false" />
                    </div>
                    <div class="panel-block">
                        <t:text id="containsText" label="Contains Text" value="${issueForm.containsText}" horizontal="false" />
                    </div>
                    <div class="panel-block">
                        <t:multiSelect id="projectIds" selectedValues="${issueForm.projectIdsAsList}" items="${projects}" placeHolder="Projects:"/>
                    </div>
                    <div class="panel-block">
                        <t:multiSelect id="groupIds" selectedValues="${issueForm.groupIdsAsList}" items="${groups}" placeHolder="Groups:"/>
                    </div>
                    <div class="panel-block">
                        <t:multiSelect id="severityIds" selectedValues="${issueForm.severityIdsAsList}" items="${severities}" placeHolder="Severities:"/>
                    </div>
                    <div class="panel-block">
                        <t:multiSelect id="statusIds" selectedValues="${issueForm.statusIdsAsList}" items="${statuses}" placeHolder="Statuses:"/>
                    </div>
                    <div class="panel-block">
                        <t:multiSelect id="assigneeIds" selectedValues="${issueForm.assigneeUserIdsAsList}" items="${users}" placeHolder="Assignees:"/>
                    </div>

                    <div class="panel-block">
                        <input type="submit" value="Search" class="button is-link is-outlined is-fullwidth" />
                    </div>
                    <div class="panel-block">
                        <button id="saveFilterButton" class="button is-link is-outlined is-fullwidth" disabled>
                            Save (Enter a filter name)
                        </button>
                    </div>
                    <div class="panel-block">
                        <c:set var="addToDashStatus" value="${empty issueForm.id || issueForm.id == 0 || issueForm.onDash ? 'disabled' : ''}"/>
                        <button class="button is-link is-outlined is-fullwidth" onclick="addToDashboard('${issueForm.id}');" ${addToDashStatus}>
                            Add to Dash
                        </button>
                    </div>
                </nav>
                </form>
            </div>
            <div class="column has-text-centered">
                <div class="card">
                    <header class="card-header">
                        <p class="card-header-title">
                            Found ${searchResult.size} Issues
                        </p>
                    </header>

                    <div class="card-content">
                        <div class="ajaxTableContainer">
                            <jsp:include page="issueTable.jsp"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="footer.jsp"/>
</body>
</html>