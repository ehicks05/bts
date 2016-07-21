package com.hicks.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;

public class TextToInputTextTag extends TagSupport
{
    private String id = "";
    private String text = "";

    @Override
    public int doStartTag() throws JspException
    {
        JspWriter writer = pageContext.getOut();

        int count = TagUtils.updateCount(this, pageContext);

        String output = "";

        // print js script
        if (count == 1)
        {
            output += "\n" +
                    "    <script>\n" +
                    "        function enableEditMode(element)\n" +
                    "        {\n" +
                    "            $(document).on('click', '#' + element.id + 'SaveButton', function(){updateIssue(element.id, element.innerHTML)});\n" +
                    "        }\n" +
                    "        function disableEditMode(element)\n" +
                    "        {\n" +
                    "\n" +
                    "        }\n" +
                    "    </script>";
        }

        String saveButtonId = id + "SaveButton";
        String cancelButtonId = id + "CancelButton";
        output += "" +
                "        <div class=\"mdl-card__supporting-text\" contenteditable=\"true\" id=\"" + id + "\" onfocus=\"enableEditMode(this)\" onblur=\"disableEditMode(this)\">\n" +
                "            " + text + "\n" +
                "        </div>\n" +
                "\n" +
                "        <ul class=\"mdl-menu mdl-menu--bottom-right mdl-js-menu mdl-js-ripple-effect\" id=\"fldDescriptionMenu\" for=\"fldDescription\">\n" +
                "            <li class=\"mdl-menu__item\" id=\"" + saveButtonId + "\"><i class=\"material-icons\" style=\"vertical-align:middle;color: green;\">add</i>Save</li>\n" +
                "            <li class=\"mdl-menu__item\" id=\"" + cancelButtonId + "\"><i class=\"material-icons\" style=\"vertical-align:middle;color: red;\">clear</i>Dismiss</li>\n" +
                "        </ul>";

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

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }
}
