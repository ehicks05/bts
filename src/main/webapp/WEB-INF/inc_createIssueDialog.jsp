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
            <div id="createIssueContainer">

            </div>
        </section>
        <footer class="modal-card-foot">
            <button class="button is-primary create">Create</button>
            <button class="button close">Cancel</button>
        </footer>
    </div>
</div>

<script>
    function initCreateIssueDialog()
    {
        $('#createIssueButton').off('click', initCreateIssueDialog); // we only want to ajax-in the form once
        $('#createIssueDialog').addClass('is-active');

        // this will load the form html, and then comprehensiveInitDialog will wire
        ajaxContent('createIssueContainer', '${pageContext.request.contextPath}', '/issue/ajaxCreateIssueForm', {}, wireUpCreateIssueDialog);
    }

    function wireUpCreateIssueDialog() {
        initDialog('createIssue');

        const createIssueDialog = $('#createIssueDialog');
        createIssueDialog.find('.create').on('click', function ()
        {
            if ($('#createIssueTitle').val() && $('#createIssueDescription').val())
                $('#frmCreateIssue').submit();
            else
                alert('Please enter a title and description.');
        });
    }

    $(function () {
        $('#createIssueButton').on('click', initCreateIssueDialog);
    });
</script>