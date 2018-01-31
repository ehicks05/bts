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

<section class="hero is-primary is-small">
    <div class="hero-body">
        <div class="container">
            <h1 class="title">
                Cache Info
            </h1>
        </div>
    </div>
</section>

<section class="section">
    <div class="container">
        <div class="columns is-multiline is-centered">
            <div class="column is-narrow">
                <table class="table is-striped is-narrow is-hoverable is-fullwidth">
                    <thead>
                        <tr>
                            <th>Property</th>
                            <th class="has-text-right">Value</th>
                        </tr>
                    </thead>
                    <tr>
                        <td>Size</td>
                        <td class="has-text-right">${size}</td>
                    </tr>
                    <tr>
                        <td>Hits</td>
                        <td class="has-text-right">${hits}</td>
                    </tr>
                    <tr>
                        <td>Misses</td>
                        <td class="has-text-right">${misses}</td>
                    </tr>
                    <tr>
                        <td>Key Hit Object Miss</td>
                        <td class="has-text-right">${keyHitObjectMiss}</td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</section>

<jsp:include page="../footer.jsp"/>
</body>
</html>