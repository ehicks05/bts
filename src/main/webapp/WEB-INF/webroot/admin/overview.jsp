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

    <style>
        #options {padding:5px; margin: auto;}
        #options div {text-align: center; padding: 10px;}
        #options i {vertical-align:middle!important; font-size: 3em!important;}

        #options {
            display: grid;
            grid-template-rows: auto;
            grid-template-columns: repeat(5, 200px);
        }
        @media (max-width: 1099px) and (min-width: 880px) {
            #options {
                display: grid;
                grid-template-rows: auto;
                grid-template-columns: repeat(4, 200px);
            }
        }
        @media (max-width: 879px) and (min-width: 660px) {
            #options {
                display: grid;
                grid-template-rows: auto;
                grid-template-columns: repeat(3, 200px);
            }
        }
        @media (max-width: 659px) and (min-width: 440px) {
            #options {
                display: grid;
                grid-template-rows: auto;
                grid-template-columns: repeat(2, 200px);
            }
        }
        @media (max-width: 439px) {
            #options {
                display: grid;
                grid-template-rows: auto;
                grid-template-columns: repeat(1, 200px);
            }
        }
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
                    <i class="material-icons">person</i><br>Manage Users</a>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/view?tab1=admin&tab2=projects&action=form">
                    <i class="material-icons">folder</i><br>Manage Projects</a>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/view?tab1=admin&tab2=groups&action=form">
                    <i class="material-icons">security</i><br>Manage Groups</a>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/view?tab1=admin&tab2=cache&action=form">
                    <i class="material-icons">memory</i><br>Cache Info</a>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/view?tab1=admin&tab2=system&action=form">
                    <i class="material-icons">computer</i><br>System Info</a>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/view?tab1=admin&tab2=audit&action=form">
                    <i class="material-icons">history</i><br>Audit Records</a>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/view?tab1=admin&tab2=email&action=form">
                    <i class="material-icons">email</i><br>Manage Email</a>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/view?tab1=admin&tab2=logs&action=form">
                    <i class="material-icons">assignment</i><br>Logs</a>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/view?tab1=admin&tab2=backups&action=form">
                    <i class="material-icons">backup</i><br>Backups</a>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/view?tab1=admin&tab2=sql&action=form">
                    <i class="material-icons">storage</i><br>SQL</a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../footer.jsp"/>
</body>
</html>