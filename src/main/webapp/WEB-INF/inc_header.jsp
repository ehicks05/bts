<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bulma/0.8.0/css/bulma.min.css" />
</c:if>
<c:if test="${!empty btsSystem.theme && !(btsSystem.theme eq 'default')}">
    <link rel="stylesheet" href="https://unpkg.com/bulmaswatch@0.7.5/${btsSystem.theme}/bulmaswatch.min.css">
</c:if>
<link rel="stylesheet" type="text/css" href="https://unpkg.com/bulma-prefers-dark" />
<%-- Bulma --%>

<%-- Font Awesome --%>
<script src="https://kit.fontawesome.com/eb9542ee7f.js" crossorigin="anonymous"></script>
<%-- Font Awesome --%>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/bts.css" media="screen" />
<script src="${pageContext.request.contextPath}/js/util.js"></script>
<script src="${pageContext.request.contextPath}/js/ajaxUtil.js"></script>

<style>
    @media(prefers-color-scheme: dark)
    {
        .editable:hover {background-color: rgb(54, 54, 54);}
        .modal-card-body {background-color: rgb(54, 54, 54);}
        .select > select {
            border-color: #363636 !important;
            background-color: #0a0a0a !important;
            color: #dbdbdb !important;
        }

        .invertable {
            filter: invert(100%);
        }

        .select2-container--default .select2-selection--single{
            background-color: #000;
        }

        .select2-container--default .select2-selection--multiple{
            background-color: #000;
            border-color: rgb(54, 54, 54);
        }

        .select2-search { background-color: #000; }
        .select2-search input { color: #dbdbdb; background-color: #000; }
        .select2-results { background-color: #000; }
        .select2-dropdown {border-color: rgb(54, 54, 54);}
        .select2-container--default .select2-selection--multiple .select2-selection__choice {background-color: rgb(54, 54, 54);}
    }
</style>