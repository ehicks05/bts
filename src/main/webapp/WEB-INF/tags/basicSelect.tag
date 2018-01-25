<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@tag description="Text with Label" pageEncoding="UTF-8" %>
<%@attribute name="id" fragment="false" %>
<%@attribute name="value" fragment="false" required="false" %>
<%@attribute name="items" fragment="false" type="java.util.List" %>
<%@attribute name="label" fragment="false" %>
<%@attribute name="placeholder" fragment="false" required="false" %>
<%@attribute name="horizontal" fragment="false" required="false" %>
<%@attribute name="required" fragment="false" required="false" %>

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

<div class="field ${isHorizontal}">
    <div class="field-label">
        <label class="label">${label}</label>
    </div>
    <div class="field-body">
        <div class="control">
            <div class="select">
                <select id="${id}" name="${id}" <c:if test="${required}">required</c:if>>
                    <c:forEach var="item" items="${items}">
                        <c:set var="selected" value="${!empty value && item.value eq value}" />
                        <option value="${item.value}">${item.text}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
    </div>
</div>