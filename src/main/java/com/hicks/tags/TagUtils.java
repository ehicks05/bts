package com.hicks.tags;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

public class TagUtils
{
    public static int updateCount(TagSupport tag, PageContext pageContext)
    {
        String className = tag.getClass().getSimpleName();
        Object textToSelectTags = pageContext.getAttribute(className);
        int count = 0;
        if (textToSelectTags == null)
        {
            count++;
            pageContext.setAttribute(className, count);
        }
        else
        {
            count = ((Integer) textToSelectTags) + 1;
            pageContext.setAttribute(className, count);
        }

        return count;
    }

    public static int getCount(TagSupport tag, PageContext pageContext)
    {
        return (int) pageContext.getAttribute(tag.getClass().getSimpleName());
    }
}
