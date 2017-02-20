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
    <title>BTS</title>
    <jsp:include page="inc_header.jsp"/>
    <script>
        function initHeader()
        {
            var filterName = $('#fldName').val();
            $('#filterName').val(filterName);
        }

        function saveIssueForm()
        {
            $('#frmFilter').attr('action', '${pageContext.request.contextPath}/view?tab1=search&action=saveIssueForm').submit();
        }

        function addToDashboard(issueFormId)
        {
            location.href = '${pageContext.request.contextPath}/view?tab1=search&action=addToDashboard&issueFormId=' + issueFormId;
        }
    </script>

</head>
<body onload="initHeader();">

<jsp:include page="header.jsp"/>

<div class="mdl-grid">
    <div class="mdl-card mdl-cell mdl-cell--4-col mdl-cell--8-col-tablet mdl-shadow--2dp">
        <c:set var="formName" value="${fn:length(issueForm.formName) == 0 ? 'New Filter' : issueForm.formName}"/>
        <div class="mdl-card__title"><h5>Issue Filter: ${formName}</h5></div>
        <form name="frmFilter" id="frmFilter" method="post" action="${pageContext.request.contextPath}/view?tab1=search&action=search">
            <input type="hidden" name="sortColumn" id="sortColumn" value="${issueForm.sortColumn}"/>
            <input type="hidden" name="sortDirection" id="sortDirection" value="${issueForm.sortDirection}"/>
            <input type="hidden" name="page" id="page" value="${issueForm.page}"/>
            <input type="hidden" name="filterName" id="filterName"/>
            <input type="hidden" name="filterId" id="filterId" value="${issueForm.id}"/>

            <div style="padding: 10px;">

                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" type="text" size="20" maxlength="32" id="containsText" name="containsText" value="${issueForm.containsText}">
                    <label class="mdl-textfield__label" for="containsText">Contains Text:</label>
                </div>

                <br>
                <t:multiSelect id="projectIds" selectedValues="${issueForm.projectIdsAsList}" items="${projects}" placeHolder="Projects:"/>
                <br>
                <t:multiSelect id="groupIds" selectedValues="${issueForm.groupIdsAsList}" items="${groups}" placeHolder="Groups:"/>
                <br>
                <t:multiSelect id="severityIds" selectedValues="${issueForm.severityIdsAsList}" items="${severities}" placeHolder="Severities:"/>
                <br>
                <t:multiSelect id="statusIds" selectedValues="${issueForm.statusIdsAsList}" items="${statuses}" placeHolder="Statuses:"/>
                <br>
                <t:multiSelect id="assigneeIds" selectedValues="${issueForm.assigneeUserIdsAsList}" items="${users}" placeHolder="Assignees:"/>
            </div>
            <div class="mdl-card__actions">
                <input type="submit" value="Search" class="mdl-button mdl-js-button mdl-button--raised" />
                <input type="button" value="Save" class="mdl-button mdl-js-button mdl-button--raised" id="showSaveIssueFormDialog" />

                <c:if test="${empty issueForm.id || issueForm.id == 0 || issueForm.onDash}">
                    <c:set var="addToDashStatus" value="disabled"/>
                </c:if>
                <input type="button" value="Add to Dash" class="mdl-button mdl-js-button mdl-button--raised" onclick="addToDashboard('${issueForm.id}');" ${addToDashStatus}/>
            </div>
        </form>
    </div>

    <br>
    <div class="mdl-card mdl-cell mdl-cell--8-col mdl-cell--8-col-tablet mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>Found ${searchResult.size} Issues</h5></div>

        <div class="tableContainer">
            <jsp:include page="issueTable.jsp"/>
        </div>
    </div>
</div>

<dialog class="mdl-dialog" id="saveFilterDialog">
    <h4 class="mdl-dialog__title">Save Issue Filter</h4>
    <div class="mdl-dialog__content">
        <form id="frmSave" name="frmSave" method="post" action="${pageContext.request.contextPath}/view?tab1=search&action=saveIssueForm">
            <table>
                <tr>
                    <td>Filter Name:</td>
                    <td><input type="text" id="fldName" name="fldName" size="20" maxlength="256" value="${issueForm.formName}" required/></td>
                </tr>
            </table>
        </form>
    </div>
    <div class="mdl-dialog__actions">
        <button type="button" class="mdl-button save">Save</button>
        <button type="button" class="mdl-button close">Cancel</button>
    </div>
</dialog>
<script>
    var dialog = document.querySelector('#saveFilterDialog');
    var showDialogButton = document.querySelector('#showSaveIssueFormDialog');
    if (!dialog.showModal)
    {
        dialogPolyfill.registerDialog(dialog);
    }
    showDialogButton.addEventListener('click', function ()
    {
        dialog.showModal();
    });
    dialog.querySelector('.save').addEventListener('click', function ()
    {
        var filterName = $('#fldName').val();
        $('#filterName').val(filterName);
        saveIssueForm();
    });
    dialog.querySelector('.close').addEventListener('click', function ()
    {
        dialog.close();
    });
</script>

<jsp:include page="footer.jsp"/>
</body>
</html>