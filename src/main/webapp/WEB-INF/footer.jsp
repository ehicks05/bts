<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:eval expression="@gitVersionProperties.version" var="version" />
<spring:eval expression="@gitVersionProperties.revision" var="revision" />
<spring:eval expression="@environment.getProperty('puffin.siteName')" var="siteName" />

<footer class="footer">
    <div class="container">
        <div class="columns">
            <div class="column">
                <p><img src="${pageContext.request.contextPath}/images/bug_16.png"/>
                    &centerdot; <strong>${siteName}</strong>
                </p>
            </div>
            <div class="column has-text-right">
                <p>
                    <a href="https://github.com/ehicks05/puffin">Puffin</a>
                    <a href="https://github.com/ehicks05/puffin/commit/${revision}" title="${revision}">v${version}</a>
                    by <a href="https://ehicks.net">Eric Hicks</a>
                </p>
            </div>
        </div>
    </div>
</footer>
<jsp:include page="inc_createIssueDialog.jsp"/>
