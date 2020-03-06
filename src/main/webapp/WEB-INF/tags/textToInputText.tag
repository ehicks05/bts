<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://eric-hicks.com/bts/commontags" %>
<%@tag description="Text To Input Tag" pageEncoding="UTF-8" %>

<%@attribute name="id" fragment="false" required="false" %>
<%@attribute name="text" fragment="false" required="false" %>
<%@attribute name="submitAction" fragment="false" required="false" %>

<c:set var="textToInputTextCounter" value="${requestScope.textToInputTextCounter + 1}" scope="request"/>
<c:if test="${textToInputTextCounter == 1}">
    <script src="${pageContext.request.contextPath}/ckeditor/ckeditor.js"></script>
    <script>
        CKEDITOR.disableAutoInline = true;
        CKEDITOR.config.extraAllowedContent = 'div(*)';
    </script>
</c:if>

<div contenteditable="true" id="${id}" class="editable">${text}</div>
<script>
    CKEDITOR.inline( '${id}', {
        on: {
            instanceReady: function( evt ) {
                var editor = evt.editor;

                editor.commands.save.setState(CKEDITOR.TRISTATE_DISABLED);

                editor.on( 'change', function( ev ) {
                    ev.editor.commands.save.setState(CKEDITOR.TRISTATE_ON);
                });

                editor.on( 'save', function( ev ) {
                    var editor = ev.editor;
                    var data = editor.getData();

                    if (data != '${t:escapeJS(text)}')
                    {
                        update('${id}', data, '${submitAction}');
                    }
                    editor.commands.save.setState(CKEDITOR.TRISTATE_DISABLED);
                });
            }
        }
    });
</script>