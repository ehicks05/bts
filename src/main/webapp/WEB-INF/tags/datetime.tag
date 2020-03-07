<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@tag description="Datetime with Label" pageEncoding="UTF-8" %>
<%@attribute name="id" fragment="false" %>
<%@attribute name="value" fragment="false" required="false" %>
<%@attribute name="label" fragment="false" %>
<%@attribute name="horizontal" fragment="false" required="false"  %>

<c:if test="${empty horizontal}">
    <c:set var="horizontal" value="${true}" />
</c:if>

<c:if test="${!horizontal}">
    <div class="field">
        <label class="label">${label}</label>
        <div class="control">
            <input class="input" type="datetime-local"  placeholder="${label}" id="${id}" name="${id}" value="${value}"/>
        </div>
    </div>
</c:if>

<c:if test="${horizontal}">
    <div class="field is-horizontal">
        <div class="field-label is-normal">
            <label class="label">${label}</label>
        </div>
        <div class="field-body">
            <div class="control">
                <input class="input" type="datetime-local" placeholder="${label}" id="${id}" name="${id}" value="${value}"/>
            </div>
        </div>
    </div>
</c:if>