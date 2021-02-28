<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<footer class="footer">
    <div class="container">
        <div class="columns">
            <div class="column">
                <p><img src="${pageContext.request.contextPath}/images/bug_16.png"/>
                    &centerdot; <strong>${btsSystem.siteName}</strong>
                </p>
            </div>
            <div class="column has-text-right" id="projectInfo">
                <p>
                    <a href="https://github.com/ehicks05/puffin">Puffin</a>
                    by <a href="https://ehicks.net">Eric Hicks</a>
                </p>
            </div>
        </div>
    </div>
</footer>
<jsp:include page="inc_createIssueDialog.jsp"/>
<c:if test="${pageContext.request.userPrincipal.principal.admin}">
    <script>
        $(function () {
            fetch("${pageContext.request.contextPath}/api/ajaxGetRequestStats/${requestId}")
                .then(function(response) {
                    return response.json();
                })
                .then(function(myJson) {
                    if (!myJson || myJson.error)
                    {
                        $('#requestStats').remove();
                        return;
                    }

                    const message =
                        'R: ' + myJson.requestTime + ' ms' +
                        ' | H: ' + myJson.handleTime + '' +
                        ' | PH: ' + myJson.postHandleTime + '' +
                        ' | T: ' + myJson.templateTime + '';
                    const jsonString = JSON.stringify(myJson);
                    $('<p></p>').prop('id', 'requestStats').text(message).addClass('is-size-7').prop('title', jsonString).appendTo($('#projectInfo'));
                    $('#requestStats').prop('title', jsonString);
                });
        });
    </script>
</c:if>
