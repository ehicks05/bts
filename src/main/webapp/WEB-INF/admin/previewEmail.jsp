<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="email" type="net.ehicks.bts.beans.EmailEvent" scope="request"/>

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
                Preview Email
            </h1>
        </div>
    </div>
</section>

<section class="section">
    <div class="container">
        <div class="title is-5">Subject</div>
        <p>${email.subject}</p>
    </div>
</section>
<section class="section">
    <div class="container">
        <div class="title is-5">Body</div>
        ${email.body}
    </div>
</section>

<section class="section">
    <div class="container">
        <div class="title is-5">Metadata</div>
        <table class="table is-striped is-narrow is-hoverable">
            <thead>
            <tr>
                <th class="has-text-right">Object Id</th>
                <th class="has-text-centered">Status</th>
                <th class="has-text-right">User Who Caused the Action</th>
                <th>action</th>
                <th class="has-text-right">issueId</th>
                <th class="has-text-right">commentId</th>
            </tr>
            </thead>
            <tr>
                <td class="has-text-right">${email.id}</td>
                <td  class="has-text-centered" title="${email.status}">
                    <i class="fas fa-${email.statusIcon}"></i>
                </td>
                <td class="has-text-right">${email.user.id}</td>
                <td>${email.eventType.verb}</td>
                <td class="has-text-right">${email.issue.id}</td>
                <td class="has-text-right">${email.comment.id}</td>
            </tr>
        </table>
    </div>
</section>

<jsp:include page="../footer.jsp"/>
</body>
</html>