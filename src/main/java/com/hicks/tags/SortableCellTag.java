package com.hicks.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class SortableCellTag extends TagSupport
{
    private String code = "";
    private String label = "";
    private String style = "";
    private String issueFormId = "";
    private String sortColumn = "";
    private String sortDirection = "";

    @Override
    public int doStartTag() throws JspException
    {
        JspWriter writer = pageContext.getOut();

        String output =
                "        <td id=\"fld_" + code + issueFormId + "\" class=\"sortableHeader\" style=\"" + style + "\" onclick=\"ajaxItems(this.id, '" + pageContext.getRequest().getServletContext().getContextPath() + "', '" + issueFormId + "', 0, '" + code + "', '" + sortDirection + "')\">" + label + "\n" +
                "            <span>\n";

        if (code.equals(sortColumn))
        {
            if (sortDirection.equals("asc")) output += "&#9650;";
            if (sortDirection.equals("desc")) output += "&#9660;";
        }

        output +=
                "            </span>\n" +
                "        </td>";

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

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public String getStyle()
    {
        return style;
    }

    public void setStyle(String style)
    {
        this.style = style;
    }

    public String getIssueFormId()
    {
        return issueFormId;
    }

    public void setIssueFormId(String issueFormId)
    {
        this.issueFormId = issueFormId;
    }

    public String getSortColumn()
    {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn)
    {
        this.sortColumn = sortColumn;
    }

    public String getSortDirection()
    {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection)
    {
        this.sortDirection = sortDirection;
    }
}
