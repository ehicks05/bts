<%--
  Created by IntelliJ IDEA.
  User: eric
  Date: 7/7/2016
  Time: 10:59 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:if test="${!empty btsSystem}">
    <jsp:useBean id="btsSystem" type="net.ehicks.bts.beans.BtsSystem" scope="request"/>
</c:if>
<c:if test="${!empty signUpResultMessage}">
    <jsp:useBean id="signUpResultMessage" type="java.lang.String" scope="session"/>
</c:if>
<c:if test="${!empty signUpResultClass}">
    <jsp:useBean id="signUpResultClass" type="java.lang.String" scope="session"/>
</c:if>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="inc_title.jsp"/>
    <jsp:include page="inc_header.jsp"/>

    <style>
        .hero.is-success {
            background: #F2F6FA;
        }
        @media(prefers-color-scheme: dark)
        {
            .hero.is-success {
                background: #222;
            }
        }
    </style>
</head>
<body>
<nav class="navbar" role="navigation" aria-label="main navigation">
    <div class="container">
        <div class="navbar-brand">
            <div class="navbar-item">
                <div class="logoContainer has-background-light">
                    <img src="${pageContext.request.contextPath}/images/puffin-text.png" alt="Puffin" style="margin: .5rem .5rem 0;" />
                </div>
            </div>
        </div>
    </div>
</nav>

<section class="hero is-success is-fullheight">
    <div class="hero-body">
        <div class="container has-text-centered">
            <div class="column is-4 is-offset-4">

                <div class="box">
                    <h3 class="title has-text-grey">${btsSystem.siteName}</h3>
                    <p class="subtitle has-text-grey">Please log in</p>
                    <form id="frmLogin" method="POST" action="${pageContext.request.contextPath}/login">
                        <div class="field">
                            <div class="control">
                                <input class="input" type="email" placeholder="Your Email" autofocus="true" id="username" name="username">
                            </div>
                        </div>

                        <div class="field">
                            <div class="control">
                                <input class="input" type="password" placeholder="Your Password" id="password" name="password">
                            </div>
                        </div>
                        <div class="field">
                            <label class="checkbox">
                                <input type="checkbox" name="rememberMe">
                                Remember me
                            </label>
                        </div>
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="submit" value="Login" class="button is-block is-primary" onclick="frmLogin.submit();" />
                    </form>
                </div>

                <c:if test="${error != null}">
                    <article id="errorMessage" class="message is-danger">
                        <div class="message-header">
                            <p>Uh Oh</p>
                            <button class="delete" aria-label="delete" onclick="$('#errorMessage').addClass('is-hidden')"></button>
                        </div>
                        <div class="message-body">
                                ${error}
                        </div>
                    </article>
                </c:if>

                <article id="welcomeMessage" class="message is-primary">
                    <div class="message-header">
                        <p>Welcome Message</p>
                        <button class="delete" aria-label="delete" onclick="$('#welcomeMessage').addClass('is-hidden')"></button>
                    </div>
                    <div class="message-body">
                        ${btsSystem.logonMessage}
                    </div>
                </article>

                <c:if test="${!empty sessionScope['signUpResultMessage']}">
                    <article id="signUpResultMessage" class="message ${sessionScope['signUpResultClass']}">
                        <div class="message-header">
                            <p>Sign Up Result</p>
                            <button class="delete" aria-label="delete" onclick="$('#signUpResultMessage').addClass('is-hidden')"></button>
                        </div>
                        <div class="message-body">
                            ${sessionScope['signUpResultMessage']}
                            <c:if test="${sessionScope['signUpResultClass'] eq 'is-success'}">
                                <i class="fas fa-trophy has-text-warning" ></i>
                                <i class="fas fa-star has-text-warning" ></i>
                            </c:if>
                        </div>
                    </article>

                    <c:remove var="signUpResultMessage" scope="session"/>
                    <c:remove var="signUpResultClass" scope="session"/>
                </c:if>

                <p class="has-text-grey">
                    Created by <a href="https://ehicks.net" style="text-decoration: underline">Eric Hicks</a>
                </p>
            </div>
        </div>
    </div>
</section>

<div class="modal" id="signUpDialog">
    <div class="modal-background"></div>
    <div class="modal-card">
        <header class="modal-card-head">
            <p class="modal-card-title">Sign Up</p>
            <button class="delete" aria-label="close"></button>
        </header>
        <section class="modal-card-body">
            <form id="frmSignUp" name="frmCreateIssue" method="post" action="${pageContext.request.contextPath}/signUp?tab1=registration/register">
                <t:text id="fldEmailAddress" label="Email" required="true" />
                <t:text id="fldPassword" label="Password" required="true" />
                <t:text id="fldPassword2" label="Re-enter Password" required="true" />
            </form>
        </section>
        <footer class="modal-card-foot">
            <button class="button is-primary create">Sign Up</button>
            <button class="button close">Cancel</button>
        </footer>
    </div>
</div>

<%--<script>--%>
<%--    initDialog('signUp');--%>

<%--    $(function () {--%>
<%--        var dialog = $('#signUpDialog');--%>

<%--        dialog.find('.create').on('click', function ()--%>
<%--        {--%>
<%--            $('#frmSignUp').submit()--%>
<%--        });--%>
<%--    });--%>
<%--</script>--%>
</body>
</html>
