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
    <jsp:include page="../inc_header.jsp"/>
    <jsp:include page="../inc_title.jsp"/>

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

<jsp:include page="../header.jsp"/>

<section class="hero is-primary is-small">
    <div class="hero-body">
        <div class="container">
            <h1 class="title">
                Subscriptions
            </h1>
        </div>
    </div>
</section>

<section class="section">
    <div class="container">
        <div class="columns is-multiline is-centered">
            <div class="column is-one-fifth">
                <form id="frmCreateSubscription" name="frmCreateSubscription" method="post" action="${pageContext.request.contextPath}/view?tab1=settings&tab2=subscriptions&action=add">
                    <nav class="panel">
                        <p class="panel-heading">
                            Add Subscription
                        </p>
                        <div class="panel-block">
                            <t:multiSelect id="projectIds" selectedValues="" items="${projects}" placeHolder="Projects"/>
                        </div>
                        <div class="panel-block">
                            <t:multiSelect id="groupIds" selectedValues="" items="${groups}" placeHolder="Groups"/>
                        </div>
                        <div class="panel-block">
                            <t:multiSelect id="severityIds" selectedValues="" items="${severities}" placeHolder="Severities"/>
                        </div>
                        <div class="panel-block">
                            <t:multiSelect id="statusIds" selectedValues="" items="${statuses}" placeHolder="Statuses"/>
                        </div>
                        <div class="panel-block">
                            <t:multiSelect id="assigneeIds" selectedValues="" items="${users}" placeHolder="Assignees"/>
                        </div>

                        <div class="panel-block">
                            <input type="submit" value="Add" class="button is-link is-outlined is-fullwidth" />
                        </div>
                    </nav>
                </form>
            </div>

            <div class="column">
                <div class="box">
                    <h3 class="subtitle is-3">Manage Subscriptions</h3>

                    <div class="ajaxTableContainer">
                        <table class="table">
                            <thead>
                            <tr>
                                <th>
                                    <label class="checkbox" for="table-header">
                                        <input type="checkbox" id="table-header" class="mdl-checkbox__input" />
                                    </label>
                                </th>
                                <th class="has-text-right">Object Id</th>
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
                                    <td class="has-text-right"><a href="${pageContext.request.contextPath}/view?tab1=settings&tab2=subscriptions&action=form&subscriptionId=${subscription.id}">${subscription.id}</a></td>
                                    <td class="mdl-data-table__cell--non-numeric">${subscription.description}</td>
                                    <td>
                                        <a onclick="deleteSubscription('${subscription.id}');" class="icon">
                                            <i class="fas fa-trash"></i>
                                        </a>
                                    </td>
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
                        <a class="button" id="printSubscriptionButton" onclick="printSubscriptions();">
                        <span class="icon has-text-danger">
                            <i class="fas fa-file-pdf"></i>
                        </span>
                            <span>Print Subscriptions</span>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="../footer.jsp"/>
</body>
</html>