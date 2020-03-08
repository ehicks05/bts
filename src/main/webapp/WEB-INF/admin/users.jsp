<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../inc_title.jsp"/>
    <jsp:include page="../inc_header.jsp"/>

    <script>
        function deleteUser(userId)
        {
            if (confirm('Are you sure?'))
                location.href="${pageContext.request.contextPath}/admin/users/delete?userId=" + userId;
        }

        function printUsers()
        {
            window.open(
                '${pageContext.request.contextPath}/admin/users/print',
                '_blank' // <- This is what makes it open in a new window.
            );
        }
    </script>
</head>
<body>

<jsp:include page="../header.jsp"/>

<section class="hero is-primary is-small">
    <div class="hero-body">
        <div class="container">
            <h1 class="title">
                Manage Users
            </h1>
        </div>
    </div>
</section>

<section class="section">
    <div class="container">
        <div class="columns is-multiline is-centered">
            <div class="column is-narrow">
                <table class="table is-striped is-narrow is-hoverable is-fullwidth">
                    <thead>
                    <tr>
                        <th>Logon Id</th>
                        <th>Name</th>
                        <th class="has-text-centered">Enabled</th>
                        <th></th>
                    </tr>
                    </thead>
                    <c:forEach var="user" items="${users}" varStatus="loop">
                        <tr>
                            <td><a href="${pageContext.request.contextPath}/admin/users/modify/form?userId=${user.id}">${user.username}</a></td>
                            <td>${user.fullName}</td>
                            <td class="has-text-centered">
                                <c:if test="${user.enabled}"><i class="fas fa-check has-text-success" ></i></c:if>
                                <c:if test="${!user.enabled}"><i class="fas fa-ban has-text-danger" ></i></c:if>
                            </td>
                            <td class="has-text-centered"><a onclick="deleteUser('${user.id}');"><i class="fas fa-trash"></i></a></td>
                        </tr>
                    </c:forEach>
                </table>

                <a class="button is-primary" id="addUserButton">
                    <span class="icon">
                        <i class="fas fa-plus"></i>
                    </span>
                    <span>Add User</span>
                </a>
                <a class="button" onclick="printUsers();">
                    <span class="icon">
                        <i class="fas fa-print"></i>
                    </span>
                    <span>Print Users</span>
                </a>
            </div>
        </div>
    </div>
</section>

<dialog id="addUserDialog" class="mdl-dialog">
    <h4 class="mdl-dialog__title">Add User</h4>
    <div class="mdl-dialog__content">
        <form id="frmCreateUser" name="frmCreateUser" method="post" action="${pageContext.request.contextPath}/admin/users/create">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <table>
                <tr>
                    <td>Username:</td>
                    <td>
                        <input type="text" id="username" name="username" size="20" maxlength="256" value="" required/>
                    </td>
                    <td>Password:</td>
                    <td>
                        <input type="password" id="password" name="password" size="20" maxlength="256" value="" required/>
                    </td>
                    <td>First Name:</td>
                    <td>
                        <input type="text" id="firstName" name="firstName" size="20" maxlength="256" value="" required/>
                    </td>
                    <td>Last Name:</td>
                    <td>
                        <input type="text" id="lastName" name="lastName" size="20" maxlength="256" value="" required/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div class="mdl-dialog__actions">
        <button type="button" class="button is-primary create">Create</button>
        <button type="button" class="button close">Cancel</button>
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
        if (!document.querySelector('#username').value)
            alert('Please enter a username.');
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