<%--
  Created by IntelliJ IDEA.
  User: eric
  Date: 7/7/2016
  Time: 11:00 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <jsp:include page="inc_header.jsp"/>
</head>
<body>
<jsp:include page="header.jsp"/>
<br>
<div align="center">
    <div style="padding:10px; max-width: 400px; background-color: white;">
        Sorry, login failed!
        <br><br>
        <input type="button" class="btn btn-primary" value="Retry" onclick="location.href='${pageContext.request.contextPath}/view?tab1=main&action=form';">
    </div>
</div>
</body>
</html>
