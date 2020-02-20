<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="inc_title.jsp"/>
    <jsp:include page="inc_header.jsp"/>

    <style>
        #options {padding:5px;}
        #options i {vertical-align:middle!important; font-size: 3em!important;}
    </style>
</head>
<body>

<jsp:include page="header.jsp"/>

<section class="hero is-primary is-small">
    <div class="hero-body">
        <div class="container">
            <h1 class="title">
                Settings
            </h1>
        </div>
    </div>
</section>

<section class="section">
    <div class="container">
        <div class="columns is-multiline is-centered">
            <c:forEach var="adminSubscreen" items="${userSession.systemInfo.settingsSubscreens}">
                <div class="column is-one-third has-text-centered">
                    <a href="${pageContext.request.contextPath}/${adminSubscreen[0]}">
                        <span class="icon is-large">
                            <i class="fas fa-3x fa-${adminSubscreen[1]}"></i>
                        </span>
                        <br>${adminSubscreen[2]}
                    </a>
                </div>
            </c:forEach>
        </div>
    </div>
</section>

<jsp:include page="footer.jsp"/>
</body>
</html>