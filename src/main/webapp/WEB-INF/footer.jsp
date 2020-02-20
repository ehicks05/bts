<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:eval expression="@gitVersionProperties.version" var="version" />
<spring:eval expression="@gitVersionProperties.revision" var="revision" />
<spring:eval expression="@environment.getProperty('puffin.siteName')" var="siteName" />

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
                    version
                    <a href="https://github.com/ehicks05/puffin/commit/${revision}" title="${revision}">${version}</a>
                    by <a href="https://ehicks.net">Eric Hicks</a>
                </p>
                <p>
                    <c:if test="${!empty sessionScope.userSession}">
                        <span title="Time from entering the controller to rendering this element.">${userSession.currentTimeMillis - userSession.enteredController} ms</span>
                    </c:if>
                </p>
            </div>
        </div>
    </div>
</footer>

<%--<c:if test="${!empty sessionScope.userSession}">--%>
    <jsp:include page="inc_createIssueDialog.jsp"/>
<%--</c:if>--%>

<c:remove var="lastRequestDuration" scope="session" />