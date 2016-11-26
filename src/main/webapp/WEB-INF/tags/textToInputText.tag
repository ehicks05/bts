<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@tag description="Text To Input Tag" pageEncoding="UTF-8" %>
<%@attribute name="id" fragment="false" %>
<%@attribute name="text" fragment="false" %>
<%@attribute name="submitAction" fragment="false" %>

<c:set var="textToInputTextCounter" value="${requestScope.textToInputTextCounter + 1}" scope="request"/>
<c:if test="${textToInputTextCounter == 1}">
    <script>
        function enableEditMode(element, url)
        {
            $(document).on('click', '#' + element.id + 'SaveButton', function ()
            {
                update(element.id, element.innerHTML, url)
            });
        }
        function disableEditMode(element)
        {

        }
    </script>
</c:if>

<div class="editable" contenteditable="true" id="${id}" onfocus="enableEditMode(this, '${submitAction}')" onblur="disableEditMode(this)">
    ${text}
</div>

<ul class="mdl-menu mdl-menu--bottom-left mdl-js-menu mdl-js-ripple-effect" id="${id}Menu" for="${id}">
    <li class="mdl-menu__item" id="${id}SaveButton"><i class="material-icons" style="vertical-align:middle;color: green;">add</i>Save</li>
    <li class="mdl-menu__item" id="${id}CancelButton"><i class="material-icons" style="vertical-align:middle;color: red;">clear</i>Dismiss</li>
</ul>
