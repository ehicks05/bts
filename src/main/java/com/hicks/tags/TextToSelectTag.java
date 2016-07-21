package com.hicks.tags;

import com.hicks.ISelectTagSupport;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;

public class TextToSelectTag extends TagSupport
{
    private String id = "";
    private String value = "";
    private String text = "";
    private List<ISelectTagSupport> items;

    @Override
    public int doStartTag() throws JspException
    {
        JspWriter writer = pageContext.getOut();

        int count = TagUtils.updateCount(this, pageContext);

        String output = "";

        // print js script
        if (count == 1)
        {
            output += "        <script type=\"text/javascript\">\n" +
                    "            $(document).ready(function ()\n" +
                    "            {\n" +
                    "//                $('#fldZone').hide();\n" +
                    "            });\n" +
                    "\n" +
                    "            function select2Enable(textId, selectId)\n" +
                    "            {\n" +
                    "                var currentText = $('#' + textId).text();\n" +
                    "\n" +
                    "                // get value of the <option> that matches our textDiv.\n" +
                    "                var currentValue = $('#' + selectId + ' option').filter(function () { return $(this).html() == currentText; }).val();\n" +
                    "\n" +
                    "//                alert(currentValue);\n" +
                    "                $('#' + textId).hide();\n" +
                    "                $('#' + selectId).val(currentValue).trigger(\"change\");\n" +
                    "                $('#' + selectId).show()            ;\n" +
                    "                $('#' + selectId).select2()         ;\n" +
                    "                $('#' + selectId).select2('open')   ;\n" +
                    "                $('#' + selectId).on(\"select2:close\", function (e) {\n" +
                    "                    select2Disable(textId, selectId, $('#' + selectId).val());\n" +
                    "                });\n" +
                    "            }\n" +
                    "\n" +
                    "            function select2Disable(textId, selectId, optionValue)\n" +
                    "            {\n" +
                    "                var newText = $(\"#\" + selectId + \" option[value='\" + optionValue + \"']\").text();\n" +
                    "                var newValue = $(\"#\" + selectId + \" option[value='\" + optionValue + \"']\").val();\n" +
                    "//                alert(newText);\n" +
                    "                var oldText = $('#' + textId).text();\n" +
                    "                if (newText != oldText)\n" +
                    "                {\n" +
                    "                    updateIssue(selectId, newValue);\n" +
                    "                }\n" +
                    "\n" +
                    "                $('#' + textId).text(newText);\n" +
                    "                $('#' + textId).show();\n" +
                    "                $('#' + selectId).hide();\n" +
                    "                $('#' + selectId).select2('destroy');\n" +
                    "            }\n" +
                    "        </script>\n";
        }

        String divId = id + "Text";
        output += "        <div id=\"" + divId + "\" onclick=\"select2Enable(this.id, $('#" + id + "').attr('id'))\">" + text + "</div>\n" +
                "        <select id=\"" + id + "\" class=\"js-example-basic-single\" style=\"display:none;\">\n";

        for (ISelectTagSupport item : items)
        {
            output += "            <option value=\"" + item.getValue() + "\">" + item.getText() + "</option>\n";
        }
        output += "        </select>";

        try
        {
            writer.println(output);
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }

        return Tag.EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException
    {
        return Tag.EVAL_PAGE;
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public void setId(String id)
    {
        this.id = id;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public List<ISelectTagSupport> getItems()
    {
        return items;
    }

    public void setItems(List<ISelectTagSupport> items)
    {
        this.items = items;
    }
}
