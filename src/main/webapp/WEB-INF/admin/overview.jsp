<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../inc_title.jsp"/>
    <jsp:include page="../inc_header.jsp"/>
</head>
<body>

<jsp:include page="../header.jsp"/>

<section class="hero is-primary is-small">
    <div class="hero-body">
        <div class="container">
            <h1 class="title">
                Administration
            </h1>
        </div>
    </div>
</section>

<section class="section">
    <div class="container">

        <style>.tags:hover{filter: brightness(110%);}</style>
        <div class="field is-grouped is-grouped-multiline">
            <c:forEach var="adminSubscreen" items="${adminSubscreens}">
                <div class="control">
                    <a href="${adminSubscreen[0]}">
                        <div class="tags are-large has-addons">
                            <span class="tag is-primary">
                                <span class="icon">
                                    <i class="fas fa-${adminSubscreen[1]}"></i>
                                </span>
                            </span>
                            <span class="tag">
                                <span>${adminSubscreen[2]}</span>
                            </span>
                        </div>
                    </a>
                </div>
            </c:forEach>
        </div>
    </div>
</section>

<jsp:include page="../footer.jsp"/>
</body>
</html>