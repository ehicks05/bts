<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@tag description="Textarea with Label" pageEncoding="UTF-8" %>
<%@attribute name="id" fragment="false" %>
<%@attribute name="value" fragment="false" required="false" %>
<%@attribute name="label" fragment="false" %>
<%@attribute name="placeholder" fragment="false" required="false" %>
<%@attribute name="horizontal" fragment="false" required="false"  %>
<%@attribute name="required" fragment="false" required="false"  %>
<%@attribute name="readOnly" fragment="false" required="false"  %>

<c:set var="selectCounter" value="${requestScope.selectCounter + 1}" scope="request"/>
<c:if test="${selectCounter == 1}">
    <script>

    </script>
</c:if>

<c:if test="${empty placeholder}">
    <c:set var="placeholder" value="${label}" />
</c:if>
<c:if test="${empty horizontal || horizontal}">
    <c:set var="isHorizontal" value="is-horizontal" />
</c:if>
<c:set var="readOnly" value="${readOnly ? 'readOnly' : ''}"/>
<c:set var="required" value="${required ? 'required' : ''}"/>


<div class="field ${isHorizontal}">
    <div class="field-label">
        <label class="label">${label}</label>
    </div>
    <div class="field-body">
        <div class="field">
            <div class="control">
                <textarea class="textarea" id="${id}" name="${id}" placeholder="${placeholder}" ${readOnly} ${required}>${value}</textarea>
            </div>
        </div>
    </div>
</div>