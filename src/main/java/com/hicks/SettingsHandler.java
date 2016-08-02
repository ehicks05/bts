package com.hicks;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

public class SettingsHandler
{
    public static String showSettings(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        return "/WEB-INF/webroot/settings.jsp";
    }
}
