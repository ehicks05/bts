<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

</main>
<c:if test="${!empty sessionScope.userSession}">
    <jsp:include page="inc_footer.jsp"/>
</c:if>

<footer class="mdl-mini-footer">
    <div class="mdl-mini-footer__left-section">
        <div class="mdl-logo">
            <c:if test="${!empty sessionScope.userSession}">
                server processed request in ${sessionScope.lastRequestDuration} ms
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

<script>
    $(document).ready(showResponseMessage);
</script>

<c:remove var="lastRequestDuration" scope="session" />