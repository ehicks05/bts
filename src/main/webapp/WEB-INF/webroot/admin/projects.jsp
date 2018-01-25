<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="projects" type="java.util.List<net.ehicks.bts.beans.Project>" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../inc_title.jsp"/>
    <jsp:include page="../inc_header.jsp"/>

    <script>
        function deleteProject(projectId)
        {
            if (confirm('Are you sure?'))
                location.href="${pageContext.request.contextPath}/view?tab1=admin&tab2=projects&action=delete&projectId=" + projectId;
        }
    </script>
</head>
<body>

<jsp:include page="../header.jsp"/>

<section class="hero is-primary is-small">
    <div class="hero-body">
        <div class="container">
            <h1 class="title">
                Manage Projects
            </h1>
        </div>
    </div>
</section>

<section class="section">
    <div class="container">
        <div class="columns is-multiline is-centered">
            <div class="column">
                <table class="table is-striped is-narrow is-hoverable is-fullwidth">
                    <thead>
                    <tr>
                        <th>
                            <label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect mdl-data-table__select" for="table-header">
                                <input type="checkbox" id="table-header" class="mdl-checkbox__input" />
                            </label>
                        </th>
                        <th class="has-text-right">Object Id</th>
                        <th>Name</th>
                        <th>Prefix</th>
                        <th></th>
                    </tr>
                    </thead>
                    <c:forEach var="project" items="${projects}" varStatus="loop">
                        <tr>
                            <td>
                                <label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect mdl-data-table__select" for="row[${loop.count}]">
                                    <input type="checkbox" id="row[${loop.count}]" class="mdl-checkbox__input" />
                                </label>
                            </td>
                            <td class="has-text-right">${project.id}</td>
                            <td><a href="${pageContext.request.contextPath}/view?tab1=admin&tab2=projects&tab3=modify&action=form&projectId=${project.id}">${project.name}</a></td>
                            <td>${project.prefix}</td>
                            <td class="has-text-centered"><a onclick="deleteProject('${project.id}');" ><i class="fas fa-trash"></i></a></td>
                        </tr>
                    </c:forEach>
                </table>

                <input id="createProjectButton" type="button" value="Create Project" class="button is-primary" />
            </div>
        </div>
    </div>
</section>

<div id="createProjectDialog" class="modal">
    <div class="modal-background"></div>
    <div class="modal-card">
        <header class="modal-card-head">
            <p class="modal-card-title">Create Project</p>
            <button class="delete" aria-label="close"></button>
        </header>
        <section class="modal-card-body">
            <form id="frmCreateProject" name="frmCreateProject" method="post" action="${pageContext.request.contextPath}/view?tab1=admin&tab2=projects&action=create">
                <t:text id="fldName" value="" label="Name" required="true"/>
                <t:text id="fldPrefix" value="" label="Prefix" required="true"/>
            </form>
        </section>
        <footer class="modal-card-foot">
            <button class="button is-success create">Create</button>
            <button class="button close">Cancel</button>
        </footer>
    </div>
</div>

<script>
    initDialog('createProject');

    document.querySelector('#createProjectDialog .create').addEventListener('click', function ()
    {
        if (!document.querySelector('#fldName').value || !document.querySelector('#fldPrefix').value)
            alert('Please enter a project name and prefix.');
        else
            $('#frmCreateProject').submit();
    });
</script>

<jsp:include page="../footer.jsp"/>
</body>
</html>