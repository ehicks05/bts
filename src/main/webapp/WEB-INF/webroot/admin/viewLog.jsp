<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="lines" type="java.util.List<java.util.List<java.lang.String>>" scope="request"/>
<jsp:useBean id="threadToColorMap" type="java.util.Map<java.lang.String, java.lang.String>" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <title>BTS</title>
    <jsp:include page="../inc_header.jsp"/>

</head>
<body>

<jsp:include page="../header.jsp"/>

<div class="mdl-grid">

    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>Log ${logName}</h5></div>

        <div class="tableContainer">
            <table class="list" style="font-size: 0.8em">
                <thead>
                <tr class="listheading">
                    <td>Time</td>
                    <td>Thread</td>
                    <td>Level</td>
                    <td>Class</td>
                    <td>Message</td>
                </tr>
                </thead>
                <c:forEach var="line" items="${lines}" varStatus="loop">
                    <tr class="listrowodd" style="background-color: ${threadToColorMap[line[1]]}">
                        <td>${line[0]}</td>
                        <td>${line[1]}</td>
                        <td>${line[2]}</td>
                        <td>${line[3]}</td>
                        <td>${line[4]}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>

<jsp:include page="../footer.jsp"/>
</body>
</html>