<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<c:if test="${!empty userSession}">
    <jsp:useBean id="userSession" type="net.ehicks.bts.UserSession" scope="session"/>
</c:if>

<footer class="footer">
    <div class="container">
        <div class="columns">
            <div class="column">
                <p><img src="${pageContext.request.contextPath}/images/bug_16.png"/>
                    &centerdot; <strong>${btsSystem.instanceName}</strong>
                </p>
            </div>
            <div class="column has-text-right">
                <p>
                    Puffin build <span title="${userSession.systemInfo.gitVersion}">${userSession.systemInfo.version}</span> by <a href="https://ehicks.net">Eric Hicks</a>
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

<c:if test="${!empty sessionScope.userSession}">
    <jsp:include page="inc_createIssueDialog.jsp"/>
</c:if>

<c:remove var="lastRequestDuration" scope="session" />