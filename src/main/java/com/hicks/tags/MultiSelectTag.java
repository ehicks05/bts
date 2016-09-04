package com.hicks.tags;

import com.hicks.ISelectTagSupport;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MultiSelectTag extends TagSupport
{
    private String id = "";
    private List<String> selectedValues;
    private List<ISelectTagSupport> items;
    private boolean required;

    @Override
    public int doStartTag() throws JspException
    {
        JspWriter writer = pageContext.getOut();

        if (selectedValues == null)
            selectedValues = new ArrayList<>();

        int count = TagUtils.updateCount(this, pageContext);

        String output = "";

        // print js script
        if (count == 1)
        {
            String allowClear = required ? "false" : "true";
            output +=
                    "\n        <script>\n" +
                            "            $(document).ready(function () {" +
                            "               $('.mySumo').SumoSelect({\n" +
                            "                placeholder: 'Any',\n" +
                            "                captionFormatAllSelected: '{0} selected',\n" +
                            "                csvDispCount: 3,\n" +
                            "                search: true\n" +
                            "            })});" +
                            "        </script>\n";
        }

        output += "        <select name=\"" + id + "\" id=\"" + id + "\" class=\"mySumo\" multiple>\n";

        for (ISelectTagSupport item : items)
        {
            String selected = selectedValues.contains(item.getValue()) ? " selected" : "";
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

    public List<String> getSelectedValues()
    {
        return selectedValues;
    }

    public void setSelectedValues(List<String> selectedValues)
    {
        this.selectedValues = selectedValues;
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
