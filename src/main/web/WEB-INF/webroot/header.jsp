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

            <!-- start search form -->
            <%--<div class="mdl-textfield mdl-js-textfield mdl-textfield--expandable" style="padding-right: 20px;">--%>
                <%--<label class="mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab mdl-button--accent" for="search-expandable">--%>
                    <%--<i class="material-icons">search</i>--%>
                <%--</label>--%>
                <%--<div class="mdl-textfield__expandable-holder">--%>
                    <%--<input class="mdl-textfield__input" type="text" size="10" id="search-expandable" />--%>
                    <%--<label class="mdl-textfield__label" for="search-expandable">Search text</label>--%>
                <%--</div>--%>
            <%--</div>--%>
            <!-- end search form -->

            <c:if test="${!empty sessionScope.userSession}">
                <button class="mdl-button mdl-js-button mdl-js-ripple-effect mdl-button--fab mdl-button--mini-fab mdl-button--accent" onclick="showCreateDialog();">
                    <i class="material-icons">add</i>
                </button>
            </c:if>
        </div>
    </header>

    <c:if test="${!empty sessionScope.userSession}">
        <div class="mdl-layout__drawer">
            <span class="mdl-layout-title">BTS</span>
            <nav class="mdl-navigation">
                <a class="mdl-navigation__link" href="${pageContext.request.contextPath}/view?tab1=main&action=form">
                    <i class="material-icons">dashboard</i>
                    Dashboard
                </a>
                <a class="mdl-navigation__link" href="#">
                    <i class="material-icons">settings</i>
                    Settings
                </a>
                <a class="mdl-navigation__link" href="${pageContext.request.contextPath}/view?tab1=main&tab2=user&action=form&userId=${userSession.userId}">
                    <i class="material-icons">account_circle</i>
                    ${sessionScope.userSession.logonId}
                </a>
                <a class="mdl-navigation__link" href="${pageContext.request.contextPath}/view?tab1=main&action=logout">
                    <i class="material-icons">power_settings_new</i>
                    Logout
                </a>
            </nav>
        </div>
    </c:if>

<%--<table style="margin: 0 auto; width: 100%; display:none;" class="table">--%>
    <%--<tr>--%>
        <%--<td>--%>
            <%--<h1 style="margin: 0;padding: 0;">BTS</h1>--%>
        <%--</td>--%>

        <%--<c:if test="${!empty sessionScope.userSession}">--%>
            <%--<td>--%>
                <%--<button class="btn btn-default" onclick="goToDashboard()">--%>
                    <%--Dashboard--%>
                <%--</button>--%>
            <%--</td>--%>
            <%--<td>--%>
                <%--<button class="btn btn-primary" onclick="showCreateDialog();">--%>
                    <%--Create--%>
                <%--</button>--%>
            <%--</td>--%>

            <%--<td style="width: 100%"></td>--%>

            <%--<td style="text-align: right;white-space: nowrap">--%>
                <%--<form method="post" action="${pageContext.request.contextPath}/view?tab1=main&action=search">--%>
                    <%--<input type="text" size="12" maxlength="255" id="searchTerm" name="searchTerm" value="${issuesForm.title}"/>--%>
                    <%--<input type="hidden" id="header_title" name="title"/>--%>
                    <%--<input type="hidden" id="header_description" name="description"/>--%>
                    <%--&lt;%&ndash;<input type="submit" value="Search" class="btn btn-primary btn-xs" onclick="doSearch($('#searchTerm').val());"/>&ndash;%&gt;--%>
                    <%--<button class="btn btn-default" style="padding:5px 2px 3px 4px;" onclick="doSearch($('#searchTerm').val());">--%>
                        <%--<span style="font-size: 1.7em;vertical-align: middle;" class="material-icons">search</span>--%>
                    <%--</button>--%>
                <%--</form>--%>
            <%--</td>--%>
            <%--<td style="text-align: right">${sessionScope.userSession.logonId}</td>--%>
            <%--<td style="text-align: right">--%>
                <%--<button class="btn btn-default" style="padding:5px 3px 4px 4px;" onclick="location.href='${pageContext.request.contextPath}/view?tab1=main&action=logout';">--%>
                    <%--<span style="font-weight:normal;font-size: 1.7em;vertical-align: middle;" class="material-icons">settings</span>--%>
                <%--</button>--%>
            <%--</td>--%>
            <%--<td style="text-align: right">--%>
                <%--&lt;%&ndash;<input type="button" class="btn btn-primary btn-xs" value="logout" onclick="location.href='${pageContext.request.contextPath}/view?tab1=main&action=logout';"/>&ndash;%&gt;--%>
                <%--<button class="btn btn-default" style="padding:5px 3px 3px 4px;" onclick="location.href='${pageContext.request.contextPath}/view?tab1=main&action=logout';">--%>
                    <%--<span style="font-weight:normal;font-size: 1.7em;vertical-align: middle;" class="material-icons">power_settings_new</span>--%>
                <%--</button>--%>
            <%--</td>--%>
        <%--</c:if>--%>
    <%--</tr>--%>
<%--</table>--%>