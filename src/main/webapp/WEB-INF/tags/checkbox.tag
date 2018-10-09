<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@tag description="Checkbox with Label" pageEncoding="UTF-8" %>
<%@attribute name="id" fragment="false" %>
<%@attribute name="checked" fragment="false" type="java.lang.Boolean" %>
<%@attribute name="label" fragment="false" %>
<%@attribute name="horizontal" fragment="false" required="false" %>

<c:set var="selectCounter" value="${requestScope.selectCounter + 1}" scope="request"/>
<c:if test="${selectCounter == 1}">
    <script>

    </script>
</c:if>

<c:if test="${empty horizontal || horizontal}">
    <c:set var="isHorizontal" value="is-horizontal" />
</c:if>

<div class="field ${isHorizontal}">
    <div class="field-label">
        <label class="label" for="${id}">${label}</label>
    </div>
    <div class="field-body">
        <div class="control">
            <input class="checkbox" type="checkbox" id="${id}" name="${id}" ${checked ? "checked" : ""} />
        </div>
    </div>
</div>