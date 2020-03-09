<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="subscriptions" type="java.util.List<net.ehicks.bts.beans.Subscription>" scope="request"/>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../inc_header.jsp"/>
    <jsp:include page="../inc_title.jsp"/>

    <script>
        function saveSubscription()
        {
            $('#frmCreateSubscription').submit();
        }

        function deleteSubscription(subscriptionId)
        {
            if (confirm('Are you sure?'))
                location.href="${pageContext.request.contextPath}/settings/subscriptions/delete?subscriptionId=" + subscriptionId;
        }

        function printSubscriptions()
        {
            window.open(
                '${pageContext.request.contextPath}/settings/subscriptions/print',
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
                <form:form id="frmCreateSubscription" name="frmCreateSubscription" modelAttribute="subscription"
                           method="post" action="${pageContext.request.contextPath}/settings/subscriptions/save">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <form:hidden path="user" />
                    <form:hidden path="id" />
                    <nav class="panel">
                        <p class="panel-heading">
                            <c:if test="${subscription.id == 0}">
                                Add Subscription
                            </c:if>
                            <c:if test="${subscription.id != 0}">
                                Update Subscription
                            </c:if>
                        </p>
                        <div class="panel-block"><t:multiSelect isSpring="true" id="projects" selectedValues="${subscription.projects}" items="${projects}" placeHolder="Projects"/></div>
                        <div class="panel-block"><t:multiSelect isSpring="true" id="groups" selectedValues="${subscription.groups}" items="${groups}" placeHolder="Groups"/></div>
                        <div class="panel-block"><t:multiSelect isSpring="true" id="severities" selectedValues="${subscription.severities}" items="${severities}" placeHolder="Severities"/></div>
                        <div class="panel-block"><t:multiSelect isSpring="true" id="statuses" selectedValues="${subscription.statuses}" items="${statuses}" placeHolder="Statuses"/></div>
                        <div class="panel-block"><t:multiSelect isSpring="true" id="issueTypes" selectedValues="${subscription.issueTypes}" items="${issueTypes}" placeHolder="Issue Types"/></div>
                        <div class="panel-block"><t:multiSelect isSpring="true" id="assignees" selectedValues="${subscription.assignees}" items="${users}" placeHolder="Assignees"/></div>
                        <div class="panel-block"><t:multiSelect isSpring="true" id="reporters" selectedValues="${subscription.reporters}" items="${users}" placeHolder="Reporters"/></div>

                        <div class="panel-block">
                            <button class="button is-link is-outlined is-fullwidth" onclick="saveSubscription()">
                                ${subscription.id == 0 ? 'Add' : 'Save'}
                            </button>
                        </div>
                    </nav>
                </form:form>
            </div>

            <div class="column is-narrow">
                <div class="box">
                    <h3 class="subtitle is-3">Manage Subscriptions</h3>

                    <div class="ajaxTableContainer">
                        <table class="table">
                            <thead>
                            <tr>
                                <th class="has-text-right">Object Id</th>
                                <th>Description</th>
                                <th></th>
                            </tr>
                            </thead>
                            <c:forEach var="sub" items="${subscriptions}" varStatus="loop">
                                <tr class="${sub.id == subscription.id ? 'is-selected' : ''}">
                                    <td class="has-text-right">
                                        <a href="${pageContext.request.contextPath}/settings/subscriptions/form?subscriptionId=${sub.id}">${sub.id}</a>
                                    </td>
                                    <td>${sub.description}</td>
                                    <td>
                                        <a onclick="deleteSubscription('${sub.id}');" class="icon">
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

                    <div class="buttons">
                        <button class="button" id="printSubscriptionButton" onclick="printSubscriptions();">
                            <span class="icon has-text-danger">
                                <i class="fas fa-file-pdf"></i>
                            </span>
                            <span>Print Subscriptions</span>
                        </button>
                        <a class="button" href="${pageContext.request.contextPath}/settings/subscriptions/form">
                            <span class="icon has-text-success">
                                <i class="fas fa-plus"></i>
                            </span>
                            <span>New Subscription</span>
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