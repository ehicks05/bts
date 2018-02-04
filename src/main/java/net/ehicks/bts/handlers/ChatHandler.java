package net.ehicks.bts.handlers;

import net.ehicks.bts.UserSession;
import net.ehicks.bts.routing.Route;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

public class ChatHandler
{
    @Route(tab1 = "chat", tab2 = "", tab3 = "", action = "form")
    public static String showProfile(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");


        return "/webroot/chat.jsp";
    }
}
