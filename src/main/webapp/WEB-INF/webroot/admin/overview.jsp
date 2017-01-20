<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>

<!DOCTYPE html>
<html>
<head>
    <title>BTS</title>
    <jsp:include page="../inc_header.jsp"/>

    <style>
        #options {padding:5px;}
        #options i {vertical-align:middle!important; font-size: 3em!important;}
    </style>
</head>
<body>

<jsp:include page="../header.jsp"/>

<div class="mdl-grid">

    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>Administration</h5></div>

        <div id="options">
            <div>
                <a href="${pageContext.request.contextPath}/view?tab1=admin&tab2=users&action=form">
                    <i class="material-icons">person</i>Manage Users</a>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/view?tab1=admin&tab2=projects&action=form">
                    <i class="material-icons">folder</i>Manage Projects</a>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/view?tab1=admin&tab2=groups&action=form">
                    <i class="material-icons">security</i>Manage Groups</a>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/view?tab1=admin&tab2=cache&action=form">
                    <i class="material-icons">memory</i>Cache Info</a>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/view?tab1=admin&tab2=system&action=form">
                    <i class="material-icons">computer</i>System Info</a>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/view?tab1=admin&tab2=audit&action=form">
                    <i class="material-icons">history</i>Audit Records</a>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/view?tab1=admin&tab2=email&action=form">
                    <i class="material-icons">email</i>Manage Email</a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../footer.jsp"/>
</body>
</html>