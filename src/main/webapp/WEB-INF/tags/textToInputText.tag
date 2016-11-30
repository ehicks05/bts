<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@tag description="Text To Input Tag" pageEncoding="UTF-8" %>
<%@attribute name="id" fragment="false" %>
<%@attribute name="text" fragment="false" %>
<%@attribute name="submitAction" fragment="false" %>

<c:set var="textToInputTextCounter" value="${requestScope.textToInputTextCounter + 1}" scope="request"/>
<c:if test="${textToInputTextCounter == 1}">

</c:if>

<div contenteditable="true" id="${id}" class="editable">${text}</div>
<script>
    CKEDITOR.disableAutoInline = true;
    CKEDITOR.config.extraAllowedContent = 'div(*)';
    CKEDITOR.inline( '${id}' );

    CKEDITOR.on( 'instanceReady', function( evt ) {
        var editor = evt.editor;

        editor.commands.save.setState(CKEDITOR.TRISTATE_DISABLED);

        editor.on( 'change', function( ev ) {
            ev.editor.commands.save.setState(CKEDITOR.TRISTATE_ON);
        });

        editor.on( 'save', function( ev ) {
            var data = ev.editor.getData();

            if (data != '${text}')
            {
                update('${id}', ev.editor.getData(), '${submitAction}');
                editor.commands.save.setState(CKEDITOR.TRISTATE_DISABLED);
                editor.commands.undo.setState(CKEDITOR.TRISTATE_DISABLED);
            }
        });
    });

    CKEDITOR.on( 'loaded', function( evt ) {
        // your stuff here
    } );

</script>