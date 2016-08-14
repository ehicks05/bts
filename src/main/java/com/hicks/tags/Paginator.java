package com.hicks.tags;

import com.hicks.SearchResult;
import com.hicks.beans.IssueForm;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.text.DecimalFormat;

public class Paginator extends TagSupport
{
    private String id = "";
    private IssueForm issueForm;
    private SearchResult searchResult;

    @Override
    public int doStartTag() throws JspException
    {
        JspWriter writer = pageContext.getOut();

        int count = TagUtils.updateCount(this, pageContext);

        String output = "";

        // print js script
        if (count == 1)
        {
            output +=
                    "\n        <script type=\"text/javascript\">\n" +
                            "            $(document).ready(function () {$('.js-example-basic-single').select2({width: 'resolve', allowClear: false, placeholder: 'Any'})});\n" +
                            "        </script>\n";
        }

        output +=
                "<tr>\n" +
                "    <td colspan=\"100\" style=\"text-align: center;\">\n";

        if (searchResult.isHasPrevious())
        {
            output +=
                "        <span onclick=\"goToPage('first')\" style=\"vertical-align: middle;\" class=\"material-icons\">first_page</span>\n" +
                "        <span onclick=\"goToPage('previous')\" style=\"vertical-align: middle;\" class=\"material-icons\">chevron_left</span>\n";
        }

        String formattedPage = new DecimalFormat("#,###").format(issueForm.getPage());
        String formattedPages = new DecimalFormat("#,###").format(searchResult.getPages());
        output +=
                "\n" +
                "        <span class=\"currentPageSpan\" style=\"vertical-align: middle;\">" + formattedPage + " of " + formattedPages + "</span>\n";

        if (searchResult.isHasNext())
        {
            output +=
                "        <span onclick=\"goToPage('next')\" style=\"vertical-align: middle;\" class=\"material-icons\">chevron_right</span>\n" +
                "        <span onclick=\"goToPage('last')\" style=\"vertical-align: middle;\" class=\"material-icons\">last_page</span>\n";
        }

        output +=
                "    </td>\n" +
                "</tr>";

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

    public IssueForm getIssueForm()
    {
        return issueForm;
    }

    public void setIssueForm(IssueForm issueForm)
    {
        this.issueForm = issueForm;
    }

    public SearchResult getSearchResult()
    {
        return searchResult;
    }

    public void setSearchResult(SearchResult searchResult)
    {
        this.searchResult = searchResult;
    }
}
