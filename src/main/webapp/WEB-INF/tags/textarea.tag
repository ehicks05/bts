<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://eric-hicks.com/bts/commontags" %>

<%@tag description="Textarea with Label" pageEncoding="UTF-8" %>
<%@attribute name="id" fragment="false" %>
<%@attribute name="label" fragment="false" %>
<%@attribute name="horizontal" fragment="false" %>
<%@attribute name="labelClass" fragment="false" %>
<%@attribute name="value" fragment="false" %>

<c:if test="${horizontal}">
    <div class="field is-horizontal">
        <div class="field-label is-normal">
            <label class="label">${label}</label>
        </div>
        <div class="field-body">
            <div class="field">
                <div class="control">
                    <textarea class="textarea" id="${id}" name="${id}" placeholder="${label}">${value}</textarea>
                </div>
            </div>
        </div>
    </div>
</c:if>
<c:if test="${!horizontal}">
    <div class="field">
        <label class="label">${label}</label>
        <div class="control">
            <textarea class="textarea" id="${id}" name="${id}" placeholder="${label}">${value}</textarea>
        </div>
    </div>
</c:if>