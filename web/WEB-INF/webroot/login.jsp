<%--
  Created by IntelliJ IDEA.
  User: eric
  Date: 7/7/2016
  Time: 10:59 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>

    <jsp:include page="inc_header.jsp"/>

    <script>

        function initForm()
        {
            $('#j_username').focus();
        }

        window.onload = initForm;
    </script>
</head>
<body>
<jsp:include page="header.jsp"/>
<br>
<div align="center">
    <div style="padding:10px; max-width: 400px; background-color: white;">
        <form method="POST" action="j_security_check">
            <table>
                <tr>
                    <td colspan="2">Login to BTS:</td>
                </tr>
                <tr>
                    <td>Name:</td>
                    <td><input type="text" id="j_username" name="j_username" /></td>
                </tr>
                <tr>
                    <td>Password:</td>
                    <td><input type="password" id="j_password" name="j_password"/></td>
                </tr>
                <tr>
                    <td colspan="2"><input type="submit" value="Go" class="btn btn-primary"/></td>
                </tr>
            </table>
        </form>
    </div>
</div>

</body>
</html>
