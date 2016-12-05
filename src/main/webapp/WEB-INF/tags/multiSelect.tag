<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@tag description="Multi Select Tag" pageEncoding="UTF-8" %>
<%@attribute name="id" fragment="false" %>
<%@attribute name="items" fragment="false" type="java.util.List<com.hicks.ISelectTagSupport>" %>
<%@attribute name="selectedValues" fragment="false" %>
<%@attribute name="placeHolder" fragment="false" %>

<c:if test="${empty placeHolder}">
    <c:set var="placeHolder" value="Any"/>
</c:if>

<c:set var="multiSelectCounter" value="${requestScope.multiSelectCounter + 1}" scope="request"/>
<c:if test="${multiSelectCounter == 1}">
    <script>
       $(document).ready(function () {
          initSumo();
       });

       function initSumo()
       {
           $('.mySumo').SumoSelect({
               placeholder: '${placeHolder}',
               captionFormatAllSelected: '{0} selected',
               csvDispCount: 3,
               search: true});
       }
   </script>
</c:if>

<select name="${id}" id="${id}" class="mySumo" multiple>
    <c:forEach var="item" items="${items}">
        <c:set var="selected" value=""/>
        <c:if test="${selectedValues.contains(item.value)}">
            <c:set var="selected" value="selected"/>
        </c:if>

        <option value="${item.value}" ${selected}>${item.text}</option>
    </c:forEach>
</select>