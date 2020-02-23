<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:eval expression="@gitVersionProperties.version" var="version" />
<spring:eval expression="@gitVersionProperties.revision" var="revision" />
<spring:eval expression="@environment.getProperty('puffin.siteName')" var="siteName" />
<jsp:useBean id="now" class="java.util.Date" />

<footer class="footer">
    <div class="container">
        <div class="columns">
            <div class="column">
                <p><img src="${pageContext.request.contextPath}/images/bug_16.png"/>
                    &centerdot; <strong>${siteName}</strong>
                </p>
            </div>
            <div class="column has-text-right">
                <p>
                    <a href="https://github.com/ehicks05/puffin">Puffin</a>
                    <a href="https://github.com/ehicks05/puffin/commit/${revision}" title="${revision}">v${version}</a>
                    by <a href="https://ehicks.net">Eric Hicks</a>
                </p>
            </div>
        </div>
    </div>
</footer>

<%--<c:if test="${!empty sessionScope.userSession}">--%>
    <jsp:include page="inc_createIssueDialog.jsp"/>
<%--</c:if>--%>

<c:remove var="lastRequestDuration" scope="session" />