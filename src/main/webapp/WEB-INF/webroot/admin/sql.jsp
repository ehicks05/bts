<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="users" type="java.util.List<net.ehicks.bts.beans.User>" scope="request"/>
<c:if test="${!empty sessionScope.resultSets}">
    <jsp:useBean id="resultSets" type="java.util.List<net.ehicks.bts.handlers.admin.PrintableSqlResult>" scope="session"/>
</c:if>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../inc_title.jsp"/>
    <jsp:include page="../inc_header.jsp"/>

    <script>
        $('document').ready(function ()
        {
            $('#runCommand').click(function ()
            {
                $('#frmSqlCommand').submit();
            });

            $('#sqlCommand').keydown(function ()
            {
                if ((event.keyCode == 10 || event.keyCode == 13) && event.ctrlKey)
                {
                    $('#frmSqlCommand').submit();
                }
            });
        });
    </script>
</head>
<body>

<jsp:include page="../header.jsp"/>

<div style="margin: 1%">
    <div>
        <div class="mdl-card__title"><h5>SQL Command</h5></div>

        <form id="frmSqlCommand" name="frmSqlCommand" method="post" action="${pageContext.request.contextPath}/view?tab1=admin&tab2=sql&action=runCommand">
            <label for="sqlCommand">SQL Command:</label><br>
            <textarea id="sqlCommand" name="sqlCommand" rows="5" cols="80">${sessionScope.sqlCommand}</textarea>
        </form>

        <div>
            <input id="runCommand" type="button" value="Run Command" class="mdl-button mdl-js-button mdl-button--raised" />
        </div>
        <br>
        
        <div class="mdl-card__title"><h5>Results</h5></div>

        <div class="tableContainer">
            <c:forEach var="resultSet" items="${resultSets}">
                <pre>${resultSet.sqlCommand}</pre>

                <c:if test="${!empty resultSet.columnLabels}">
                    <table class="list">
                        <tr class="listheading">
                            <td colspan="100">${resultSet.sqlCommand}</td>
                        </tr>
                        <tr class="listheading">
                            <c:forEach var="columnLabel" items="${resultSet.columnLabels}">
                                <td>${columnLabel}</td>
                            </c:forEach>
                        </tr>
                        <c:forEach var="resultRow" items="${resultSet.resultRows}">
                            <tr>
                                <c:forEach var="resultCell" items="${resultRow}">
                                    <td>${resultCell}</td>
                                </c:forEach>
                            </tr>
                        </c:forEach>
                    </table>
                </c:if>

                <c:if test="${!empty resultSet.rowsUpdated}">
                    ${resultSet.rowsUpdated} rows updated.
                </c:if>
                <c:if test="${!empty resultSet.error}">
                    ${resultSet.error}
                </c:if>
                <hr>
            </c:forEach>
        </div>
    </div>
</div>

<jsp:include page="../footer.jsp"/>
</body>
</html>