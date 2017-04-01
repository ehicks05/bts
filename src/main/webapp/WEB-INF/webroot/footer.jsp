<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${!empty userSession}">
    <jsp:useBean id="userSession" type="net.ehicks.bts.UserSession" scope="session"/>
</c:if>

    <footer class="mdl-mini-footer">
        <div class="mdl-mini-footer__left-section">
            <div class="mdl-logo">
                <c:if test="${!empty sessionScope.userSession}">
                    server processed request in ${userSession.currentTimeMillis - userSession.enteredController} ms
                </c:if>
            </div>
        </div>
        <div class="mdl-mini-footer__right-section">
            <ul class="mdl-mini-footer__link-list">
                <li>
                    &copy;2017
                    <img style="height: 24px;" src="${pageContext.request.contextPath}/images/puffin.png">
                </li>
            </ul>
        </div>
    </footer>
</main>
<c:if test="${!empty sessionScope.userSession}">
    <jsp:include page="inc_footer.jsp"/>
</c:if>

<script>
    $(window).load(showResponseMessage);
</script>

<c:remove var="lastRequestDuration" scope="session" />