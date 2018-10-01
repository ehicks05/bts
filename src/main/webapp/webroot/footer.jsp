<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<c:if test="${!empty userSession}">
    <jsp:useBean id="userSession" type="net.ehicks.bts.UserSession" scope="session"/>
</c:if>

<%--<nav class="level">--%>
    <%--<div class="container">--%>
    <%--<hr>--%>
        <%--<div class="level-item has-text-centered">--%>
            <%--<nav class="breadcrumb is-centered" aria-label="breadcrumbs">--%>
                <%--<ul>--%>
                    <%--<li><a href="#" onclick="followBreadcrumbs('${param.tab1}')">${param.tab1}</a></li>--%>
                    <%--<c:if test="${!empty param.tab2}">--%>
                        <%--<li><a href="#" onclick="followBreadcrumbs('${param.tab1}','${param.tab2}')">${param.tab2}</a></li>--%>
                    <%--</c:if>--%>
                    <%--<c:if test="${!empty param.tab3}">--%>
                        <%--<li><a href="#" onclick="followBreadcrumbs('${param.tab1}','${param.tab2}','${param.tab3}')">${param.tab3}</a></li>--%>
                    <%--</c:if>--%>
                <%--</ul>--%>
            <%--</nav>--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</nav>--%>
<footer class="footer">
    <div class="container">
        <div class="content has-text-centered">
            <p><img src="${pageContext.request.contextPath}/images/bug_16.png"/>
                &centerdot; <strong>${btsSystem.instanceName}</strong>
            </p>
            <p>
                <strong>Puffin build <span title="${userSession.systemInfo.gitVersion}">${userSession.systemInfo.version}</span></strong> by <a href="http://ehicks.net">Eric Hicks</a>
            </p>
            <p>
                <c:if test="${!empty sessionScope.userSession}">
                    Page rendered in ${userSession.currentTimeMillis - userSession.enteredController} ms
                </c:if>
            </p>
        </div>
    </div>
</footer>

<c:if test="${!empty sessionScope.userSession}">
    <jsp:include page="inc_createIssueDialog.jsp"/>
</c:if>

<c:remove var="lastRequestDuration" scope="session" />