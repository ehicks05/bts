package com.hicks.tags;

import com.hicks.ISelectTagSupport;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.ArrayList;
import java.util.List;

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

    public static List<ISelectTagSupport> stringToISelectTag(String input)
    {
        int indexOfPipe = input.indexOf("|");
        String delimiter = ",";
        if (indexOfPipe != -1)
            delimiter = "|";
        List<ISelectTagSupport> selectTagObjects = new ArrayList<>();
        for (String item : input.split(delimiter))
        {
            boolean compound = item.contains("=");
            String value = item;
            String text = item;
            if (compound)
            {
                value = item.split("=")[0];
                text = item.split("=")[1];
            }
            selectTagObjects.add(new SelectTagObject(value, text));
        }
        return selectTagObjects;
    }

    static class SelectTagObject implements ISelectTagSupport
    {
        String value = "";
        String text = "";

        public SelectTagObject(String value, String text)
        {
            this.value = value;
            this.text = text;
        }

        @Override
        public String getValue()
        {
            return value;
        }

        @Override
        public String getText()
        {
            return text;
        }
    }
}
