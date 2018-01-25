<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="btsSystem" type="net.ehicks.bts.beans.BtsSystem" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../inc_title.jsp"/>
    <jsp:include page="../inc_header.jsp"/>

    <script>
        $('document').ready(function ()
        {
            $('#emailhost').focus();
        })
    </script>
</head>
<body>

<jsp:include page="../header.jsp"/>

<section class="hero is-primary is-small">
    <div class="hero-body">
        <div class="container">
            <h1 class="title">
                Email Settings
            </h1>
        </div>
    </div>
</section>

<section class="section">
    <div class="container">
        <div class="columns is-multiline is-centered">
            <div class="column">
                <form id="frmProject" method="post" action="${pageContext.request.contextPath}/view?tab1=admin&tab2=email&tab3=modify&action=modify">
                    <div class="field is-horizontal">
                        <div class="field-label">
                            <label class="label">Host</label>
                        </div>
                        <div class="field-body">
                            <div class="control">
                                <input class="input" type="text" placeholder="Host" id="emailHost" name="emailHost" value="${btsSystem.emailHost}">
                            </div>
                        </div>
                    </div>
                    <div class="field is-horizontal">
                        <div class="field-label">
                            <label class="label">Username</label>
                        </div>
                        <div class="field-body">
                            <div class="control">
                                <input class="input" type="text" placeholder="Username" id="emailUser" name="emailUser" value="${btsSystem.emailUser}">
                            </div>
                        </div>
                    </div>
                    <div class="field is-horizontal">
                        <div class="field-label">
                            <label class="label">Password</label>
                        </div>
                        <div class="field-body">
                            <div class="control">
                                <input class="input" type="text" placeholder="Password" id="emailPassword" name="emailPassword" value="${btsSystem.emailPassword}">
                            </div>
                        </div>
                    </div>
                    <div class="field is-horizontal">
                        <div class="field-label">
                            <label class="label">Port</label>
                        </div>
                        <div class="field-body">
                            <div class="control">
                                <input class="input" type="text" placeholder="Port" id="emailPort" name="emailPort" value="${btsSystem.emailPort}">
                            </div>
                        </div>
                    </div>
                    <div class="field is-horizontal">
                        <div class="field-label">
                            <label class="label">From Address</label>
                        </div>
                        <div class="field-body">
                            <div class="control">
                                <input class="input" type="text" placeholder="From Address" id="emailFromAddress" name="emailFromAddress" value="${btsSystem.emailFromAddress}">
                            </div>
                        </div>
                    </div>
                    <div class="field is-horizontal">
                        <div class="field-label">
                            <label class="label">From Name</label>
                        </div>
                        <div class="field-body">
                            <div class="control">
                                <input class="input" type="text" placeholder="From Name" id="emailFromName" name="emailFromName" value="${btsSystem.emailFromName}">
                            </div>
                        </div>
                    </div>
                    
                    <div class="is-centered">
                        <input id="saveProjectButton" type="submit" value="Save" class="button is-primary" />
                    </div>
                </form>
            </div>
        </div>
    </div>
</section>

<jsp:include page="../footer.jsp"/>
</body>
</html>