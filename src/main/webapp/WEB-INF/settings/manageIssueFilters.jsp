<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<jsp:useBean id="issueForms" type="java.util.List<net.ehicks.bts.beans.IssueForm>" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../inc_title.jsp"/>
    <jsp:include page="../inc_header.jsp"/>

    <script>
        function deleteIssueForm(id) {
            if (confirm('Are you sure you want to delete this issue search form?'))
                location.href = '${pageContext.request.contextPath}/settings/savedSearches/delete?issueFormId=' + id;
        }
    </script>
</head>
<body>

<jsp:include page="../header.jsp"/>

<section class="hero is-primary is-small">
    <div class="hero-body">
        <div class="container">
            <h1 class="title">
                Saved Filters: ${fn:length(issueForms)}
            </h1>
        </div>
    </div>
</section>

<section class="section">
    <div class="container">
        <div class="columns is-multiline is-centered">
            <div class="column is-narrow">
                <table id="issueForms" class="table is-narrow is-striped">
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th class="has-text-centered">On Dashboard</th>
                            <th></th>
                        </tr>
                    </thead>

                    <c:forEach var="issueForm" items="${issueForms}">
                        <tr>
                            <td>
                                <a href="${pageContext.request.contextPath}/search/form?issueFormId=${issueForm.id}">
                                    ${issueForm.formName}
                                </a>
                            </td>
                            <td class="has-text-centered">
                                <c:if test="${issueForm.onDash}"><i class="fas fa-check has-text-success"></i></c:if>
                                <c:if test="${!issueForm.onDash}"><i class="fas fa-times has-text-danger"></i></c:if>
                            </td>
                            <td>
                                <button class="button is-small is-light" onclick="deleteIssueForm(${issueForm.id})"><i class="fas fa-trash"></i></button>
                            </td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty issueForms}">
                        <tr><td colspan="100">Nothing to show here.</td></tr>
                    </c:if>
                </table>
            </div>
        </div>
    </div>
</section>

<jsp:include page="../footer.jsp"/>
</body>
</html>