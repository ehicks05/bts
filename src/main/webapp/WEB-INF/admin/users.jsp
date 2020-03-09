<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

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

<div id="addUserDialog" class="modal">
    <div class="modal-background"></div>
    <div class="modal-card">
        <header class="modal-card-head">
            <p class="modal-card-title">Add User</p>
            <button class="delete close" aria-label="close"></button>
        </header>
        <section class="modal-card-body">
            <div>
                <form id="frmCreateUser" name="frmCreateUser" method="post" action="${pageContext.request.contextPath}/admin/users/create">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <table>
                        <t:text id="username" label="Username" horizontal="true" value="" required="true" isSpring="false" isStatic="false" />
                        <t:text id="password" label="Password" horizontal="true" value="" required="true" isSpring="false" isStatic="false" />
                        <t:text id="firstName" label="First Name" horizontal="true" value="" required="true" isSpring="false" isStatic="false" />
                        <t:text id="lastName" label="Last Name" horizontal="true" value="" required="true" isSpring="false" isStatic="false" />
                    </table>
                </form>
            </div>
        </section>
        <footer class="modal-card-foot">
            <button class="button is-success create">Create</button>
            <button class="button close">Cancel</button>
        </footer>
    </div>
</div>
<script>
    const dialog = document.querySelector('#addUserDialog');
    const button = document.querySelector('#addUserButton');

    button.addEventListener('click', toggleDialog);
    document.querySelector('#addUserDialog .create').addEventListener('click', function ()
    {
        if (!document.querySelector('#username').value)
            alert('Please enter a username.');
        else
            $('#frmCreateUser').submit();
    });
    dialog.querySelectorAll('#addUserDialog .close').forEach(el => el.addEventListener('click', toggleDialog));

    function toggleDialog() {
        document.querySelector('#addUserDialog').classList.toggle('is-active');
    }
</script>

<jsp:include page="../footer.jsp"/>
</body>
</html>