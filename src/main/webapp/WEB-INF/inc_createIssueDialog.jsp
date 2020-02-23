<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<div class="modal" id="createIssueDialog">
    <div class="modal-background"></div>
    <div class="modal-card">
        <header class="modal-card-head">
            <p class="modal-card-title">Create Issue</p>
            <button class="delete" aria-label="close"></button>
        </header>
        <section class="modal-card-body">
            <form id="frmCreateIssue" name="frmCreateIssue" method="post" action="${pageContext.request.contextPath}/issue/create">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <t:text        id="createIssueTitle" label="Title" required="true" />
                <t:text        id="createIssueDescription" label="Description" required="true" />
                <t:basicSelect id="createIssueProject" label="Project" items="${projects}" required="true" />
                <t:basicSelect id="createIssueGroup" label="Group" items="${groups}" required="true" />
                <t:basicSelect id="createIssueIssueType" label="Issue Type" items="${issueTypes}" required="true" />
                <t:basicSelect id="createIssueSeverity" label="Severity" items="${severities}" required="true" />
                <t:basicSelect id="createIssueStatus" label="Status" items="${statuses}" required="true" />
            </form>
        </section>
        <footer class="modal-card-foot">
            <button class="button is-primary create">Create</button>
            <button class="button close">Cancel</button>
        </footer>
    </div>
</div>

<script>
    initDialog('createIssue');

    $(function () {
        var createIssueDialog = $('#createIssueDialog');

        createIssueDialog.find('.create').on('click', function ()
        {
            if ($('#createIssueTitle').val() && $('#createIssueDescription').val())
                $('#frmCreateIssue').submit();
            else
                alert('Please enter a title and description.');
        });
    });
</script>