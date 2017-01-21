<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>BTS</title>
    <jsp:include page="inc_header.jsp"/>
    <script>
        function initHeader()
        {

        }
    </script>

</head>
<body onload="initHeader();">

<jsp:include page="header.jsp"/>

<div class="mdl-grid">
    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
        <div class="mdl-card__title">
            <h5>
                <div class="mdl-card__title-text">
                    Sorry...
                </div>
                <div class="mdl-card__subtitle-text">
                    something went wrong...
                </div>
            </h5>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>