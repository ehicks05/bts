<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${!empty sessionScope.userSession}">
    <jsp:include page="inc_footer.jsp"/>
</c:if>
<script>
    $(document).ready(showResponseMessage);
</script>