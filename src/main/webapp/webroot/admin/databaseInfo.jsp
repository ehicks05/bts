<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="tableSizes" type="java.util.Map<java.lang.String, java.util.List<java.lang.Object>>" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../inc_title.jsp"/>
    <jsp:include page="../inc_header.jsp"/>
</head>
<body>

<jsp:include page="../header.jsp"/>

<section class="hero is-primary is-small">
    <div class="hero-body">
        <div class="container">
            <h1 class="title">
                Database Info
            </h1>
        </div>
    </div>
</section>

<section class="section">
    <div class="container">
        <div class="columns is-multiline is-centered">
            <div class="column">
                <div class="box overflowTableContainer">
                    <h5 class="subtitle is-5">Table Sizes</h5>

                    <table class="table is-narrow">
                        <tr>
                            <c:forEach var="columnLabel" items="${tableSizes['columnLabels']}">
                                <th>${columnLabel}</th>
                            </c:forEach>
                        </tr>

                        <c:forEach var="resultRow" items="${tableSizes['resultRows']}">
                            <tr>
                                <c:forEach var="resultCell" items="${resultRow}">
                                    <td>${resultCell}</td>
                                </c:forEach>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="../footer.jsp"/>
</body>
</html>