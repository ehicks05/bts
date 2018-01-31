package net.ehicks.bts.tags;

import net.ehicks.bts.ISelectTagSupport;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TagUtils
{
    public static Date longToDate(long input)
    {
        return new Date(input);
    }

    public static String formatZonedDateTime(ZonedDateTime zonedDateTime)
    {
        return zonedDateTime.format(DateTimeFormatter.RFC_1123_DATE_TIME);
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

    private static class SelectTagObject implements ISelectTagSupport
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
