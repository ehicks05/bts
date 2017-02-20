<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="issueForms" type="java.util.List<net.ehicks.bts.beans.IssueForm>" scope="request"/>

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
        <div class="mdl-card__title"><h5>Your Saved Filters: ${fn:length(issueForms)}</h5></div>

        <table id="issueForms" style="margin: 0 auto" class="mdl-data-table mdl-js-data-table mdl-data-table--selectable mdl-shadow--2dp">
            <thead>
            <tr>
                <th>
                    ID
                </th>
                <th class="mdl-data-table__cell--non-numeric">
                    Name
                </th>
                <th>On Dashboard</th>
                <th></th>
            </tr>
            </thead>

            <c:forEach var="issueForm" items="${issueForms}">
                <tr>
                    <td>
                        <a href="${pageContext.request.contextPath}/view?tab1=search&action=form&issueFormId=${issueForm.id}">
                            ${issueForm.id}
                        </a>
                    </td>
                    <td class="mdl-data-table__cell--non-numeric">
                        ${issueForm.formName}
                    </td>
                    <td class="mdl-data-table__cell--non-numeric">
                        <c:if test="${issueForm.onDash}"><i class="material-icons" style="color: green;">check</i></c:if>
                        <c:if test="${!issueForm.onDash}"><i class="material-icons" style="color: red;">clear</i></c:if>
                    </td>
                    <td>
                        <a class="material-icons" href="${pageContext.request.contextPath}/view?tab1=settings&tab2=savedSearches&action=delete&issueFormId=${issueForm.id}">delete</a>
                    </td>
                </tr>
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