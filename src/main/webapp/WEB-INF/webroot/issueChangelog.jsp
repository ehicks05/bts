<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="audits" type="java.util.List<net.ehicks.bts.beans.Audit>" scope="request"/>

<table id="filmTable" style="width:100%;margin: 0 auto" class="list">
    <thead>
    <tr class="listheading">
        <th class="mdl-data-table__cell--non-numeric">User</th>
        <th class="mdl-data-table__cell--non-numeric">Event Time</th>
        <th class="mdl-data-table__cell--non-numeric">Event Type</th>
        <th class="mdl-data-table__cell--non-numeric">Object Key</th>
        <th class="mdl-data-table__cell--non-numeric">Field Name</th>
        <th class="mdl-data-table__cell--non-numeric">Old Value</th>
        <th class="mdl-data-table__cell--non-numeric">New Value</th>
    </tr>
    </thead>

    <tbody id="myTBody">
    <c:forEach var="audit" items="${audits}" varStatus="loop">
        <tr class="${loop.index % 2 == 0 ? 'listrowodd' : 'listroweven'}">
            <td class="mdl-data-table__cell--non-numeric">${audit.userName}</td>
            <td class="alignright"><fmt:formatDate value="${audit.eventTime}" pattern="dd/MMM/yy hh:mm a" /></td>
            <td class="mdl-data-table__cell--non-numeric">${audit.eventType}</td>
            <td class="mdl-data-table__cell--non-numeric">${audit.objectKey}</td>
            <td class="mdl-data-table__cell--non-numeric">${audit.fieldName}</td>
            <td class="mdl-data-table__cell--non-numeric">${audit.oldValue}</td>
            <td class="mdl-data-table__cell--non-numeric">${audit.newValue}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>