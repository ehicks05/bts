package com.hicks.tags;

import com.hicks.ISelectTagSupport;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;

public class SelectTag extends TagSupport
{
    private String id = "";
    private String value = "";
    private List<ISelectTagSupport> items;
    private boolean required;

    @Override
    public int doStartTag() throws JspException
    {
        JspWriter writer = pageContext.getOut();

        int count = TagUtils.updateCount(this, pageContext);

        String output = "";

        // print js script
        if (count == 1)
        {
            String allowClear = required ? "false" : "true";
            output +=
                    "\n        <script type=\"text/javascript\">\n" +
                    "            $(document).ready(function () {$('.js-example-basic-single').select2({width: 'resolve', allowClear: " + allowClear + ", placeholder: 'Any'})});\n" +
                    "        </script>\n";
        }

        output += "        <select name=\"" + id + "\" id=\"" + id + "\" class=\"js-example-basic-single\">\n";

        if (!required)
            output += "            <option></option>\n";
        for (ISelectTagSupport item : items)
        {
            String selected = item.getValue().equals(value) ? " selected" : "";
            output += "            <option value=\"" + item.getValue() + "\" " + selected + ">" + item.getText() + "</option>\n";
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

    public List<ISelectTagSupport> getItems()
    {
        return items;
    }

    public void setItems(List<ISelectTagSupport> items)
    {
        this.items = items;
    }

    public boolean isRequired()
    {
        return required;
    }

    public void setRequired(boolean required)
    {
        this.required = required;
    }
}
