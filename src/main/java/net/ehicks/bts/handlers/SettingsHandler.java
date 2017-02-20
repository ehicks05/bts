package net.ehicks.bts.handlers;

import net.ehicks.bts.Route;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

public class SettingsHandler
{
    @Route(tab1 = "settings", tab2 = "", tab3 = "", action = "form")
    public static String showSettings(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        return "/WEB-INF/webroot/settings.jsp";
    }
}
