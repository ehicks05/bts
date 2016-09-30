<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<dialog class="mdl-dialog">
    <h4 class="mdl-dialog__title">Create Issue</h4>
    <div class="mdl-dialog__content">
        <form id="frmCreate" name="frmCreate" method="post" action="${pageContext.request.contextPath}/view?tab1=main&tab1=main&tab2=issue&action=create">
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
                    <td>Zone:</td>
                    <td>
                        <select name="fldZone" required>
                            <c:forEach var="zone" items="${zones}">
                                <option name="${zone.name}" value="${zone.id}">${zone.name}</option>
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
    var dialog = document.querySelector('dialog');
    var showDialogButton = document.querySelector('#show-create-issue-dialog');
    if (!dialog.showModal)
    {
        dialogPolyfill.registerDialog(dialog);
    }
    showDialogButton.addEventListener('click', function ()
    {
        dialog.showModal();
    });
    document.querySelector('.create').addEventListener('click', function ()
    {
        $('#frmCreate').submit()
    });
    dialog.querySelector('.close').addEventListener('click', function ()
    {
        dialog.close();
    });
</script>