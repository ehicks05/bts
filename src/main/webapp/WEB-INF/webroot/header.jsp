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

            <c:if test="${!empty sessionScope.userSession}">
                <div style="width: 30px;"></div>
                <span style="font-size: 1em;" class="mdl-layout-title">${param.tab1}</span>
                <span style="font-size: 1em;" class="mdl-layout-title">.${param.tab2}</span>
                <span style="font-size: 1em;" class="mdl-layout-title"><c:if test="${!empty param.tab3}">.</c:if>${param.tab3}</span>
            </c:if>
            <div class="mdl-layout-spacer"></div>


            <c:if test="${!empty sessionScope.userSession}">
                <button id="show-create-issue-dialog" class="mdl-button mdl-js-button mdl-js-ripple-effect mdl-button--fab mdl-button--mini-fab mdl-button--accent">
                    <i class="material-icons">add</i>
                </button>
            </c:if>
        </div>
    </header>

    <c:if test="${!empty sessionScope.userSession}">
        <style>
            .selectedLink {color: white !important; background-color: #3f51b5}
            .selectedLink:hover {color: white !important; background-color: #3f51b5 !important;}
        </style>

        <div class="mdl-layout__drawer">
            <span class="mdl-layout-title">BTS</span>
            <nav class="mdl-navigation">
                <c:if test="${param.tab2 == 'dashboard'}">
                    <c:set var="statusClass" value="selectedLink"/>
                </c:if>
                <c:if test="${param.tab2 != 'dashboard'}">
                    <c:set var="statusClass" value=""/>
                </c:if>
                <a class="mdl-navigation__link ${statusClass}" href="${pageContext.request.contextPath}/view?tab1=main&tab2=dashboard&action=form">
                    <i class="material-icons" style="padding-right: 10px;">dashboard</i>Dashboard
                </a>
                <c:if test="${param.tab2 == 'search'}">
                    <c:set var="statusClass" value="selectedLink"/>
                </c:if>
                <c:if test="${param.tab2 != 'search'}">
                    <c:set var="statusClass" value=""/>
                </c:if>
                <a class="mdl-navigation__link ${statusClass}" href="${pageContext.request.contextPath}/view?tab1=main&tab2=search&action=form">
                    <i class="material-icons" style="padding-right: 10px;">search</i>Search
                </a>
                <c:if test="${param.tab2 == 'settings'}">
                    <c:set var="statusClass" value="selectedLink"/>
                </c:if>
                <c:if test="${param.tab2 != 'settings'}">
                    <c:set var="statusClass" value=""/>
                </c:if>
                <a class="mdl-navigation__link ${statusClass}" href="${pageContext.request.contextPath}/view?tab1=main&tab2=settings&action=form">
                    <i class="material-icons" style="padding-right: 10px;">settings</i>Settings
                </a>
                <c:if test="${param.tab1 == 'admin'}">
                    <c:set var="statusClass" value="selectedLink"/>
                </c:if>
                <c:if test="${param.tab1 != 'admin'}">
                    <c:set var="statusClass" value=""/>
                </c:if>
                <a class="mdl-navigation__link ${statusClass}" href="${pageContext.request.contextPath}/view?tab1=admin&tab2=overview&action=form">
                    <i class="material-icons" style="padding-right: 10px;">build</i>Admin
                </a>
                <c:if test="${param.tab2 == 'profile'}">
                    <c:set var="statusClass" value="selectedLink"/>
                </c:if>
                <c:if test="${param.tab2 != 'profile'}">
                    <c:set var="statusClass" value=""/>
                </c:if>
                <a class="mdl-navigation__link ${statusClass}" href="${pageContext.request.contextPath}/view?tab1=main&tab2=profile&action=form&userId=${userSession.userId}">
                    <i class="material-icons" style="padding-right: 10px;">account_circle</i>Profile
                </a>
                <a class="mdl-navigation__link" href="${pageContext.request.contextPath}/view?tab1=main&action=logout">
                    <i class="material-icons" style="padding-right: 10px;">power_settings_new</i>Logout
                </a>
            </nav>
        </div>
    </c:if>