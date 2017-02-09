<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="lines" type="java.util.List<java.util.List<java.lang.String>>" scope="request"/>

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
            <table class="mdl-data-table mdl-shadow--2dp">
                <thead>
                <tr>
                    <th class="mdl-data-table__cell--non-numeric">Time</th>
                    <th class="mdl-data-table__cell--non-numeric">Thread</th>
                    <th class="mdl-data-table__cell--non-numeric">Level</th>
                    <th class="mdl-data-table__cell--non-numeric">Class</th>
                    <th class="mdl-data-table__cell--non-numeric">Message</th>
                </tr>
                </thead>
                <c:forEach var="line" items="${lines}" varStatus="loop">
                    <tr>
                        <td class="mdl-data-table__cell--non-numeric">${line[0]}</td>
                        <td class="mdl-data-table__cell--non-numeric">${line[1]}</td>
                        <td class="mdl-data-table__cell--non-numeric">${line[2]}</td>
                        <td class="mdl-data-table__cell--non-numeric">${line[3]}</td>
                        <td class="mdl-data-table__cell--non-numeric">${line[4]}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>

<jsp:include page="../footer.jsp"/>
</body>
</html>