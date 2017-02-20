<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="subscriptions" type="java.util.List<net.ehicks.bts.beans.Subscription>" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <title>BTS</title>
    <jsp:include page="inc_header.jsp"/>

    <script>
        function createSubscription()
        {
            $('#frmCreateSubscription').submit();
        }

        function deleteSubscription(subscriptionId)
        {
            if (confirm('Are you sure?'))
                location.href="${pageContext.request.contextPath}/view?tab1=settings&tab2=subscriptions&action=delete&subscriptionId=" + subscriptionId;
        }

        function printSubscriptions()
        {
            window.open(
                '${pageContext.request.contextPath}/view?tab1=settings&tab2=subscriptions&action=print',
                '_blank' // <- This is what makes it open in a new window.
            );
        }
    </script>
</head>
<body>

<jsp:include page="header.jsp"/>

<div class="mdl-grid">

    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>Manage Subscriptions</h5></div>

        <div class="tableContainer">
            <table class="mdl-data-table mdl-shadow--2dp">
                <thead>
                <tr>
                    <th>
                        <label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect mdl-data-table__select" for="table-header">
                            <input type="checkbox" id="table-header" class="mdl-checkbox__input" />
                        </label>
                    </th>
                    <th>Object Id</th>
                    <th class="mdl-data-table__cell--non-numeric">Description</th>
                    <th></th>
                </tr>
                </thead>
                <c:forEach var="subscription" items="${subscriptions}" varStatus="loop">
                    <tr>
                        <td>
                            <label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect mdl-data-table__select" for="row[${loop.count}]">
                                <input type="checkbox" id="row[${loop.count}]" class="mdl-checkbox__input" />
                            </label>
                        </td>
                        <td><a href="${pageContext.request.contextPath}/view?tab1=main&tab2=subscriptions&action=form&subscriptionId=${subscription.id}">${subscription.id}</a></td>
                        <td class="mdl-data-table__cell--non-numeric">${subscription.description}</td>
                        <td><a onclick="deleteSubscription('${subscription.id}');" class="clickable material-icons">delete</a></td>
                    </tr>
                </c:forEach>
                <c:if test="${empty subscriptions}">
                    <tr>
                        <td colspan="4">No subscriptions found.</td>
                    </tr>
                </c:if>
            </table>
        </div>

        <div class="mdl-card__actions">
            <input id="printSubscriptionButton" type="button" value="Print Subscriptions" onclick="printSubscriptions();"
                   class="mdl-button mdl-js-button mdl-button--raised"
                   style="padding-left:40px;background-image: url('${pageContext.request.contextPath}/images/mimetypes/Adobe.png'); background-position: left; background-repeat: no-repeat;"/>
        </div>
    </div>
    <br>
    <div class="mdl-card mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-cell--4-col-phone mdl-shadow--2dp">
        <div class="mdl-card__title"><h5>Add Subscription</h5></div>

        <form id="frmCreateSubscription" name="frmCreateSubscription" method="post" action="${pageContext.request.contextPath}/view?tab1=settings&tab2=subscriptions&action=add">
            <div style="padding: 10px;max-width: 300px;">
                <t:multiSelect id="projectIds" selectedValues="" items="${projects}" placeHolder="Projects:"/>
                <br>
                <t:multiSelect id="groupIds" selectedValues="" items="${groups}" placeHolder="Groups:"/>
                <br>
                <t:multiSelect id="severityIds" selectedValues="" items="${severities}" placeHolder="Severities:"/>
                <br>
                <t:multiSelect id="statusIds" selectedValues="" items="${statuses}" placeHolder="Statuses:"/>
                <br>
                <t:multiSelect id="assigneeIds" selectedValues="" items="${users}" placeHolder="Assignees:"/>
            </div>
        </form>

        <div class="mdl-card__actions">
            <input type="button" value="Add" onclick="createSubscription();" class="mdl-button mdl-js-button mdl-button--raised"/>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>