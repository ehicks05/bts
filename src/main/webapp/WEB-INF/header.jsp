<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
    function goToIssue()
    {
        var issueId = document.getElementById('fldGoToIssue').value;
        if (issueId)
            location.href = '${pageContext.request.contextPath}/issue/form?issueId=' + issueId;
    }

    let timeout;
    function displayNotification(status, message)
    {
        const notification = document.querySelector('#update-notification');
        notification.innerHTML = '<button class="delete"></button>' + message;

        if (status === 'success')
            notification.className = 'notification is-success';
        else if (status === 'error')
            notification.className = 'notification is-danger';
        else
            notification.className = 'notification is-info';

        clearTimeout(timeout);
        timeout = setTimeout(function () {
            notification.className = 'notification is-hidden';
        }, 10000);
    }

    function checkForResponseMessage()
    {
        var message;
        <c:if test="${!empty requestScope.responseMessage}">
            message = '${requestScope.responseMessage}';
        </c:if>

        if (message)
        {
            displayNotification('ok', message);
        }
    }

    $(checkForResponseMessage);
    $(function () {
        $('#fldGoToIssue').on('keypress', function (e) {
            document.getElementById('goToIssueButton').disabled = false;
            if (e.keyCode === 13)
            {
                goToIssue();
            }
        });

        var settingsSubscreens = [
            ["/settings/savedSearches/form", "search", "Saved Searches", "savedSearches"],
            ["/settings/subscriptions/form", "envelope", "Subscriptions", "subscriptions"]
        ];
        var container = $('#settingsSubscreensContainer');
        settingsSubscreens.forEach(subscreen => {
            const statusClass = '';
            const aClass = "navbar-item " + statusClass;
            const href = subscreen[0];
            container.append(
            "<a class='" + aClass + "' href='" + href + "'>" +
                "<span class='icon is-medium has-text-info'>" +
                    "<i class='fas fa-lg fa-" + subscreen[1] + "'></i>" +
                "</span>" + subscreen[2] +
            "</a>"
            );
        });

        var adminSubscreens = [
            ["/admin/system/modify/form", "server", "Manage System", "system"],
            ["/admin/users/form", "user", "Manage Users", "users"],
            ["/admin/groups/form", "users", "Manage Groups", "groups"],
            ["/admin/projects/form", "folder", "Manage Projects", "projects"],
            ["/admin/email/form", "envelope", "Manage Email", "email"],
            ["/admin/backups/form", "cloud-upload-alt", "Backups", "backups"],
            ["/admin/system/info/form", "chart-bar", "System Info", "system"],
            ["/admin/dbInfo/form", "chart-bar", "Database Info", "dbInfo"],
            ["/admin/audit/form", "history", "Audit Records", "audit"]
        ];
        container = $('#adminSubscreensContainer');
        adminSubscreens.forEach(subscreen => {
            const statusClass = '';
            const aClass = "navbar-item " + statusClass;
            const href = subscreen[0];
            container.append(
                "<a class='" + aClass + "' href='" + href + "'>" +
                "<span class='icon is-medium has-text-info'>" +
                "<i class='fas fa-lg fa-" + subscreen[1] + "'></i>" +
                "</span>" + subscreen[2] +
                "</a>"
            );
        });
    });

    document.addEventListener('DOMContentLoaded', function () {

        // Get all "navbar-burger" elements
        var $navbarBurgers = Array.prototype.slice.call(document.querySelectorAll('.navbar-burger'), 0);

        // Check if there are any navbar burgers
        if ($navbarBurgers.length > 0) {

            // Add a click event on each of them
            $navbarBurgers.forEach(function ($el) {
                $el.addEventListener('click', function () {

                    // Get the target from the "data-target" attribute
                    var target = $el.dataset.target;
                    var $target = document.getElementById(target);

                    // Toggle the class on both the "navbar-burger" and the "navbar-menu"
                    $el.classList.toggle('is-active');
                    $target.classList.toggle('is-active');

                });
            });
        }

    });

</script>

<nav class="navbar" role="navigation" aria-label="main navigation">
    <div class="container">
        <div class="navbar-brand">
            <div class="navbar-item">
                <img src="${pageContext.request.contextPath}/images/puffin-text.png" alt="Puffin" class="invertable"/>
            </div>

            <button class="button navbar-burger" data-target="navMenu">
                <span></span>
                <span></span>
                <span></span>
            </button>
        </div>

        <div class="navbar-menu" id="navMenu">
            <div class="navbar-start">
                <div class="navbar-item">
                    <a class="button" id="createIssueButton">
                        <span class="icon has-text-primary">
                          <i class="fas fa-plus-square"></i>
                        </span>
                        <span>Create Issue</span>
                    </a>
                </div>
                <div class="navbar-item">
                    <div class="field has-addons">
                        <div class="control">
                            <input id="fldGoToIssue" type="text" class="input" size="3" maxlength="16" />
                        </div>
                        <div class="control">
                            <button id="goToIssueButton" class="button has-text-primary" onclick="goToIssue();" disabled>
                                <span class="icon">
                                    <i class="fas fa-search"></i>
                                </span>
                            </button>
                        </div>
                    </div>
                </div>
                <c:set var="statusClass" value="${param.tab1 == 'dashboard' ? 'is-active' : ''}"/>
                <a class="navbar-item ${statusClass}" href="${pageContext.request.contextPath}/dashboard">
                    Dashboard
                </a>
                <c:set var="statusClass" value="${param.tab1 == 'search' ? 'is-active' : ''}"/>
                <a class="navbar-item ${statusClass}" href="${pageContext.request.contextPath}/search/form">
                    Search
                </a>
                <c:set var="statusClass" value="${param.tab1 == 'settings' ? 'is-active' : ''}"/>
                <div class="navbar-item has-dropdown is-hoverable">
                    <a class="navbar-link ${statusClass}" href="${pageContext.request.contextPath}/settings/form">
                        Settings
                    </a>
                    <div class="navbar-dropdown" id="settingsSubscreensContainer">

                    </div>
                </div>
                <c:set var="statusClass" value="${param.tab1 == 'admin' ? 'is-active' : ''}"/>
                <c:if test="${pageContext.request.userPrincipal.principal.admin}">
                    <div class="navbar-item has-dropdown is-hoverable">
                        <a class="navbar-link ${statusClass}" href="${pageContext.request.contextPath}/admin/form">
                            Admin
                        </a>
                        <div class="navbar-dropdown" id="adminSubscreensContainer">

                        </div>
                    </div>
                </c:if>
                <c:set var="statusClass" value="${param.tab1 == 'profile' ? 'is-active' : ''}"/>
                <a class="navbar-item ${statusClass}" href="${pageContext.request.contextPath}/profile/form?profileUserId=${pageContext.request.userPrincipal.principal.id}">
                    Profile
                </a>
            </div>
            <div class="navbar-end">
                <a class="navbar-item" onclick="document.getElementById('logoutForm').submit();">
                    <form id="logoutForm" action="${pageContext.request.contextPath}/logout" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    </form>
                    <span class="icon is-medium has-text-info">
                        <i class="fas fa-lg fa-sign-out-alt"></i>
                    </span>
                </a>
            </div>
        </div>
    </div>
</nav>

<div id="update-notification" class="notification is-hidden" style="position: fixed; bottom: 0; right: 1rem; z-index: 10;">
    <button class="delete"></button>
</div>