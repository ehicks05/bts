<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../inc_title.jsp"/>
    <jsp:include page="../inc_header.jsp"/>

    <script>

    </script>
</head>
<body>

<jsp:include page="../header.jsp"/>

<section class="hero is-primary is-small">
    <div class="hero-body">
        <div class="container">
            <h1 class="title">
                Modify System
            </h1>
        </div>
    </div>
</section>

<section class="section">
    <div class="container">
        <div class="columns is-multiline is-centered">
            <div class="column">
                <form id="frmProject" method="post" action="${pageContext.request.contextPath}/admin/system/modify/modify">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <t:text id="instanceName" label="Instance Name" value="${btsSystem.instanceName}" />
                    <t:textarea id="logonMessage" label="Logon Message" value="${btsSystem.logonMessage}" rich="true"/>
                    <t:text id="defaultAvatar" label="Default Avatar" value="${btsSystem.defaultAvatar.id}"/>
                    <t:basicSelect id="theme" label="Theme" items="${themes}" value="${btsSystem.theme}"/>
                    <t:text id="emailFromAddress" label="Email 'From' Address" value="${btsSystem.emailFromAddress}"/>
                    <t:text id="emailFromName" label="Email 'From' Name" value="${btsSystem.emailFromName}"/>

                    <input id="saveSystemButton" type="submit" value="Save" class="button is-primary" />
                </form>
            </div>
        </div>
    </div>
</section>

<jsp:include page="../footer.jsp"/>
</body>
</html>