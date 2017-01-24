<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<dialog id="createIssueDialog" class="mdl-dialog">
    <h4 class="mdl-dialog__title">Create Issue</h4>
    <div class="mdl-dialog__content">
        <form id="frmCreateIssue" name="frmCreateIssue" method="post" action="${pageContext.request.contextPath}/view?tab1=main&tab1=main&tab2=issue&action=create">
            <table>
                <tr>
                    <td>Title:</td>
                    <td>
                        <input type="text" name="fldTitle" size="20" maxlength="256" value="" required/>
                    </td>
                </tr>
                <tr>
                    <td>Project:</td>
                    <td>
                        <select name="fldProject" required>
                            <c:forEach var="project" items="${projects}">
                                <option name="${project.name}" value="${project.id}">${project.name}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>Group:</td>
                    <td>
                        <select name="fldGroup" required>
                            <c:forEach var="group" items="${groups}">
                                <option name="${group.name}" value="${group.id}">${group.name}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>Issue Type:</td>
                    <td>
                        <select name="fldIssueType" required>
                            <c:forEach var="issueType" items="${issueTypes}">
                                <option name="${issueType.name}" value="${issueType.id}">${issueType.name}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>Severity:</td>
                    <td>
                        <select name="fldSeverity" required>
                            <c:forEach var="severity" items="${severities}">
                                <option name="${severity.name}" value="${severity.id}">${severity.name}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>Status:</td>
                    <td>
                        <select name="fldStatus" required>
                            <c:forEach var="status" items="${statuses}">
                                <option name="${status.name}" value="${status.id}">${status.name}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>Description:</td>
                    <td>
                        <input type="text" name="fldDescription" size="20" maxlength="1024" value=""/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div class="mdl-dialog__actions">
        <button type="button" class="mdl-button create">Create</button>
        <button type="button" class="mdl-button close">Cancel</button>
    </div>
</dialog>
<script>
    var createIssueDialog = document.querySelector('#createIssueDialog');
    var showDialogButton = document.querySelector('#show-create-issue-dialog');
    if (!createIssueDialog.showModal)
    {
        dialogPolyfill.registerDialog(createIssueDialog);
    }
    showDialogButton.addEventListener('click', function ()
    {
        createIssueDialog.showModal();
    });
    document.querySelector('#createIssueDialog .create').addEventListener('click', function ()
    {
        $('#frmCreateIssue').submit()
    });
    createIssueDialog.querySelector('#createIssueDialog .close').addEventListener('click', function ()
    {
        createIssueDialog.close();
    });
</script>

<script>
    $(document).ready(showResponseMessage);
</script>