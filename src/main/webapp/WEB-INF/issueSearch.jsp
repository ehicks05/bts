<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<jsp:useBean id="issueForm" type="net.ehicks.bts.beans.IssueForm" scope="request"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="inc_title.jsp"/>
    <jsp:include page="inc_header.jsp"/>
    <script>
        $(function (){
            $('#formName').on('keyup', enableSaveFilterButton);
            enableSaveFilterButton();

            $('#saveFilterButton').on('click', saveIssueForm);
        });

        function saveIssueForm()
        {
            $('#frmFilter').attr('action', '${pageContext.request.contextPath}/search/saveIssueForm').submit();
        }

        function addToDashboard(issueFormId)
        {
            location.href = '${pageContext.request.contextPath}/search/addToDashboard?issueFormId=' + issueFormId;
        }

        function enableSaveFilterButton(e)
        {
            if ($('#formName').val())
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
                <form:form name="frmFilter" id="frmFilter" method="post" action="${pageContext.request.contextPath}/search/search" modelAttribute="issueForm" >
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <input type="hidden" name="formId" id="formId" value="${issueForm.id}"/>

                    <nav class="panel">
                    <p class="panel-heading">
                        <c:set var="formName" value="${issueForm.id == 0 || fn:length(issueForm.formName) == 0 ? 'New Form' : issueForm.formName}"/>
                        Issue Filter: ${formName}
                    </p>
                    <div class="panel-block">
                        <input type="hidden" name="sortColumn" id="sortColumn" value="${issueForm.sortColumn}"/>
                        <input type="hidden" name="sortDirection" id="sortDirection" value="${issueForm.sortDirection}"/>
                        <input type="hidden" name="page" id="page" value="${issueForm.page}"/>
                        <input type="hidden" name="id" id="id" value="${issueForm.id}"/>

                        <t:text id="formName" label="Form Name" value="${issueForm.formName}" horizontal="false" />
                    </div>
                    <div class="panel-block">
                        <t:text id="containsText" label="Contains Text" value="${issueForm.containsText}" horizontal="false" />
                    </div>
                    <div class="panel-block">
                        <t:multiSelect id="projectIds" selectedValues="${issueForm.projectIds}" items="${projects}" placeHolder="Projects:"/>
                    </div>
                    <div class="panel-block">
                        <t:multiSelect id="groupIds" selectedValues="${issueForm.groupIds}" items="${groups}" placeHolder="Groups:"/>
                    </div>
                    <div class="panel-block">
                        <t:multiSelect id="severityIds" selectedValues="${issueForm.severityIds}" items="${severities}" placeHolder="Severities:"/>
                    </div>
                    <div class="panel-block">
                        <t:multiSelect id="statusIds" selectedValues="${issueForm.statusIds}" items="${statuses}" placeHolder="Statuses:"/>
                    </div>
                    <div class="panel-block">
                        <t:multiSelect id="assigneeIds" selectedValues="${issueForm.assigneeIds}" items="${users}" placeHolder="Assignees:"/>
                    </div>
                    <div class="panel-block">
                        <c:if test="${!empty issueForm.id && issueForm.id != 0}">
                            <t:checkbox id="onDash" label="On Dashboard" checked="${issueForm.onDash}" horizontal="false" />
                        </c:if>
                    </div>

                    <div class="panel-block">
                        <input type="submit" value="Search" class="button is-link is-outlined is-fullwidth" />
                    </div>
                    <div class="panel-block">
                        <button id="saveFilterButton" class="button is-link is-outlined is-fullwidth" disabled>
                            Save (Enter a filter name)
                        </button>
                    </div>
                </nav>
                </form:form>
            </div>
            <div class="column has-text-centered">
                <div class="card">
                    <header class="card-header">
                        <p class="card-header-title">
                            Found ${issueForm.searchResult.size} Issues
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