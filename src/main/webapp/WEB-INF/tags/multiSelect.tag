<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="http://eric-hicks.com/bts/commontags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@tag description="Multi Select Tag" pageEncoding="UTF-8" %>
<%@attribute name="id" fragment="false" %>
<%@attribute name="items" fragment="false" type="java.lang.Object" %>
<%@attribute name="selectedValues" fragment="false" %>
<%@attribute name="placeHolder" fragment="false" %>
<%@attribute name="isSpring" fragment="false" %>

<c:set var="multiSelectCounter" value="${requestScope.multiSelectCounter + 1}" scope="request"/>
<c:if test="${multiSelectCounter == 1}">
    <script>

    </script>
</c:if>

<c:if test="${isSpring}">
    <form:select id="${id}" path="${id}" items="${items}" itemLabel="name" itemValue="id"
                 class="js-example-basic-multiple" multiple="true" />
</c:if>
<c:if test="${!isSpring}">
    <c:set var="items" value="${ct:parseItemsForSelect(items)}" />
    <select id="${id}" name="${id}" class="js-example-basic-multiple" multiple>
        <c:forEach var="item" items="${items}">
            <c:set var="selected" value=""/>
            <c:if test="${selectedValues.contains(item.value)}">
                <c:set var="selected" value="selected"/>
            </c:if>

            <option value="${item.value}" ${selected}>${item.text}</option>
        </c:forEach>
    </select>
</c:if>
<script>
    $(function() {
        $('#${id}').select2({
            placeholder: "${placeHolder}"
        });
    });
</script>
