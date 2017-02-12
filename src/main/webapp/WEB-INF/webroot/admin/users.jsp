<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="users" type="java.util.List<net.ehicks.bts.beans.User>" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <title>BTS</title>
    <jsp:include page="../inc_header.jsp"/>

    <script>
        function deleteUser(userId)
        {
            if (confirm('Are you sure?'))
                location.href="${pageContext.request.contextPath}/view?tab1=admin&tab2=users&action=delete&userId=" + userId;
        }

        function printUsers()
        {
            window.open(
                '${pageContext.request.contextPath}/view?tab1=admin&tab2=users&action=print',
                '_blank' // <- This is what makes it open in a new window.
            );
        }
    </script>
</head>
<body>

<jsp:include page="../header.jsp"/>

<div class="mdl-grid">

    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>Manage Users</h5></div>

        <div class="tableContainer">
            <table class="mdl-data-table mdl-shadow--2dp">
                <thead>
                    <tr>
                        <th>
                            <label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect mdl-data-table__select" for="table-header">
                                <input type="checkbox" id="table-header" class="mdl-checkbox__input" />
                            </label>
                        </th>
                        <th>Object Id</th>
                        <th class="mdl-data-table__cell--non-numeric">Logon Id</th>
                        <th class="mdl-data-table__cell--non-numeric">Name</th>
                        <th>Enabled</th>
                        <th></th>
                    </tr>
                </thead>
                <c:forEach var="user" items="${users}" varStatus="loop">
                    <tr>
                        <td>
                            <label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect mdl-data-table__select" for="row[${loop.count}]">
                                <input type="checkbox" id="row[${loop.count}]" class="mdl-checkbox__input" />
                            </label>
                        </td>
                        <td><a href="${pageContext.request.contextPath}/view?tab1=main&tab2=profile&action=form&userId=${user.id}">${user.id}</a></td>
                        <td class="mdl-data-table__cell--non-numeric"><a href="${pageContext.request.contextPath}/view?tab1=admin&tab2=users&tab3=modify&action=form&userId=${user.id}">${user.logonId}</a></td>
                        <td class="mdl-data-table__cell--non-numeric">${user.name}</td>
                        <td class="mdl-data-table__cell--non-numeric">
                            <c:if test="${user.enabled}"><i class="material-icons" style="color: green;">check</i></c:if>
                            <c:if test="${!user.enabled}"><i class="material-icons" style="color: red;">clear</i></c:if>
                        </td>
                        <td><a onclick="deleteUser('${user.id}');" class="clickable material-icons">delete</a></td>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <div class="mdl-card__actions">
            <input id="addUserButton" type="button" value="Add User" class="mdl-button mdl-js-button mdl-button--raised" />
            <input id="printUserButton" type="button" value="Print Users" onclick="printUsers();" class="mdl-button mdl-js-button mdl-button--raised" style="padding-left:40px;background-image: url('${pageContext.request.contextPath}/images/mimetypes/Adobe.png'); background-position: left; background-repeat: no-repeat;"/>
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

<jsp:include page="../footer.jsp"/>
</body>
</html>