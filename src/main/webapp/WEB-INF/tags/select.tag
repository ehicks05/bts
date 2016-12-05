<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@tag description="Text To Select Tag" pageEncoding="UTF-8" %>
<%@attribute name="id" fragment="false" %>
<%@attribute name="value" fragment="false" %>
<%@attribute name="items" fragment="false" type="java.util.List<com.hicks.ISelectTagSupport>" %>
<%@attribute name="formatFunction" fragment="false" %>

<c:set var="selectCounter" value="${requestScope.selectCounter + 1}" scope="request"/>
<c:if test="${selectCounter == 1}">
    <script>

    </script>
</c:if>

<script>
    $(document).ready(function ()
    {
        $('#${id}').select2({
            templateResult: ${formatFunction},
            templateSelection: ${formatFunction}
        }).val('${value}').trigger('change');
    });
</script>
<select id="${id}" name="${id}" class="js-example-basic-single" style="width: 60%;">
    <c:forEach var="item" items="${items}">
        <option value="${item.value}">${item.text}</option>
    </c:forEach>
</select>