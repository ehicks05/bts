<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="users" type="java.util.List<net.ehicks.bts.beans.User>" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../inc_title.jsp"/>
    <jsp:include page="../inc_header.jsp"/>

    <script>
        $('document').ready(function ()
        {
            $('#clearCache').click(function ()
            {
                location.href = '${pageContext.request.contextPath}/view?tab1=admin&tab2=cache&action=clearCache';
            });
        });
    </script>
</head>
<body>

<jsp:include page="../header.jsp"/>

<div class="mdl-grid">

    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>Cache Info</h5></div>

        <div class="tableContainer">
            <table class="mdl-data-table mdl-shadow--2dp">
                <thead>
                <tr>
                    <th class="mdl-data-table__cell--non-numeric">Property</th>
                    <th>Value</th>
                </tr>
                </thead>
                <tr>
                    <td class="mdl-data-table__cell--non-numeric">Size</td>
                    <td>${size}</td>
                </tr>
                <tr>
                    <td class="mdl-data-table__cell--non-numeric">Hits</td>
                    <td>${hits}</td>
                </tr>
                <tr>
                    <td class="mdl-data-table__cell--non-numeric">Misses</td>
                    <td>${misses}</td>
                </tr>
                <tr>
                    <td class="mdl-data-table__cell--non-numeric">Key Hit Object Miss</td>
                    <td>${keyHitObjectMiss}</td>
                </tr>
            </table>
        </div>

        <div class="mdl-card__actions">
            <input id="clearCache" type="button" value="Clear Cache" class="mdl-button mdl-js-button mdl-button--raised" />
        </div>
    </div>
</div>

<jsp:include page="../footer.jsp"/>
</body>
</html>