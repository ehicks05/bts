<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${!empty userSession}">
    <jsp:useBean id="userSession" type="net.ehicks.bts.UserSession" scope="session"/>
</c:if>

<script>
    function goToIssue()
    {
        var issueId = document.getElementById('goToIssue').value;
        if (issueId)
            location.href = '${pageContext.request.contextPath}/view?tab1=main&tab2=issue&action=form&issueId=' + issueId;
    }

    function followBreadcrumbs(tab1, tab2, tab3)
    {
        var tabs = '';
        if (tab1) tabs += 'tab1=' + tab1;
        if (tab2) tabs += '&tab2=' + tab2;
        if (tab3) tabs += '&tab3=' + tab3;
        location.href = '${pageContext.request.contextPath}/view?' + tabs + '&action=form';
    }

    function showResponseMessage()
    {
        var message;
        <c:if test="${!empty sessionScope.responseMessage}">
            message = '${sessionScope.responseMessage}';
        </c:if>
        <c:if test="${!empty requestScope.responseMessage}">
            message = '${requestScope.responseMessage}';
        </c:if>
        if (message)
        {
            alert(message);
        }
    }


</script>

<div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
    <header class="mdl-layout__header">
        <div class="mdl-layout__header-row">
            <%--<span class="mdl-layout-title">BTS</span>--%>

            <c:if test="${!empty sessionScope.userSession}">
                <span style="font-size: 1em;" class="mdl-layout-title clickable" onclick="followBreadcrumbs('${param.tab1}')">${param.tab1}</span>
                <c:if test="${!empty param.tab2}">
                    <i class="material-icons">navigate_next</i><span style="font-size: 1em;" class="mdl-layout-title clickable" onclick="followBreadcrumbs('${param.tab1}','${param.tab2}')">${param.tab2}</span>
                </c:if>
                <c:if test="${!empty param.tab3}">
                    <i class="material-icons">navigate_next</i><span style="font-size: 1em;" class="mdl-layout-title clickable" onclick="followBreadcrumbs('${param.tab1}','${param.tab2}','${param.tab3}')">${param.tab3}</span>
                </c:if>

                <input id="goToIssue" type="text" size="1" maxlength="32" style="margin-left: 30px;font-size:18px;" onkeypress="document.getElementById('goToIssueButton').disabled = false;" onclick="document.getElementById('goToIssueButton').disabled = false;"/>
                <button id="goToIssueButton" class="mdl-button mdl-js-button mdl-button--icon" onclick="goToIssue();">
                    <i class="material-icons">search</i>
                </button>

                <div class="mdl-layout-spacer"></div>

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
            <span class="mdl-layout-title"><img style="height: 24px;padding-right: 10px;" src="${pageContext.request.contextPath}/images/bug_16.png"><span style="color:black;">BTS</span></span>
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
                <c:if test="${param.tab1 == 'settings'}">
                    <c:set var="statusClass" value="selectedLink"/>
                </c:if>
                <c:if test="${param.tab1 != 'settings'}">
                    <c:set var="statusClass" value=""/>
                </c:if>
                <a class="mdl-navigation__link ${statusClass}" href="${pageContext.request.contextPath}/view?tab1=settings&action=form">
                    <i class="material-icons" style="padding-right: 10px;">settings</i>Settings
                </a>
                <c:if test="${param.tab1 == 'admin'}">
                    <c:set var="statusClass" value="selectedLink"/>
                </c:if>
                <c:if test="${param.tab1 != 'admin'}">
                    <c:set var="statusClass" value=""/>
                </c:if>

                <c:if test="${userSession.user.admin}">
                    <a class="mdl-navigation__link ${statusClass}" href="${pageContext.request.contextPath}/view?tab1=admin&tab2=overview&action=form">
                        <i class="material-icons" style="padding-right: 10px;">build</i>Admin
                    </a>
                </c:if>

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

    <c:remove var="responseMessage" scope="session" />