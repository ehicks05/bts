package com.hicks;

import java.util.Arrays;
import java.util.List;

public class Common
{
    public static String toMetric(long in)
    {
        double value = in;
        List<String> units = Arrays.asList("", "K", "M", "G");
        int unit = 0;
        while (value > 1024)
        {
            value /= 1024;
            unit++;
        }
        return String.format("%.2f", value) + " " + units.get(unit) + "B";
    }
}
