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

    </script>
</head>
<body>

<jsp:include page="../header.jsp"/>

<section class="hero is-primary is-small">
    <div class="hero-body">
        <div class="container">
            <h1 class="title">
                Modify User ${user.username}
            </h1>
        </div>
    </div>
</section>

<section class="section">
    <div class="container">
        <div class="columns is-multiline is-centered">
            <div class="column">
                <form id="frmUser" method="post" action="${pageContext.request.contextPath}/admin/users/modify/modify?userId=${user.id}">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                    <t:text id="userId" label="User Id" value="${user.id}" isStatic="true" />
                    <t:text id="logonId" label="Logon Id" value="${user.username}" />
                    <t:text id="firstName" label="First Name" value="${user.firstName}" />
                    <t:text id="lastName" label="Last Name" value="${user.lastName}" />
                    <t:checkbox id="enabled" label="Enabled" checked="${user.enabled}" />

                    <div class="field is-horizontal">
                        <div class="field-label">
                            <label class="label">Groups</label>
                        </div>
                        <div class="field-body">
                            <div class="control is-expanded">
                                <div class="select is-multiple is-fullwidth">
                                    <t:multiSelect id="groupIds" items="${groups}" selectedValues="${user.groupIds}" placeHolder="None"/>
                                    <t:multiSelect id="projectIds" items="${projects}" selectedValues="${user.projectIds}" placeHolder="None"/>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="field is-horizontal">
                        <div class="field-label">
                            <label class="label">Projects</label>
                        </div>
                        <div class="field-body">
                            <div class="control is-expanded">
                                <div class="select is-multiple is-fullwidth">
                                    <t:multiSelect id="projects" items="${projects}" selectedValues="${userProjects}" placeHolder="None"/>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="field is-horizontal">
                        <div class="field-label">
                            <label class="label">Avatar</label>
                        </div>
                        <div class="field-body">
                            <div class="control is-expanded">
                                <div class="box button is-large" id="updateAvatarButton" title="${user.avatar.name}">
                                    <figure class="image is-32x32">
                                        <img src="${pageContext.request.contextPath}/avatar/${user.avatar.id}"/>
                                    </figure>
                                </div>
                            </div>
                        </div>
                    </div>

                    <input id="saveUserButton" type="submit" value="Save" class="button is-primary" />
                    <input id="changePasswordButton" type="button" value="Change Password" class="button" />
                </form>
            </div>
        </div>
    </div>
</section>

<div class="modal" id="changePasswordDialog">
    <div class="modal-background"></div>
    <div class="modal-card">
        <header class="modal-card-head">
            <p class="modal-card-title">Change Password</p>
            <button class="delete" aria-label="close"></button>
        </header>
        <section class="modal-card-body">
            <form id="frmChangePassword" name="frmChangePassword" method="post" action="${pageContext.request.contextPath}/admin/users/changePassword?userId=${user.id}">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <t:text id="password" label="New Password" required="true"/>
            </form>
        </section>

        <footer class="modal-card-foot">
            <button type="button" class="button is-primary change">Change</button>
            <button type="button" class="button close">Cancel</button>
        </footer>
    </div>
</div>

<script>
    initDialog('changePassword');

    $(function () {
        document.querySelector('#changePasswordDialog .change').addEventListener('click', function ()
        {
            if (!document.querySelector('#password').value)
                alert('Please enter a password.');
            else
                $('#frmChangePassword').submit();
        });
    });
</script>

<div class="modal" id="updateAvatarDialog">
    <div class="modal-background"></div>
    <div class="modal-card">
        <header class="modal-card-head">
            <p class="modal-card-title">Update Avatar</p>
            <button class="delete" aria-label="close"></button>
        </header>
        <section class="modal-card-body">
            <h5 class="subtitle is-5">Current Avatar:</h5>
            <div class="columns is-multiline is-centered is-gapless">
                <div class="column is-one-fifth has-text-centered" style="border: 6px solid black; border-radius: 5px;">
                    <div class="box has-text-centered" title="${user.avatar.name}">
                        <figure class="image is-64x64 has-text-centered">
                            <img src="${pageContext.request.contextPath}/avatar/${user.avatar.id}"/>
                        </figure>
                    </div>
                </div>
            </div>
            <hr>
            <h5 class="subtitle is-5">Choose Existing:</h5>
            <form id="frmUpdateAvatar" name="frmUpdateAvatar" method="post" action="${pageContext.request.contextPath}/admin/users/modify/updateAvatar?userId=${user.id}">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input type="hidden" id="fldAvatarId" name="fldAvatarId" value="${user.avatar.id}"/>
                <div class="columns is-multiline is-centered is-gapless">
                    <c:forEach var="avatar" items="${publicAvatars}">
                        <c:set var="borderColor" value="${user.avatar.id eq avatar.id ? '#4488FF' : '#FFF'}" />
                        <div id="avatar${avatar.id}" class="column is-one-fifth has-text-centered"
                             style="border: 6px solid ${borderColor}; border-radius: 5px;" onclick="selectAvatar(this.id, '${avatar.id}');">
                            <div class="box has-text-centered" title="${avatar.name}">
                                <figure class="image is-64x64 has-text-centered">
                                    <img src="${pageContext.request.contextPath}/avatar/${avatar.id}"/>
                                </figure>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <input type="submit" value="Update" class="button is-primary save" />
            </form>

            <script>
                function selectAvatar(selectedElementId, avatarId)
                {
                    var previousValue = $('#fldAvatarId').val();
                    $('#avatar' + previousValue).css('border-color', '#FFF');

                    $('#fldAvatarId').val(avatarId);
                    $('#' + selectedElementId).css('border-color', '#4488FF');
                }
            </script>
            <hr>
            <h5 class="subtitle is-5">Upload Avatar:</h5>
            <form id="frmUploadAvatar" name="frmUploadAvatar" enctype="multipart/form-data"
                  method="post" action="${pageContext.request.contextPath}/admin/users/modify/uploadAvatar?userId=${user.id}&${_csrf.parameterName}=${_csrf.token}">
                <div class="field has-addons">

                    <div class="file has-name is-fullwidth">
                        <label class="file-label">
                            <input class="file-input" type="file" id="file" name="file" required>
                            <span class="file-cta">
                                <span class="file-icon">
                                    <i class="fas fa-upload"></i>
                                </span>
                                <span class="file-label">
                                    Choose a file…
                                </span>
                            </span>
                            <span id="uploadAvatarFilename" class="file-name">
                                No File Selected
                            </span>
                        </label>
                    </div>

                    <input type="submit" value="Upload" class="button is-primary save" />
                </div>
            </form>
        </section>

        <footer class="modal-card-foot">
            <button class="button close">Cancel</button>
        </footer>
    </div>
</div>

<script>
    $(function () {
        var file = document.getElementById("fldFile");
        file.onchange = function() {
            if (file.files.length > 0)
            {
                document.getElementById('uploadAvatarFilename').innerHTML = file.files[0].name;
            }
        };
    });

    initDialog('updateAvatar');
</script>

<jsp:include page="../footer.jsp"/>
</body>
</html>