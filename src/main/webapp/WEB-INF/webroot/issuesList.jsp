<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="issueForm" type="com.hicks.beans.IssueForm" scope="request"/>
<jsp:useBean id="searchResult" type="com.hicks.SearchResult" scope="request"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>BTS</title>
    <jsp:include page="inc_header.jsp"/>
    <script>
        function initHeader()
        {

        }

        function saveIssueForm()
        {
            $('#frmFilter').attr('action', '${pageContext.request.contextPath}/view?tab1=main&tab2=search&action=saveIssueForm').submit();
        }

        function ajaxFilms(callingElementId, issueFormId, newPage, newSortColumn, newSortDirection)
        {
            var myUrl = '${pageContext.request.contextPath}/view?tab2=search&action=ajaxGetPageOfResults';
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
<body onload="initHeader();">

<jsp:include page="header.jsp"/>

<div class="mdl-grid">
    <div class="mdl-card mdl-cell mdl-cell--4-col mdl-cell--8-col-tablet mdl-shadow--2dp">
        <c:set var="formName" value="${fn:length(issueForm.formName) == 0 ? 'New Filter' : issueForm.formName}"/>
        <div class="mdl-card__title"><h5>Issue Filter: ${formName}</h5></div>
        <form name="frmFilter" id="frmFilter" method="post" action="${pageContext.request.contextPath}/view?tab1=main&tab2=search&action=search">
            <input type="hidden" id="fldRating" name="fldRating">
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
                <label for="projectIds">Projects: </label>
                <ct:multiSelect id="projectIds" selectedValues="${issueForm.projectIdsAsList}" items="${projects}" required="${false}"/>
                <br>
                <label for="zoneIds">Zones: </label>
                <ct:multiSelect id="zoneIds" selectedValues="${issueForm.zoneIdsAsList}" items="${zones}" required="${false}"/>
                <br>
                <label for="severityIds">Severities: </label>
                <ct:multiSelect id="severityIds" selectedValues="${issueForm.severityIdsAsList}" items="${severities}" required="${false}"/>
                <br>
                <label for="statusIds">Statuses: </label>
                <ct:multiSelect id="statusIds" selectedValues="${issueForm.statusIdsAsList}" items="${statuses}" required="${false}"/>
                <br>
                <label for="assigneeIds">Assignees: </label>
                <ct:multiSelect id="assigneeIds" selectedValues="${issueForm.assigneeUserIdsAsList}" items="${users}" required="${false}"/>
            </div>
            <div class="mdl-card__actions">
                <input type="submit" value="Search" class="mdl-button mdl-js-button mdl-button--raised" />
                <input type="button" value="Save" class="mdl-button mdl-js-button mdl-button--raised" id="showSaveIssueFormDialog" />
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
        <form id="frmSave" name="frmSave" method="post" action="${pageContext.request.contextPath}/view?tab1=main&tab2=search&action=saveIssueForm">
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