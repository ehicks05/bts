<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="groups" type="java.util.List<net.ehicks.bts.beans.Group>" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../inc_title.jsp"/>
    <jsp:include page="../inc_header.jsp"/>

    <script>
        function deleteGroup(groupId)
        {
            if (confirm('Are you sure?'))
                location.href="${pageContext.request.contextPath}/admin/groups/delete?groupId=" + groupId;
        }
    </script>
</head>
<body>

<jsp:include page="../header.jsp"/>

<section class="hero is-primary is-small">
    <div class="hero-body">
        <div class="container">
            <h1 class="title">
                Manage Groups
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
                        <th>Name</th>
                        <th></th>
                    </tr>
                    </thead>
                    <c:forEach var="group" items="${groups}" varStatus="loop">
                        <tr>
                            <td><a href="${pageContext.request.contextPath}/admin/groups/modify/form?groupId=${group.id}">${group.name}</a></td>
                            <td><a onclick="deleteGroup('${group.id}');"><i class="fas fa-trash"></i></a></td>
                        </tr>
                    </c:forEach>
                </table>

                <input id="createGroupButton" type="button" value="Create Group" class="button is-primary" />
            </div>
        </div>
    </div>
</section>

<div id="createGroupDialog" class="modal">
    <div class="modal-background"></div>
    <div class="modal-card">
        <header class="modal-card-head">
            <p class="modal-card-title">Create Group</p>
            <button class="delete" aria-label="close"></button>
        </header>
        <section class="modal-card-body">
            <form id="frmCreateGroup" name="frmCreateGroup" method="post" action="${pageContext.request.contextPath}/admin/groups/create">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <t:text id="fldName" value="" label="Name" required="true"/>
            </form>
        </section>
        <footer class="modal-card-foot">
            <button class="button is-success create">Create</button>
            <button class="button close">Cancel</button>
        </footer>
    </div>
</div>

<script>
    initDialog('createGroup');

    document.querySelector('#createGroupDialog .create').addEventListener('click', function ()
    {
        if (!document.querySelector('#fldName').value)
            alert('Please enter a group name.');
        else
            $('#frmCreateGroup').submit();
    });
</script>

<jsp:include page="../footer.jsp"/>
</body>
</html>