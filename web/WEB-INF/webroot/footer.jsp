<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script>
    function showCreateDialog()
    {
        $("#createIssueDialog").dialog({
            autoOpen: false,
            modal: true,
            buttons: {
                "Create" : function () {$('#frmCreate').submit()},
                "Cancel" : function () {$("#createIssueDialog").dialog("close")}
            }
        });

        $("#createIssueDialog").dialog("open");
    }
</script>
</div> <!-- Close the div we left open in the header -->
<div style="display: none;">
    <div id="createIssueDialog" title="Create Issue">
        <form id="frmCreate" name="frmCreate" method="post" action="${pageContext.request.contextPath}/view?tab1=main&tab2=issue&action=create">
            <table>
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
                    <td>Title:</td>
                    <td>
                        <input type="text" name="fldTitle" size="20" maxlength="256" value="" required/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>