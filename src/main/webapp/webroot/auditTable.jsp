<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="auditForm" type="net.ehicks.bts.AuditForm" scope="request"/>
<jsp:useBean id="searchResult" type="net.ehicks.bts.SearchResult" scope="request"/>

<div class="overflowTableContainer">
    <table id="filmTable" style="width:100%;margin: 0 auto" class="table is-striped is-narrow is-hoverable is-fullwidth">
        <thead>
        <tr class="listheading">
            <t:sortableCell code="id" label="Audit ID" style="text-align:right;" searchForm="${auditForm}" />
            <t:sortableCell code="user_id" label="User ID" searchForm="${auditForm}"/>
            <t:sortableCell code="user_ip" label="User IP" searchForm="${auditForm}"/>
            <t:sortableCell code="event_time" label="Event Time" searchForm="${auditForm}" cssClass="has-text-right"/>
            <t:sortableCell code="event_type" label="Event Type" searchForm="${auditForm}"/>
            <t:sortableCell code="object_key" label="Object Key" searchForm="${auditForm}"/>
            <t:sortableCell code="field_name" label="Field Name" searchForm="${auditForm}"/>
            <th>Old Value</th>
            <th>New Value</th>
        </tr>
        </thead>

        <tbody id="myTBody">
        <c:forEach var="audit" items="${searchResult.searchResults}" varStatus="loop">
            <tr>
                <td class="has-text-right">${audit.id}</td>
                <td>${audit.userId} ${audit.userName}</td>
                <td>${audit.userIp}</td>
                <td style="white-space: nowrap" class="has-text-right"><fmt:formatDate value="${audit.eventTime}" pattern="dd/MMM/yy h:mm:ss a" /></td>
                <td>${audit.eventType}</td>
                <td>${audit.objectKey}</td>
                <td>${audit.fieldName}</td>
                <td>${audit.oldValue}</td>
                <td>${audit.newValue}</td>
            </tr>
        </c:forEach>
        </tbody>

        <c:if test="${empty searchResult.searchResults}">
            <tr><td colspan="100">No Results</td></tr>
        </c:if>
    </table>
</div>
<t:paginator searchForm="${auditForm}" searchResult="${searchResult}"/>
