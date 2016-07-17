<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
    function doSearch(searchTerm)
    {
        $('#header_title').val(searchTerm);
        $('#header_description').val(searchTerm);
    }
</script>

<table style="margin: 0 auto; width: 100%;" class="table">
    <tr>
        <td>
            <h1 style="margin: 0;padding: 0;">BTS</h1>
        </td>

        <c:if test="${!empty sessionScope.userSession}">
            <td>
                <button class="btn btn-default" onclick="location.href='${pageContext.request.contextPath}/view?tab1=main&action=form'">
                    Dashboard
                        <%--<span style="font-size: 1.7em;vertical-align: middle;" class="material-icons">add</span>--%>
                </button>
            </td>
            <td>
                <button class="btn btn-primary" onclick="showCreateDialog();">
                    Create
                        <%--<span style="font-size: 1.7em;vertical-align: middle;" class="material-icons">add</span>--%>
                </button>
            </td>

            <td style="width: 100%"></td>
            
            <td style="text-align: right;white-space: nowrap">
                <form method="post" action="${pageContext.request.contextPath}/view?tab1=main&action=search">
                    <input type="text" size="12" maxlength="255" id="searchTerm" name="searchTerm" value="${issuesForm.title}"/>
                    <input type="hidden" id="header_title" name="title"/>
                    <input type="hidden" id="header_description" name="description"/>
                    <%--<input type="submit" value="Search" class="btn btn-primary btn-xs" onclick="doSearch($('#searchTerm').val());"/>--%>
                    <button class="btn btn-default" style="padding:5px 2px 3px 4px;" onclick="doSearch($('#searchTerm').val());">
                        <span style="font-size: 1.7em;vertical-align: middle;" class="material-icons">search</span>
                    </button>
                </form>
            </td>
            <td style="text-align: right">${sessionScope.userSession.logonId}</td>
            <td style="text-align: right">
                <button class="btn btn-default" style="padding:5px 3px 4px 4px;" onclick="location.href='${pageContext.request.contextPath}/view?tab1=main&action=logout';">
                    <span style="font-weight:normal;font-size: 1.7em;vertical-align: middle;" class="material-icons">settings</span>
                </button>
            </td>
            <td style="text-align: right">
                <%--<input type="button" class="btn btn-primary btn-xs" value="logout" onclick="location.href='${pageContext.request.contextPath}/view?tab1=main&action=logout';"/>--%>
                <button class="btn btn-default" style="padding:5px 3px 3px 4px;" onclick="location.href='${pageContext.request.contextPath}/view?tab1=main&action=logout';">
                    <span style="font-weight:normal;font-size: 1.7em;vertical-align: middle;" class="material-icons">power_settings_new</span>
                </button>
            </td>
        </c:if>
    </tr>
</table>