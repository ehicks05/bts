<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${!empty userSession}">
    <jsp:useBean id="userSession" type="com.hicks.UserSession" scope="session"/>
</c:if>

<script>
    function doSearch(searchTerm)
    {
        $('#header_title').val(searchTerm);
        $('#header_description').val(searchTerm);
    }

    function goToDashboard()
    {
        location.href='${pageContext.request.contextPath}/view?tab1=main&action=form';
    }
</script>

<div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
    <header class="mdl-layout__header">
        <div class="mdl-layout__header-row">
            <span class="mdl-layout-title">BTS</span>
            <div class="mdl-layout-spacer"></div>

            <c:if test="${!empty sessionScope.userSession}">
                <button id="show-create-issue-dialog" class="mdl-button mdl-js-button mdl-js-ripple-effect mdl-button--fab mdl-button--mini-fab mdl-button--accent" onclick="showCreateDialog();">
                    <i class="material-icons">add</i>
                </button>
            </c:if>
        </div>
    </header>

    <c:if test="${!empty sessionScope.userSession}">
        <div class="mdl-layout__drawer">
            <span class="mdl-layout-title">BTS</span>
            <nav class="mdl-navigation">
                <a class="mdl-navigation__link" href="${pageContext.request.contextPath}/view?tab1=main&tab2=dashboard&action=form">
                    <i class="material-icons" style="padding-right: 10px;">dashboard</i>Dashboard
                </a>
                <a class="mdl-navigation__link" href="${pageContext.request.contextPath}/view?tab1=main&action=form">
                    <i class="material-icons" style="padding-right: 10px;">search</i>Search
                </a>
                <a class="mdl-navigation__link" href="${pageContext.request.contextPath}/view?tab1=main&tab2=settings&action=form&userId=${userSession.userId}">
                    <i class="material-icons" style="padding-right: 10px;">settings</i>Settings
                </a>
                <a class="mdl-navigation__link" href="${pageContext.request.contextPath}/view?tab1=main&tab2=user&action=form&userId=${userSession.userId}">
                    <i class="material-icons" style="padding-right: 10px;">account_circle</i>Profile
                </a>
                <a class="mdl-navigation__link" href="${pageContext.request.contextPath}/view?tab1=main&action=logout">
                    <i class="material-icons" style="padding-right: 10px;">power_settings_new</i>Logout
                </a>
            </nav>
        </div>
    </c:if>