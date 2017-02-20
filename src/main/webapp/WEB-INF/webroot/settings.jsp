<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>BTS</title>
    <jsp:include page="inc_header.jsp"/>

    <style>
        #options {padding:5px;}
        #options i {vertical-align:middle!important; font-size: 3em!important;}
    </style>
</head>
<body>

<jsp:include page="header.jsp"/>

<div class="mdl-grid">
    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>Settings</h5></div>

        <div id="options">
            <div>
                <a href="${pageContext.request.contextPath}/view?tab1=settings&tab2=savedSearches&action=form">
                    <i class="material-icons">search</i>Saved Searches</a>
            </div>

            <div>
                <a href="${pageContext.request.contextPath}/view?tab1=settings&tab2=subscriptions&action=form">
                    <i class="material-icons">email</i>Subscriptions</a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>