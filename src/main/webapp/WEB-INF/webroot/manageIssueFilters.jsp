<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="issueForms" type="java.util.List<com.hicks.beans.IssueForm>" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <title>BTS</title>
    <jsp:include page="inc_header.jsp"/>
</head>
<body>

<jsp:include page="header.jsp"/>

<div class="mdl-grid">
    <br>
    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--4-col-tablet mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>Saved Filters: ${fn:length(issueForms)}</h5></div>

        <table id="issueForms" style="margin: 0 auto" class="list">
            <tr class="listheading">
                <td>
                    ID
                </td>
                <td>
                    Name
                </td>
            </tr>

            <c:set var="rowStyle" value="listrowodd"/>
            <c:set var="rowToggle" value="${true}"/>
            <c:forEach var="issueForm" items="${issueForms}">

                <tr class="${rowStyle}">
                    <td class="alignright">
                        <a href="${pageContext.request.contextPath}/view?tab2=search&action=form&issueFormId=${issueForm.id}">
                            ${issueForm.id}
                        </a>
                    </td>
                    <td>
                        ${issueForm.formName}
                    </td>
                </tr>

                <c:if test="${rowToggle}"><c:set var="rowStyle" value="listroweven"/></c:if>
                <c:if test="${!rowToggle}"><c:set var="rowStyle" value="listrowodd"/></c:if>
                <c:set var="rowToggle" value="${!rowToggle}"/>
            </c:forEach>

            <c:if test="${empty issueForms}">
                <tr><td colspan="100">Nothing to show here.</td></tr>
            </c:if>
        </table>
    </div>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>