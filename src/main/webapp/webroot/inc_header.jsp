<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="btsSystem" type="net.ehicks.bts.beans.BtsSystem" scope="application"/>

<meta name="viewport" content="width=device-width, initial-scale=1">
<meta charset="utf-8" />

<%-- JQuery --%>
<script
        src="https://code.jquery.com/jquery-3.3.1.min.js"
        integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
        crossorigin="anonymous"></script>
<%-- JQuery --%>

<%-- Select2 --%>
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>
<%-- Select2 --%>

<%-- spin.js --%>
<script src="${pageContext.request.contextPath}/js/spin.min.js"></script>
<%-- spin.js --%>

<%-- qTip2 --%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/qtip2/3.0.3/jquery.qtip.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/qtip2/3.0.3/jquery.qtip.min.js"></script>
<%-- qTip2 --%>

<%-- Bulma --%>
<c:if test="${empty btsSystem.theme || btsSystem.theme eq 'default'}">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bulma/0.6.2/css/bulma.min.css" />
</c:if>
<c:if test="${!empty btsSystem.theme && !(btsSystem.theme eq 'default')}">
    <link rel="stylesheet" href="https://unpkg.com/bulmaswatch/${btsSystem.theme}/bulmaswatch.min.css">
</c:if>
<%-- Bulma --%>

<%-- Font Awesome --%>
<script defer src="https://use.fontawesome.com/releases/v5.0.4/js/all.js"></script>
<%-- Font Awesome --%>

<link rel="shortcut icon" href="${pageContext.request.contextPath}/images/puffin.png">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/bts.css" media="screen" />
<script src="${pageContext.request.contextPath}/js/util.js"></script>
<script src="${pageContext.request.contextPath}/js/ajaxUtil.js"></script>