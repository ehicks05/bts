<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@tag description="Text with Label" pageEncoding="UTF-8" %>
<%@attribute name="id" fragment="false" %>
<%@attribute name="value" fragment="false" required="false" %>
<%@attribute name="label" fragment="false" %>
<%@attribute name="horizontal" fragment="false" required="false"  %>
<%@attribute name="required" fragment="false" required="false"  %>
<%@attribute name="isStatic" fragment="false" required="false"  %>
<%@attribute name="isSpring" fragment="false" required="false"  %>

<c:if test="${empty horizontal}">
    <c:set var="horizontal" value="${true}" />
</c:if>
<c:set var="required" value="${required ? 'required' : ''}" />
<c:set var="isStatic" value="${isStatic ? 'is-static' : ''}"/>

<c:if test="${!horizontal}">
    <div class="field">
        <label class="label">${label}</label>
        <div class="control">
            <c:if test="${isSpring}">
                <form:input path="${id}" class="input ${isStatic}" placeholder="${label}" value="${value}"
                            required="${required}"/>
            </c:if>
            <c:if test="${!isSpring}">
                <input class="input ${isStatic}" type="text" placeholder="${label}" id="${id}" name="${id}" value="${value}" ${required}/>
            </c:if>
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
                <c:if test="${isSpring}">
                    <form:input path="${id}" class="input ${isStatic}" placeholder="${label}" value="${value}"
                                required="${required}"/>
                </c:if>
                <c:if test="${!isSpring}">
                    <input class="input ${isStatic}" type="text" placeholder="${label}" id="${id}" name="${id}" value="${value}" ${required}/>
                </c:if>
            </div>
        </div>
    </div>
</c:if>