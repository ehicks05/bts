<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="projects" type="java.util.List<net.ehicks.bts.beans.Project>" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <title>BTS</title>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/webroot/inc_header.jsp"/>

    <script>
        function deleteProject(projectId)
        {
            if (confirm('Are you sure?'))
                location.href="${pageContext.request.contextPath}/view?tab1=admin&tab2=projects&action=delete&projectId=" + projectId;
        }
    </script>
</head>
<body>

<jsp:include page="${pageContext.request.contextPath}/WEB-INF/webroot/header.jsp"/>

<div class="mdl-grid">

    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>Manage Projects</h5></div>

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
                    <th class="mdl-data-table__cell--non-numeric">Name</th>
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
                        <td>${project.id}</td>
                        <td class="mdl-data-table__cell--non-numeric"><a href="${pageContext.request.contextPath}/view?tab1=admin&tab2=projects&tab3=modify&action=form&projectId=${project.id}">${project.name}</a></td>
                        <td class="mdl-data-table__cell--non-numeric">${project.prefix}</td>
                        <td><a onclick="deleteProject('${project.id}');" class="clickable material-icons">delete</a></td>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <div class="mdl-card__actions">
            <input id="addProjectButton" type="button" value="Add Project" class="mdl-button mdl-js-button mdl-button--raised" />
        </div>
    </div>
</div>

<dialog id="addProjectDialog" class="mdl-dialog">
    <h4 class="mdl-dialog__title">Add Project</h4>
    <div class="mdl-dialog__content">
        <form id="frmCreateProject" name="frmCreateProject" method="post" action="${pageContext.request.contextPath}/view?tab1=admin&tab2=projects&action=create">
            <table>
                <tr>
                    <td>Name:</td>
                    <td>
                        <input type="text" id="fldName" name="fldName" size="20" maxlength="256" value="" required/>
                    </td>
                </tr>
                <tr>
                    <td>Prefix:</td>
                    <td>
                        <input type="text" id="fldPrefix" name="fldPrefix" size="20" maxlength="256" value="" required/>
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
    var addProjectDialog = document.querySelector('#addProjectDialog');
    var addProjectButton = document.querySelector('#addProjectButton');
    if (!addProjectDialog.showModal)
    {
        dialogPolyfill.registerDialog(addProjectDialog);
    }
    addProjectButton.addEventListener('click', function ()
    {
        addProjectDialog.showModal();
    });
    document.querySelector('#addProjectDialog .create').addEventListener('click', function ()
    {
        if (!document.querySelector('#fldName').value || !document.querySelector('#fldPrefix').value)
            alert('Please enter a project name and prefix.');
        else
            $('#frmCreateProject').submit();
    });
    addProjectDialog.querySelector('#addProjectDialog .close').addEventListener('click', function ()
    {
        addProjectDialog.close();
    });
</script>

<jsp:include page="${pageContext.request.contextPath}/WEB-INF/webroot/footer.jsp"/>
</body>
</html>