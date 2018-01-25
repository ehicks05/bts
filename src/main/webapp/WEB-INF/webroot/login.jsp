<%--
  Created by IntelliJ IDEA.
  User: eric
  Date: 7/7/2016
  Time: 10:59 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="inc_title.jsp"/>
    <jsp:include page="inc_header.jsp"/>

    <script>
        $(function () {
            $('#j_username').focus();


            $('#j_password').on('keypress', function (e) {
                if (e.keyCode === 13)
                {
                    $('#frmLogin').submit();
                }
            });
        });
    </script>
    <style>
        html,body {
            font-size: 14px;
            font-weight: 300;
        }
        .hero.is-success {
            background: #F2F6FA;
        }
        /*.avatar {*/
            /*margin-top: -70px;*/
            /*padding-bottom: 20px;*/
        /*}*/
        .avatar img {
            /*padding: 5px;*/
            /*background: #fff;*/
            /*border-radius: 0%;*/
            /*-webkit-box-shadow: 0 2px 3px rgba(10,10,10,.1), 0 0 0 1px rgba(10,10,10,.1);*/
            /*box-shadow: 0 2px 3px rgba(10,10,10,.1), 0 0 0 1px rgba(10,10,10,.1);*/
        }
        /*p.subtitle {*/
            /*padding-top: 1rem;*/
        /*}*/
    </style>
</head>
<body>
<section class="hero is-success is-fullheight">
    <div class="hero-body">
        <div class="container has-text-centered">
            <div class="column is-4 is-offset-4">
                <%--<h3 class="title has-text-grey">${applicationScope['systemInfo'].appName}</h3>--%>
                <%--<p class="subtitle has-text-grey">Please login to proceed.</p>--%>
                <div class="box">
                    <figure class="avatar">
                        <img src="${pageContext.request.contextPath}/images/puffin-text.png" style="width: 128px">
                    </figure>
                    <form id="frmLogin" method="POST" action="j_security_check">
                        <div class="field">
                            <div class="control">
                                <input class="input is-medium" type="email" placeholder="Your Email" autofocus="" id="j_username" name="j_username">
                            </div>
                        </div>

                        <div class="field">
                            <div class="control">
                                <input class="input is-medium" type="password" placeholder="Your Password" id="j_password" name="j_password">
                            </div>
                        </div>
                        <div class="field">
                            <label class="checkbox">
                                <input type="checkbox">
                                Remember me
                            </label>
                        </div>
                        <a class="button is-block is-primary is-medium" onclick="frmLogin.submit();">Login</a>
                    </form>
                </div>

                <article id="welcomeMessage" class="message is-primary">
                    <div class="message-header">
                        <p>Welcome Message</p>
                        <button class="delete" aria-label="delete" onclick="$('#welcomeMessage').addClass('is-hidden')"></button>
                    </div>
                    <div class="message-body">
                        ${applicationScope['btsSystem'].logonMessage}
                    </div>
                </article>

                <p class="has-text-grey">
                    <a href="../">Sign Up</a> &nbsp;·&nbsp;
                    <a href="../">Forgot Password</a> &nbsp;·&nbsp;
                    <a href="../">Need Help?</a>
                </p>
            </div>
        </div>
    </div>
</section>
</body>
</html>
