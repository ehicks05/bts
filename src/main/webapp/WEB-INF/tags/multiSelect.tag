<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@tag description="Multi Select Tag" pageEncoding="UTF-8" %>
<%@attribute name="id" fragment="false" %>
<%@attribute name="items" fragment="false" type="java.util.List<net.ehicks.bts.ISelectTagSupport>" %>
<%@attribute name="selectedValues" fragment="false" %>
<%@attribute name="placeHolder" fragment="false" %>

<c:set var="multiSelectCounter" value="${requestScope.multiSelectCounter + 1}" scope="request"/>
<c:if test="${multiSelectCounter == 1}">

</c:if>

<script>
    $(document).ready(function ()
    {
        $('#${id}').select2({
            placeholder: "${placeHolder} (Any)"
        });
        $('#${id}').on('select2:open', function (e) {
            var container = $(this).select('select2-container');
            var position = container.offset().top;
            var availableHeight = $(window).height() - position - container.outerHeight();
            var bottomPadding = 50; // Set as needed
            $('ul.select2-results__options').css('max-height', (availableHeight - bottomPadding) + 'px');
        });
    });
</script>
<select id="${id}" name="${id}" class="js-example-basic-single" multiple>
    <c:forEach var="item" items="${items}">
        <c:set var="selected" value=""/>
        <c:if test="${selectedValues.contains(item.value)}">
            <c:set var="selected" value="selected"/>
        </c:if>

        <option value="${item.value}" ${selected}>${item.text}</option>
    </c:forEach>
</select>