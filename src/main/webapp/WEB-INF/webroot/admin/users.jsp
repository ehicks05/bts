<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="users" type="java.util.List<com.hicks.beans.User>" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <title>BTS</title>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/webroot/inc_header.jsp"/>

    <script>
        function deleteUser(userId)
        {
            if (confirm('Are you sure?'))
                location.href="${pageContext.request.contextPath}/view?tab1=admin&tab2=users&action=delete&userId=" + userId;
        }
    </script>
</head>
<body>

<jsp:include page="${pageContext.request.contextPath}/WEB-INF/webroot/header.jsp"/>

<div class="mdl-grid">

    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>Manage Users</h5></div>

        <div class="tableContainer">
            <table class="mdl-data-table mdl-js-data-table mdl-data-table--selectable mdl-shadow--2dp">
                <thead>
                    <tr>
                        <th>Object Id</th>
                        <th class="mdl-data-table__cell--non-numeric">Logon Id</th>
                        <th>Enabled</th>
                        <th></th>
                    </tr>
                </thead>
                <c:forEach var="user" items="${users}">
                    <tr>
                        <td><a href="${pageContext.request.contextPath}/view?tab1=main&tab2=profile&action=form&userId=${user.id}">${user.id}</a></td>
                        <td class="mdl-data-table__cell--non-numeric"><a href="${pageContext.request.contextPath}/view?tab1=main&tab2=profile&action=form&userId=${user.id}">${user.logonId}</a></td>
                        <td class="mdl-data-table__cell--non-numeric">${user.enabled}</td>
                        <td><a onclick="deleteUser('${user.id}');" class="clickable material-icons">delete</a></td>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <div class="mdl-card__actions">
            <input id="addUserButton" type="button" value="Add User" class="mdl-button mdl-js-button mdl-button--raised" />
        </div>
    </div>
</div>

<dialog id="addUserDialog" class="mdl-dialog">
    <h4 class="mdl-dialog__title">Add User</h4>
    <div class="mdl-dialog__content">
        <form id="frmCreateUser" name="frmCreateUser" method="post" action="${pageContext.request.contextPath}/view?tab1=admin&tab2=users&action=create">
            <table>
                <tr>
                    <td>Logon Id:</td>
                    <td>
                        <input type="text" id="fldLogonId" name="fldLogonId" size="20" maxlength="256" value="" required/>
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
    var addUserDialog = document.querySelector('#addUserDialog');
    var addUserButton = document.querySelector('#addUserButton');
    if (!addUserDialog.showModal)
    {
        dialogPolyfill.registerDialog(addUserDialog);
    }
    addUserButton.addEventListener('click', function ()
    {
        addUserDialog.showModal();
    });
    document.querySelector('#addUserDialog .create').addEventListener('click', function ()
    {
        if (!document.querySelector('#fldLogonId').value)
            alert('Please enter a Logon Id.');
        else
            $('#frmCreateUser').submit();
    });
    addUserDialog.querySelector('#addUserDialog .close').addEventListener('click', function ()
    {
        addUserDialog.close();
    });
</script>

<jsp:include page="${pageContext.request.contextPath}/WEB-INF/webroot/footer.jsp"/>
</body>
</html>