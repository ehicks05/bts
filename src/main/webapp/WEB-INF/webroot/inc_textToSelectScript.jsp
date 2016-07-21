<script type="text/javascript">
    function select2Enable(textId, selectId)
    {
        var currentText = $('#' + textId).text();

        // get value of the <option> that matches our textDiv.
        var currentValue = $('#' + selectId + ' option').filter(function () { return $(this).html() == currentText; }).val();

//                alert(currentValue);
        $('#' + textId).hide();
        $('#' + selectId).val(currentValue).trigger("change");
        $('#' + selectId).show()            ;
        $('#' + selectId).select2()         ;
        $('#' + selectId).select2('open')   ;
        $('#' + selectId).on("select2:close", function (e) {
            select2Disable(textId, selectId, $('#' + selectId).val());
        });
    }

    function select2Disable(textId, selectId, optionValue)
    {
        var newText = $("#" + selectId + " option[value='" + optionValue + "']").text();
        var newValue = $("#" + selectId + " option[value='" + optionValue + "']").val();
//                alert(newText);
        var oldText = $('#' + textId).text();
        if (newText != oldText)
        {
            updateIssue(selectId, newValue);
        }

        $('#' + textId).text(newText);
        $('#' + textId).show();
        $('#' + selectId).hide();
        $('#' + selectId).select2('destroy');
    }
</script>

<div style="padding: 4px;" id="myText" onclick="select2Enable(this.id, $('#fldZone').attr('id'))">${issue.zone.name}</div>
<select id="fldZone" class="js-example-basic-single" style="display:none; width: 25%">
    <option value="AL">Alabama</option>
    <option value="NJ">New Jersey</option>
    <option value="WY">Wyoming</option>
    <c:forEach var="zone" items="${zones}">
        <option value="${zone.id}">${zone.name}</option>
    </c:forEach>
</select>