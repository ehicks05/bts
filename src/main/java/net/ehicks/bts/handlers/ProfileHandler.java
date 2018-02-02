package net.ehicks.bts.handlers;

import net.ehicks.bts.routing.Route;
import net.ehicks.bts.UserSession;
import net.ehicks.bts.beans.User;
import net.ehicks.common.Common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

public class ProfileHandler
{
    @Route(tab1 = "profile", tab2 = "", tab3 = "", action = "form")
    public static String showProfile(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");

        Long userId = Common.stringToLong(request.getParameter("userId"));
        User user = User.getByUserId(userId);

        if (!userId.equals(userSession.getUserId()) && !User.getAllVisible(userSession.getUserId()).contains(user))
            return "/webroot/error.jsp";

        request.setAttribute("user", user);

        return "/webroot/profile.jsp";
    }
}
