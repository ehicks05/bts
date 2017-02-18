<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="auditForm" type="net.ehicks.bts.AuditForm" scope="request"/>
<jsp:useBean id="searchResult" type="net.ehicks.bts.SearchResult" scope="request"/>

<table id="filmTable" style="width:100%;margin: 0 auto" class="list">
    <thead>
    <tr class="listheading">
        <t:sortableCell code="id" label="Audit ID" style="text-align:right;" searchForm="${auditForm}" />
        <t:sortableCell code="user_id" label="User ID" searchForm="${auditForm}"/>
        <t:sortableCell code="user_ip" label="User IP" searchForm="${auditForm}"/>
        <t:sortableCell code="event_time" label="Event Time" searchForm="${auditForm}"/>
        <t:sortableCell code="event_type" label="Event Type" searchForm="${auditForm}"/>
        <t:sortableCell code="object_key" label="Object Key" searchForm="${auditForm}"/>
        <t:sortableCell code="field_name" label="Field Name" searchForm="${auditForm}"/>
        <th class="mdl-data-table__cell--non-numeric">Old Value</th>
        <th class="mdl-data-table__cell--non-numeric">New Value</th>
    </tr>
    </thead>

    <tbody id="myTBody">
    <c:forEach var="audit" items="${searchResult.searchResults}" varStatus="loop">
        <tr class="${loop.index % 2 == 0 ? 'listrowodd' : 'listroweven'}">
            <td class="alignright">${audit.id}</td>
            <td class="mdl-data-table__cell--non-numeric">${audit.userId} ${audit.userName}</td>
            <td class="mdl-data-table__cell--non-numeric">${audit.userIp}</td>
            <td class="alignright"><fmt:formatDate value="${audit.eventTime}" pattern="dd/MMM/yy hh:mm:ss a" /></td>
            <td class="mdl-data-table__cell--non-numeric">${audit.eventType}</td>
            <td class="mdl-data-table__cell--non-numeric">${audit.objectKey}</td>
            <td class="mdl-data-table__cell--non-numeric">${audit.fieldName}</td>
            <td class="mdl-data-table__cell--non-numeric">${audit.oldValue}</td>
            <td class="mdl-data-table__cell--non-numeric">${audit.newValue}</td>
        </tr>
    </c:forEach>
    </tbody>

    <c:if test="${empty searchResult.searchResults}">
        <tr><td colspan="100">No Results</td></tr>
    </c:if>
    <t:paginator searchForm="${auditForm}" searchResult="${searchResult}"/>
</table>